package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.girst.apiclient.model.Perfil;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.service.PerfilService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/perfis")
public class PerfilEndpoint extends SegurancaEndpoint<Perfil>{

    private static final long serialVersionUID = -3690891011960715212L;
    
    @Inject
    private PerfilService perfilService;
    
    @GET
    @Encoded
    @Path("/{nivel}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buscar(@PathParam("nivel") String nivel, @Context SecurityContext context, @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
                .header("Content-Version", getApplicationVersion())
                .entity(serializar(perfilService.buscarPerfis(nivel))).build();
    }
    
}
