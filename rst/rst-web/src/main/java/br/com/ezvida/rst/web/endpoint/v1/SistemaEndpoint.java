package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.girst.apiclient.model.Sistema;
import br.com.ezvida.rst.service.SistemaService;
import fw.web.endpoint.SegurancaEndpoint;

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
@Path("/private/v1/sistemas")
public class SistemaEndpoint extends SegurancaEndpoint<Sistema> {

    private static final long serialVersionUID = -582083669190747286L;

    @Inject
    private SistemaService sistemaService;

    @GET
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@Context SecurityContext context,
                           @Context HttpServletRequest request,
                           @QueryParam("login") String login) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
                       .header("Content-Version", getApplicationVersion())
                       .entity(serializar(sistemaService.buscarSistemasPermitidos(login))).build();
    }

}
