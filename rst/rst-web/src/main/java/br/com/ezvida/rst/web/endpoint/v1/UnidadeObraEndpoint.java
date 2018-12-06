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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.ezvida.rst.service.UnidadeObraContratoUatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.UnidadeObra;
import br.com.ezvida.rst.service.UnidadeObraService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/unidades-obras")
public class UnidadeObraEndpoint extends SegurancaEndpoint<UnidadeObra> {
	private static final long serialVersionUID = 1745602896974569653L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeObraEndpoint.class);

	@Inject
	private UnidadeObraService unidadeObraService;

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listarTodos() {
		LOGGER.debug("Listando todas as Unidades Obra");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(unidadeObraService.listarTodos())).build();
	}


	@GET
	@Encoded
	@Path("/{idEmpresa}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response verificarDescricaoExistente(@Encoded @PathParam("idEmpresa") Long idEmpresa) {
		LOGGER.debug("Listando todas as Unidades Obra");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(unidadeObraService.buscarPorEmpresa(idEmpresa))).build();
	}

	@GET
    @Encoded
    @Path("/{idEmpresa}/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buscarPorNomeNaEmpresa(@Encoded @PathParam("idEmpresa") Long idEmpresa, @Encoded @PathParam("nome") String nome){
	    LOGGER.debug("Listando as unidades obras por nome");
	    return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(unidadeObraService.buscarPorEmpresaPorNome(idEmpresa, nome))).build();
    }

}
