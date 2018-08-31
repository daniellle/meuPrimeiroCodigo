package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Pais;
import br.com.ezvida.rst.service.PaisService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/pais")
public class PaisEndpoint extends SegurancaEndpoint<Pais>{

	private static final long serialVersionUID = -1014447030496321428L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PaisEndpoint.class);
	
	@Inject
    private PaisService paisService;
	
	@GET
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listar(@Context SecurityContext context
			, @Context HttpServletRequest request){
		LOGGER.debug("Listando todos Paises");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
                .entity(serializar(paisService.listarTodos())).build();
	}
	
}
