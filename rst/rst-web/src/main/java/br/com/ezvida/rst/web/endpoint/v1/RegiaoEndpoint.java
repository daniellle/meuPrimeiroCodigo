package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Regiao;
import br.com.ezvida.rst.service.RegiaoService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/regiao")
public class RegiaoEndpoint extends SegurancaEndpoint<Regiao>{

	private static final long serialVersionUID = -2969024050860360412L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegiaoEndpoint.class);
	
	@Inject
	private RegiaoService regiaoService;
	
	@GET
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listar(){
		LOGGER.debug("Listando todas Regiões");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
                .entity(serializar(regiaoService.listarTodos())).build();
	}
}
