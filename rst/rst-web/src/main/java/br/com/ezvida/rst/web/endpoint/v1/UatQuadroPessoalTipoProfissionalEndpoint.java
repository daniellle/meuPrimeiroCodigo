package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatQuadroPessoalTipoProfissional;
import br.com.ezvida.rst.service.UatQuadroPessoalTipoProfissionalService;
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
@Path("/private/v1/uatquadropessoaltipoprofissional")
public class UatQuadroPessoalTipoProfissionalEndpoint extends SegurancaEndpoint<UatQuadroPessoalTipoProfissional> {

    private static final long serialVersionUID = -4984092281740135707L;

    @Inject
    private UatQuadroPessoalTipoProfissionalService uatQuadroPessoalTipoProfissionalService;

    @GET
    @Path("/servico/{idTipoServico}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAll(@PathParam("idTipoServico") Long idTipoServico, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(this.uatQuadroPessoalTipoProfissionalService.findByTipoServico(idTipoServico,
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.CONSULTA, Funcionalidade.GESTAO_UNIDADE_SESI),
                ClienteInfos.getDadosFilter(context)))).build();
    }
}
