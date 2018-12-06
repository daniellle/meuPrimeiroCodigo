package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.model.TipoPrograma;
import br.com.ezvida.rst.service.TipoProgramaService;
import fw.web.endpoint.SegurancaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/private/v1/tipo-programa")
public class TipoProgramaEndpoint extends SegurancaEndpoint<TipoPrograma> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoProgramaEndpoint.class);

    @Inject
    private TipoProgramaService tipoProgramaService;

    @GET
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validarUnidadeObraPorEmpresa(){
        LOGGER.debug("Buscando todos os tipos de programa");
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(tipoProgramaService.buscarTodos())).build();
    }

}
