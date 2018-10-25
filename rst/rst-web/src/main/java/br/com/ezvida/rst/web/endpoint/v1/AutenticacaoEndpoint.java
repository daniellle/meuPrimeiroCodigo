package br.com.ezvida.rst.web.endpoint.v1;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.girst.apiclient.model.SistemaCredenciado;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.service.SistemaCredenciadoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.core.exception.BusinessException;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import br.com.ezvida.rst.model.Token;
import br.com.ezvida.rst.service.CredencialService;
import fw.security.exception.UnauthorizedException;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/public/v1/oauth")
public class AutenticacaoEndpoint extends SegurancaEndpoint<Token> {

    private static final long serialVersionUID = 1022960527905596654L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacaoEndpoint.class);

    @Inject
    private CredencialService service;

    @Inject
    private SistemaCredenciadoService sistemaCredenciadoService;

    @POST
    @Encoded
    @Path("/recuperar")
    @Produces({MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_JSON})
    public Response alterarSenha(@Encoded Map<String, String> propriedades) {
        LOGGER.debug("Alterando senha");
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());

        service.alterarSenha(propriedades);

        return Response.status(HttpServletResponse.SC_OK)
            .entity(getMensagem("app_rst_autenticacao_senha_alterada_sucesso"))
            .header("Content-Version", getApplicationVersion()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Encoded
    @Path("/validarhash")
    @Produces({MediaType.APPLICATION_JSON})
    public Response validarHash(@Encoded @QueryParam("hash") String hash) {
        LOGGER.debug("Validando o hash [{}] para recuperacao de senha", hash);
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());

        service.verificarHash(hash);

        return Response.status(HttpServletResponse.SC_OK)
            .entity(getMensagem("app_rst_autenticacao_hash_validado_sucesso"))
            .header("Content-Version", getApplicationVersion()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Encoded
    @Path("/enviar-email-hash")
    @Produces({MediaType.APPLICATION_JSON})
    public Response reenviarEmailHash(@Encoded @QueryParam("hash") String hash) {
        LOGGER.debug("Reenviando e email para recuperacao de senha", hash);
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());

        service.reenviarEmailHash(hash);

        return Response.status(HttpServletResponse.SC_OK)
            .entity(getMensagem("app_rst_autenticacao_email_enviado"))
            .header("Content-Version", getApplicationVersion()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Encoded
    @Path("/sistema/autorizar/{login}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response autorizarSistema(@Context SecurityContext context, @Context HttpServletRequest request, @Encoded @PathParam("login") String login)
        throws OAuthSystemException {
        LOGGER.debug("Solicitando token de autorização");
        // Token token = service.validar(request, ClienteInfos.getUsuario(context).getLogin());
        Token token = service.validar(login);

        if (token == null) {
            throw new UnauthorizedException(getMensagem("app_rst_acesso_negado"));
        }

        LOGGER.debug("Token de autorização criada, criando oauthResponse");
        //@formatter:off
        OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
            .setTokenType(OAuth.OAUTH_HEADER_NAME)
            .setAccessToken(token.getTokenAcesso())
            .setRefreshToken(token.getTokenAtualizacao())
            .buildJSONMessage();
        //@formatter:on

        getResponse().setCharacterEncoding("UTF-8");

        LOGGER.debug("Retornando response");
        return Response.status(response.getResponseStatus()).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
            .entity(response.getBody()).build();

    }


    @PUT
    @Path("/credencial/reset")
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response resetarClientSecret(
        @Context SecurityContext context,
        @Context HttpServletRequest request,
        @Encoded SistemaCredenciado sistemaCredenciado) {
        String mensagem = sistemaCredenciadoService.resetarClientSecret(sistemaCredenciado);
        return javax.ws.rs.core.Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(mensagem)).build();
    }
}
