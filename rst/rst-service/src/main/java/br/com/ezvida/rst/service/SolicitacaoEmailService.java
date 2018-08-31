package br.com.ezvida.rst.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import br.com.ezvida.rst.email.EmailTemplate;
import br.com.ezvida.rst.email.EnviarEmailService;
import br.com.ezvida.rst.model.SolicitacaoEmail;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class SolicitacaoEmailService extends BaseService {

	private static final long serialVersionUID = 8864322457190735580L;

	@Inject
	private EnviarEmailService enviarEmailService;

	@Inject
	private ParametroService parametroService;

	public SolicitacaoEmail enviarEmail(SolicitacaoEmail solicitacaoEmail) {

		EmailTemplate template = null;
		try {

			String appNome = parametroService.carregarPorNome("appnome");
			String assunto = parametroService.carregarPorNome(ParametroService.EMAIL_ASSUNTO_SESI);
			if (!appNome.equals("")) {
				assunto = appNome.concat(" - ").concat(assunto);
			}
			if (solicitacaoEmail.getTelefone() != null) {
				template = new EmailTemplate("mailSolicitacaoSesi.vm");
			} else {
				template = new EmailTemplate("mailSolicitacaoSesiSemTelefone.vm");
			}
			template.parametro("appNome", parametroService.carregarPorNome(ParametroService.EMAIL_NOME_REMETENTE_SESI));
			template.setAssunto(assunto);
			template.destinatario(new InternetAddress(parametroService.carregarPorNome(ParametroService.SOLICITACAO_EMAIL_SESI)));
			template.parametro("corpo", parametroService.carregarParametroPorNome(ParametroService.SOLICITACAO_EMAIL_SESI_CORPO_TEXTO));
			template.parametro("nome", solicitacaoEmail.getNome());
			template.parametro("cpf", solicitacaoEmail.getCpf());

			if (solicitacaoEmail.getEmail() != null) {
				template.parametro("email", solicitacaoEmail.getEmail());
			}

			if (solicitacaoEmail.getTelefone() != null) {
				template.parametro("telefone", solicitacaoEmail.getTelefone());
			}
		} catch (AddressException e) {
			// LOGGER.error(e.getMessage());
			throw new BusinessErrorException(getMensagem("app_error_montar_template_email"), e);
		}

		enviarEmailService.enviar(template);
		return solicitacaoEmail;
	}
}
