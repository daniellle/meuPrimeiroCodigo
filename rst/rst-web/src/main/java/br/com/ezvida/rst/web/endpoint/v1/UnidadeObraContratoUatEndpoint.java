package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.UnidadeObraContratoUatFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UnidadeObraContratoUat;
import br.com.ezvida.rst.service.UnidadeObraContratoUatService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import com.google.common.base.Charsets;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Permission;

@RequestScoped
@Path("/private/v1/unidades-obras-contratos-uat")
public class UnidadeObraContratoUatEndpoint extends SegurancaEndpoint<UnidadeObraContratoUat> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeObraContratoUatEndpoint.class);

    @Inject
    private UnidadeObraContratoUatService unidadeObraContratoUatService;

    @GET
    @Encoded
    @Path("/validar-unidades/{cnpj}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validarUnidadeObraPorEmpresa(@Encoded @PathParam("cnpj") String cnpj){
        LOGGER.debug("Validando a Unidade de Obra");
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(unidadeObraContratoUatService.validarPorEmpresa(cnpj))).build();
    }

    @GET
    @Encoded
    @Path("/contratos/{empresaId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {
        PermissionConstants.EMPRESA, PermissionConstants.EMPRESA_CONTRATO,
        PermissionConstants.EMPRESA_CONTRATO_CADASTRAR
    }))
    public Response pesquisarPaginado(@BeanParam UnidadeObraContratoUatFilter unidadeObraContratoUatFilter,
                                      @Context SecurityContext context, @Context HttpServletRequest request, @Encoded @PathParam("empresaId") Long empresaId){
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion()).entity(
                serializar(
                    unidadeObraContratoUatService.pesquisarPaginado(unidadeObraContratoUatFilter, ClienteInfos.getClienteInfos(context, request,
                        TipoOperacaoAuditoria.CONSULTA,
                        Funcionalidade.EMPRESA), empresaId)))
                    .build();
    }

    @POST
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response salvar(@Encoded UnidadeObraContratoUat unidadeObraContratoUat,  @Context SecurityContext context
        , @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK)
            .entity(unidadeObraContratoUatService.salvar(unidadeObraContratoUat))
            .type(MediaType.APPLICATION_JSON).build();

    }

    @PUT
    @Encoded
    @Path("/ativar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ativar(@Encoded UnidadeObraContratoUat unidadeObraContratoUat,  @Context SecurityContext context
        , @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK)
            .entity(unidadeObraContratoUatService.ativar(unidadeObraContratoUat))
            .type(MediaType.APPLICATION_JSON).build();

    }

    @PUT
    @Encoded
    @Path("/desativar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response desativar(@Encoded UnidadeObraContratoUat unidadeObraContratoUat,  @Context SecurityContext context
        , @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK)
            .entity(unidadeObraContratoUatService.desativar(unidadeObraContratoUat))
            .type(MediaType.APPLICATION_JSON).build();

    }

}
