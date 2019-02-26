package br.com.ezvida.rst.web.endpoint.v1;


import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatInstalacaoFisicaAmbiente;
import br.com.ezvida.rst.service.UatInstalacaoFisicaAmbienteService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
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
@Path("/private/v1/uatinstalacaofisicaambiente")
public class UatInstalacaoFisicaAmbienteEndPoint extends SegurancaEndpoint<UatInstalacaoFisicaAmbiente> {

    @Inject
    private UatInstalacaoFisicaAmbienteService uatInstalacaoFisicaAmbienteService;


    @GET
    @Encoded
    @Path("/categoria/{idCategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //TODO: ADICIONAR PERMISSOES
    public Response findByCategoria(@PathParam("idCategoria") Long idCategoria, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(uatInstalacaoFisicaAmbienteService.findByCategoria(idCategoria,
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.CONSULTA, Funcionalidade.CAT),
                ClienteInfos.getDadosFilter(context)))).build();
    }

}
