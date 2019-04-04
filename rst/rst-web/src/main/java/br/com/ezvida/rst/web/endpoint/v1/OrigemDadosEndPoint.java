package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.model.OrigemDados;
import br.com.ezvida.rst.service.OrigemDadosService;
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
@Path("/private/v1/origemdados")
public class OrigemDadosEndPoint extends SegurancaEndpoint<OrigemDados> {

    private static final long serialVersionUID = 1216125933858808373L;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrigemDadosEndPoint.class);

    @Inject
    private OrigemDadosService origemDadosService;

    @GET
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listar(@Context SecurityContext context
        , @Context HttpServletRequest request) {
        LOGGER.debug("Listando origem de dados");
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(origemDadosService.listarTodos())).build();
    }


    @GET
    @Path("/origem-contrato")
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listarParaComboOrigemContrato(@Context SecurityContext context
        , @Context HttpServletRequest request) {
        LOGGER.debug("Listando origem de dados");
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(origemDadosService.findOrigemDadosToContrato())).build();
    }

}
