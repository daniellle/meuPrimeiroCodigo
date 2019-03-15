package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.girst.apiclient.model.SistemaCredenciado;
import br.com.ezvida.girst.apiclient.model.filter.SistemaCredenciadoFilter;
import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.service.SistemaCredenciadoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import br.com.ezvida.rst.web.util.Response;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/sistemascredenciados")
public class SistemaCredenciadoEndPoint extends SegurancaEndpoint<SistemaCredenciado> {

	private static final String CONTENT_VERSION = "Content-Version";

	private static final long serialVersionUID = 1L;
	
	@Inject
    SistemaCredenciadoService sistemaCredenciadoService;

    @POST
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.SISTEMA_CREDENCIADO_CADASTRAR}))
    public javax.ws.rs.core.Response cadastrar(
        @Context SecurityContext context,
        @Context HttpServletRequest request,
        @Encoded SistemaCredenciado sistemaCredenciado) {
        String mensagem = sistemaCredenciadoService.cadastrar(sistemaCredenciado, ClienteInfos.getDadosFilter(context), ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.SISTEMAS_CREDENCIADOS));
        return javax.ws.rs.core.Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(new Response<>(mensagem))).build();
    }

    @PUT
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.SISTEMA_CREDENCIADO_ALTERAR}))
    public javax.ws.rs.core.Response atualizar(
        @Context SecurityContext context,
        @Context HttpServletRequest request,
        @Encoded SistemaCredenciado sistemaCredenciado) {

        String mensagem = sistemaCredenciadoService.alterar(sistemaCredenciado, ClienteInfos.getDadosFilter(context), ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.SISTEMAS_CREDENCIADOS));
        return javax.ws.rs.core.Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(new Response<>(mensagem))).build();
    }

    @GET
    @Encoded
    @Path("/paginado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.SISTEMA_CREDENCIADO_PESQUISAR}))
    public javax.ws.rs.core.Response pesquisarPaginado(
        @Context SecurityContext context,
        @Context HttpServletRequest request,
        @QueryParam("cnpj") String cnpj,
        @QueryParam("nomeResponsavel") String nomeResponsavel,
        @QueryParam("sistema") String sistema,
        @QueryParam("bloqueado") Boolean bloqueado,
        @DefaultValue("1") @QueryParam("pagina") Integer pagina,
        @DefaultValue("20") @QueryParam("qtdRegistro") Integer quantidadeRegistro
    ) {
        SistemaCredenciadoFilter sistemaCredenciadoFilter = new SistemaCredenciadoFilter(cnpj, nomeResponsavel, sistema, bloqueado, pagina, quantidadeRegistro);
        ListaPaginada<SistemaCredenciado> mensagem = sistemaCredenciadoService.pesquisarPaginado(sistemaCredenciadoFilter, ClienteInfos.getDadosFilter(context), ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.SISTEMAS_CREDENCIADOS));
        return javax.ws.rs.core.Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(mensagem)).build();
    }

    @GET
    @Encoded
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.SISTEMA_CREDENCIADO_PESQUISAR}))
    public javax.ws.rs.core.Response pesquisarPorId(
        @PathParam("id") String id,
        @Context SecurityContext context,
        @Context HttpServletRequest request) {

        SistemaCredenciado sistemaCredenciado = sistemaCredenciadoService.findById(id, ClienteInfos.getDadosFilter(context), ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.SISTEMAS_CREDENCIADOS));
        return javax.ws.rs.core.Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(sistemaCredenciado)).build();
    }

    @PUT
    @Path("/ativardesativar")
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.SISTEMA_CREDENCIADO_ATIVAR_DESATIVAR}))
    public javax.ws.rs.core.Response ativarDesativar(
        @Context SecurityContext context,
        @Context HttpServletRequest request,
        @Encoded SistemaCredenciado sistemaCredenciado) {

        String mensagem = sistemaCredenciadoService.ativarDesativar(sistemaCredenciado, ClienteInfos.getDadosFilter(context), ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ATIVAR_DESATIVAR, Funcionalidade.SISTEMAS_CREDENCIADOS));
        return javax.ws.rs.core.Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(new Response<>(mensagem))).build();
    }
}
