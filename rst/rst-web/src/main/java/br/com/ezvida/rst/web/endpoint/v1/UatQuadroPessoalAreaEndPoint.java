package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatQuadroPessoalArea;
import br.com.ezvida.rst.service.UatQuadroPessoalAreaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.web.endpoint.SegurancaEndpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/private/v1/uatquadropessoalarea")
public class UatQuadroPessoalAreaEndPoint extends SegurancaEndpoint<UatQuadroPessoalArea> {

    @Inject
    private UatQuadroPessoalAreaService uatQuadroPessoalAreaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@Context SecurityContext context
        , @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(uatQuadroPessoalAreaService.listarTodos(
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.CONSULTA, Funcionalidade.GESTAO_UNIDADE_SESI),
                ClienteInfos.getDadosFilter(context)))).build();
    }
}
