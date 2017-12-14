package br.com.ezvida.rst.web.endpoint.v1;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Encoded;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import br.com.ezvida.rst.model.Token;
import br.com.ezvida.rst.service.CredencialService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/public/v1/oauth")
public class AutenticacaoEndpoint extends SegurancaEndpoint<Token> {

	private static final long serialVersionUID = 1022960527905596654L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacaoEndpoint.class);

	@Inject
	private CredencialService service;

	
	@POST
	@Encoded
	@Path("/recuperar")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response recuperarSenha(@Encoded @QueryParam("email") String email) {
		LOGGER.debug("Recuperando a senha do usuário com email [ {} ]", email);
		getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());

		service.recuperarSenha(email);

		return Response.status(HttpServletResponse.SC_OK).entity(getMensagem("app_rst_autenticacao_email_enviado"))
				.header("Content-Version", getApplicationVersion()).type(MediaType.TEXT_PLAIN).build();
	}

	@POST
	@Encoded
	@Path("/alterarsenha")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response alterarSenha(@Encoded Map<String, String> propriedades) {
		LOGGER.debug("Alterando senha");
		getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());

		service.alterarSenha(propriedades);

		return Response.status(HttpServletResponse.SC_OK)
				.entity(getMensagem("app_rst_autenticacao_senha_alterada_sucesso"))
				.header("Content-Version", getApplicationVersion()).type(MediaType.TEXT_HTML).build();
	}

}
