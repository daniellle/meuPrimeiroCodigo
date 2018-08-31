package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Token;
import br.com.ezvida.rst.service.PorteEmpresaService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/portes-empresas")
public class PorteEmpresaEndpoint  extends SegurancaEndpoint<Token> {

	private static final long serialVersionUID = 2315055955185142072L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PorteEmpresaEndpoint.class);

	@Inject
	private PorteEmpresaService porteEmpresaService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarTodos(@Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Portes Empresa");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(porteEmpresaService.listarTodos()))
				.build();
	}
}
