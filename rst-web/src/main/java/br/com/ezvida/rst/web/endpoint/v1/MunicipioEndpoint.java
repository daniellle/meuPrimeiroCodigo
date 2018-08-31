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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Municipio;
import br.com.ezvida.rst.service.MunicipioService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/municipios")
public class MunicipioEndpoint extends SegurancaEndpoint<Municipio> {

	private static final long serialVersionUID = -5010572914084566849L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioEndpoint.class);

	@Inject
	private MunicipioService municipioService;

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listar(@Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Listando todos Municipios");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(municipioService.listarTodos()))
				.build();
	}
	
	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response buscar(@PathParam("id") Long id,
			@Context SecurityContext context
			, @Context HttpServletRequest request){
		LOGGER.debug("Pesquisando Municipios por id");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(municipioService.pesquisarPorId(id)))
				.build();
	}
	
	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/estados/{idEstado}")
	public Response buscarPorEstado(@PathParam("idEstado") Long idEstado,
			@Context SecurityContext context
			, @Context HttpServletRequest request){
		LOGGER.debug("Pesquisando Municipios por id de Estado");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(municipioService.pesquisarPorIdEstado(idEstado)))
				.build();
	}

	@POST
	@Encoded
	@Path("/pesquisar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pesquisar(Municipio municipio,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Municipios por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(municipioService.pesquisar(municipio.getDescricao()))).build();
	}

}
