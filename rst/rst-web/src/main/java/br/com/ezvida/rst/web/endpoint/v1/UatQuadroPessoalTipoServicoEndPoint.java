package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatQuadroPessoalTipoServico;
import br.com.ezvida.rst.service.UatQuadroPessoalTipoServicoService;
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
@Path("/private/v1/uatquadropessoaltiposervico")
public class UatQuadroPessoalTipoServicoEndPoint extends SegurancaEndpoint<UatQuadroPessoalTipoServico> {
    private static final long serialVersionUID = 1150219652137694653L;

    @Inject
    private UatQuadroPessoalTipoServicoService uatQuadroPessoalTipoServicoService;

    @GET
    @Path("/area/{idArea}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findByArea(@PathParam("idArea") Long idArea, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(this.uatQuadroPessoalTipoServicoService.findByArea(idArea,
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.CONSULTA, Funcionalidade.GESTAO_UNIDADE_SESI),
                ClienteInfos.getDadosFilter(context)))).build();
    }
}
