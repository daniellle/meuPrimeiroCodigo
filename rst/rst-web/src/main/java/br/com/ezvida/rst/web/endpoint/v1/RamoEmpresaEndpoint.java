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
import br.com.ezvida.rst.service.RamoEmpresaService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/ramos-empresa")
public class RamoEmpresaEndpoint extends SegurancaEndpoint<Token> {

	private static final long serialVersionUID = -5220171879623359104L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RamoEmpresaEndpoint.class);

	@Inject
	private RamoEmpresaService ramoEmpresaService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarTodos(@Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Ramos Empresa");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(ramoEmpresaService.listarTodos()))
				.build();
	}

}
