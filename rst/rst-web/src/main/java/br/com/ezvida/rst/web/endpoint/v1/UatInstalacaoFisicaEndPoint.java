package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatInstalacaoFisica;
import br.com.ezvida.rst.service.UatInstalacaoFisicaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.core.exception.BusinessException;
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
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequestScoped
@Path("/private/v1/uatinstalacaofisica")
public class UatInstalacaoFisicaEndPoint extends SegurancaEndpoint<UatInstalacaoFisica> {

    private static final long serialVersionUID = -1815482266860205291L;

    @Inject
    private UatInstalacaoFisicaService uatInstalacaoFisicaService;

    @POST
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response salvar(@Encoded List<UatInstalacaoFisica> listaInstalacoesFisicas, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK)
            .entity(this.uatInstalacaoFisicaService.salvar(listaInstalacoesFisicas,
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.GESTAO_UNIDADE_SESI),
                ClienteInfos.getDadosFilter(context))).build();
    }

    @GET
    @Path("/uatinstalacaofisica/{idUnidade}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByUnidade(@PathParam("idUnidade") Long idUnidade, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(this.uatInstalacaoFisicaService.findByUnidade(idUnidade,
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.CONSULTA, Funcionalidade.GESTAO_UNIDADE_SESI),
                ClienteInfos.getDadosFilter(context)))).build();
    }


    @PUT
    @Encoded
    @Path("/desativar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response desativar(@PathParam("id") Long id, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        try {
            this.uatInstalacaoFisicaService.desativar(id,
                ClienteInfos.getClienteInfos(context, request,
                    TipoOperacaoAuditoria.CONSULTA, Funcionalidade.GESTAO_UNIDADE_SESI),
                ClienteInfos.getDadosFilter(context));
            return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).entity(serializar(getMensagem("app_rst_instalacao_fisica_desativado_sucesso"))).build();
        } catch (BusinessException ex) {
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .header("Content-Version", getApplicationVersion()).entity(serializar(ex.getMessage())).build();
        } catch (Exception e) {
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .header("Content-Version", getApplicationVersion())
                .entity(serializar(getMensagem("app_validacao_error"))).build();
        }

    }

}
