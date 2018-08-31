package br.com.ezvida.rst.email;

import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.smtp.SMTPMessage;

import br.com.ezvida.rst.service.ParametroService;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EnviarEmailService extends BaseService {

	private static final long serialVersionUID = 7092057028221711713L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnviarEmailService.class);
	
	@Inject 
	private ParametroService parametroSerice;
	
	public void enviar(EmailTemplate template) {
		final VelocityTemplateService velocity = new VelocityTemplateService();


        LOGGER.debug("Processando envio de e-mail: {}", template);

        try {

			SMTPMessage smtp = new SMTPMessage(getSession());

			smtp.setFrom(getEnderecoRemetente());
			smtp.setSubject(template.getAssunto());
			smtp.setReturnOption(SMTPMessage.RETURN_HDRS);
			smtp.setNotifyOptions(SMTPMessage.NOTIFY_DELAY | SMTPMessage.NOTIFY_FAILURE | SMTPMessage.NOTIFY_SUCCESS);

			for (Entry<RecipientType, InternetAddress[]> destinatarios : template.getDestinatarios().entrySet()) {

				try {

					smtp.addRecipients(destinatarios.getKey(), destinatarios.getValue());

				} catch (MessagingException e) {
					throw new BusinessErrorException(getMensagem("app_error_enviar_email"), e);
				}

			}

            MimeMultipart mensagem = new MimeMultipart("related");

			BodyPart corpo = new MimeBodyPart();

			corpo.setContent(velocity.mergeTemplate(template.getNome(), template.getParametros()),
					"text/html; charset=UTF-8");

			mensagem.addBodyPart(corpo);
			smtp.setContent(mensagem);

			Transport.send(smtp);

        } catch (Exception e) {
        	LOGGER.error(e.getMessage(), e);
			throw new BusinessErrorException(getMensagem("app_error_enviar_email"), e);
        }

    }

	private Session getSession() throws NamingException {
		Properties props = new Properties();

		props.put(ParametroService.MAIL_SMTP_HOST.replaceAll("_","."),
				parametroSerice.carregarParametroPorNome(ParametroService.MAIL_SMTP_HOST).getValor());
		props.put(ParametroService.MAIL_SMTP_PORT.replaceAll("_", "."),
				parametroSerice.carregarParametroPorNome(ParametroService.MAIL_SMTP_PORT).getValor());
		props.put(ParametroService.MAIL_SMTP_AUTH.replaceAll("_", "."),
				parametroSerice.carregarParametroPorNome(ParametroService.MAIL_SMTP_AUTH).getValor());
		props.put(ParametroService.MAIL_SMTP_STARTTLS_ENABLE.replaceAll("_", "."),
				parametroSerice.carregarParametroPorNome(ParametroService.MAIL_SMTP_STARTTLS_ENABLE).getValor());
		props.put(ParametroService.MAIL_SMTP_STARTTLS_ENABLE.replaceAll("_", "."),
				parametroSerice.carregarParametroPorNome(ParametroService.MAIL_SSL_TRUST).getValor());

		Authenticator autenticacao = new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(
						parametroSerice.carregarPorNome(ParametroService.EMAIL_USERNAME_SESI),
						parametroSerice.carregarPorNome(ParametroService.EMAIL_SENHA_SESI));
			}
		};

		Session session = Session.getInstance(props, autenticacao);

		return session;
    }

	private InternetAddress getEnderecoRemetente() throws UnsupportedEncodingException {


		return new InternetAddress(parametroSerice.carregarPorNome(ParametroService.EMAIL_REMETENTE_SESI), parametroSerice.carregarPorNome(ParametroService.EMAIL_NOME_REMETENTE_SESI));
	}
}
