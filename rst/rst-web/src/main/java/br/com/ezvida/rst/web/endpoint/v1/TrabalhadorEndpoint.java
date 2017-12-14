package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.model.PrimeiroAcesso;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.service.TrabalhadorService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/public/v1/trabalhador")
public class TrabalhadorEndpoint extends SegurancaEndpoint<Trabalhador> {

	private static final long serialVersionUID = 3273560908867006426L;

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

}
