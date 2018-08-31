package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.service.UsuarioService;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/public/v1/usuarios")
public class UsuarioPublicoEndpoint extends SegurancaEndpoint<Usuario> {

    private static final long serialVersionUID = -3618279439764362012L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioPublicoEndpoint.class);

    @Inject
    @Preferencial
    private UsuarioService usuarioService;

	//@formatter:off
    @GET
    @Encoded
    @Path("/dados/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO_DADOS_CONSULTAR}))
    //@formatter:on
	public Response getDadosUsuario(@Context HttpServletRequest request, @Context SecurityContext context,
			@Encoded @PathParam("login") String login) {

		LOGGER.debug("Get dados usuario");

		getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());

		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(usuarioService.consultarDadosUsuario(login))).build();
    }

}
