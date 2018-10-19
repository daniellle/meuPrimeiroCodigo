package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.girst.apiclient.model.SistemaCredenciado;
import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.service.SistemaCredenciadoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.core.exception.BusinessException;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
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
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/private/v1/sistemascredenciados")
public class SistemaCredenciadoEndPoint extends SegurancaEndpoint<SistemaCredenciado> {

    @Inject
    SistemaCredenciadoService sistemaCredenciadoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SistemaCredenciadoEndPoint.class);

    @POST
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.SISTEMA_CREDENCIADO_CADASTRAR}))
    public javax.ws.rs.core.Response cadastrar(
        @Context SecurityContext context,
        @Context HttpServletRequest request,
        @Encoded SistemaCredenciado sistemaCredenciado) {
        try {
            String mensagem = sistemaCredenciadoService.cadastrar(sistemaCredenciado, ClienteInfos.getDadosFilter(context), ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.SISTEMAS_CREDENCIADOS));
           return javax.ws.rs.core.Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
                .header("Content-Version", getApplicationVersion())
                .entity(serializar(mensagem)).build();
        } catch (BusinessException e) {
            return javax.ws.rs.core.Response.status(HttpServletResponse.SC_BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .header("Content-Version", getApplicationVersion())
                .entity(serializar(e.getMessage())).build();
        } catch (Exception e) {
            return javax.ws.rs.core.Response.status(HttpServletResponse.SC_BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .header("Content-Version", getApplicationVersion())
                .entity(serializar(getMensagem("app_validacao_error"))).build();
        }
    }
}
