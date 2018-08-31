package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.interfaces.Payload;

import br.com.ezvida.rst.model.Token;
import br.com.ezvida.rst.service.CredencialService;
import fw.security.binding.Autenticacao;
import fw.security.exception.UnauthorizedException;
import fw.security.interceptor.TipoOAuth;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/oauth")
public class AutorizacaoEndpoint extends SegurancaEndpoint<Token> {

    private static final long serialVersionUID = 1022960527905596654L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AutorizacaoEndpoint.class);

    private Token token;

    @Inject
    private CredencialService service;

    @POST
    @Encoded
	@Path("/autenticar")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response autenticar(@Context HttpServletRequest request) throws OAuthSystemException {
		LOGGER.debug("Solicitando autenticacao");
		Token token = service.validar(request);

		if (token == null) {
			throw new UnauthorizedException(getMensagem("app_rst_acesso_negado"));
		}

		//@formatter:off
        OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
            .setTokenType(OAuth.OAUTH_HEADER_NAME)
            .setAccessToken(token.getTokenAcesso())
            .setRefreshToken(token.getTokenAtualizacao())
            .setExpiresIn(token.getExpiraEm().toString())
            .buildJSONMessage();
        //@formatter:on

		getResponse().setCharacterEncoding("UTF-8");

		return Response.status(response.getResponseStatus()).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(response.getBody()).build();

	}

	@POST
	@Encoded
    @Path("/autorizar")
    @Autenticacao(TipoOAuth.ACCESS_TOKEN)
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    public Response autorizar(@Context SecurityContext context,	@Context HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
		LOGGER.debug("Solicitando autorizacao");

        //@formatter:off
        OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
            .setTokenType(OAuth.OAUTH_HEADER_NAME)
            .setAccessToken(token.getTokenAcesso())
            .setRefreshToken(token.getTokenAtualizacao())
            .setExpiresIn(token.getExpiraEm().toString())
            .buildJSONMessage();
        //@formatter:on

        getResponse().setCharacterEncoding("UTF-8");

        //@formatter:off
        return Response.status(response.getResponseStatus())
                .type(MediaType.APPLICATION_JSON)
                .header("Content-Version", getApplicationVersion())
                .entity(response.getBody()).build();
        //@formatter:on

    }

    @POST
    @Encoded
    @Path("/atualizar")
    @Autenticacao(TipoOAuth.JWT_BEARER)
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    public Response atualizar(@Context SecurityContext context,	@Context HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        //@formatter:off
        OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
				.setTokenType(OAuth.OAUTH_HEADER_NAME)
            .setAccessToken(token.getTokenAcesso())
            .setRefreshToken(token.getTokenAtualizacao())
            .setExpiresIn(token.getExpiraEm().toString())
            .buildJSONMessage();
		// @formatter:on

        getResponse().setCharacterEncoding("UTF-8");

        return Response.status(response.getResponseStatus()).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
                .entity(response.getBody()).build();

    }
    
    @Override
    public void validar(Payload payload) {
        token = service.validar(payload);
    }

}
