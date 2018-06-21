package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.model.PrimeiroAcesso;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.service.TrabalhadorService;
import com.google.common.base.Charsets;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/public/v1/trabalhador")
public class TrabalhadorEndpoint extends SegurancaEndpoint<Trabalhador> {

	private static final long serialVersionUID = 3273560908867006426L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorEndpoint.class);

	@Inject
	private TrabalhadorService trabalhadorService;

	@GET
	@Encoded
	@Path("/primeiro-acesso")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buscarTrabalhadorPrimeiroAcesso(@QueryParam("cpf") String cpf, @QueryParam("dataNascimento") String dataNascimento) {

		Trabalhador trabalhador = trabalhadorService.buscarTrabalhadorPrimeiroAcesso(cpf, dataNascimento);

		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(trabalhador)).build();
	}

	@POST
	@Encoded
	@Path("/primeiro-acesso")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvarPrimeiroAcesso(@Encoded PrimeiroAcesso primeiroAcesso, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(trabalhadorService.salvarPrimeiroAcesso(primeiroAcesso))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Encoded
	@Path("/solicitar-email")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response solicitarEmailSesi(@Encoded PrimeiroAcesso solicitacaoEmail,  @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(trabalhadorService.solicitarEmailSesi(solicitacaoEmail))
				.type(MediaType.APPLICATION_JSON).build();

	}

    //@formatter:off
    @GET
    @Encoded
    @Path("/usuario/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.TRABALHADOR}))
    public Response getTrabalhadoresByUsuario(@PathParam("login") String login, @QueryParam("nome") String nome, @QueryParam("cpf") String cpf, @QueryParam("page") String page,
                                              @Context SecurityContext context, @Context HttpServletRequest request) {
        LOGGER.debug("Get trabalhador por login de usuario");
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion()).entity(serializar(
            trabalhadorService.buscarTrabalhadorByUsuario(login, nome, cpf, page))).build();
    }

}
