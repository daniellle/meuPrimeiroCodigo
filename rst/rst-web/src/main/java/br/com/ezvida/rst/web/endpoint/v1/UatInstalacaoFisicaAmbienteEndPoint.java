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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatInstalacaoFisicaAmbiente;
import br.com.ezvida.rst.service.UatInstalacaoFisicaAmbienteService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/uatinstalacaofisicaambiente")
public class UatInstalacaoFisicaAmbienteEndPoint extends SegurancaEndpoint<UatInstalacaoFisicaAmbiente> {

	private static final long serialVersionUID = 1L;
	
	@Inject
    private UatInstalacaoFisicaAmbienteService uatInstalacaoFisicaAmbienteService;


    @GET
    @Encoded
    @Path("/categoria/{idCategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findByCategoria(@PathParam("idCategoria") Long idCategoria, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(uatInstalacaoFisicaAmbienteService.findByCategoria(idCategoria,
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.CONSULTA, Funcionalidade.GESTAO_UNIDADE_SESI),
                ClienteInfos.getDadosFilter(context)))).build();
    }
}
