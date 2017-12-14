package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.filter.SegmentoFilter;
import br.com.ezvida.rst.model.Segmento;
import br.com.ezvida.rst.service.SegmentoService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/segmentos")
public class SegmentosEndpoint extends SegurancaEndpoint<Segmento> {

	private static final long serialVersionUID = 322491951183030952L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentosEndpoint.class);
	
	@Inject
	private SegmentoService segmentoService;
	
	@GET
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listar(){
		LOGGER.debug("Listando todos Segmentos");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(segmentoService.pesquisarTodos())).build();
	}
	
	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pesquisar(@BeanParam SegmentoFilter segmentoFilter) {
		LOGGER.debug("Pesquisando Segmento por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(segmentoService.pesquisarPaginado(segmentoFilter))).build();
	}

}
