package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.RedeCredenciadaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.RedeCredenciada;
import br.com.ezvida.rst.service.RedeCredenciadaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/redes-credenciadas")
public class RedeCredenciadaEndpoint extends SegurancaEndpoint<RedeCredenciada> {

	private static final long serialVersionUID = -472071417558030683L;
	
	@Inject
	private RedeCredenciadaService redeCredenciadaService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = {  PermissionConstants.REDE_CREDENCIADA,
			PermissionConstants.REDE_CREDENCIADA_CONSULTAR, PermissionConstants.REDE_CREDENCIADA_ALTERAR,
			PermissionConstants.REDE_CREDENCIADA_DESATIVAR, PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_CONSULTAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR}))
	public Response pesquisarPorId(@PathParam("id") Long id,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(redeCredenciadaService.pesquisarPorId(id
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.REDE_CREDENCIADA), ClienteInfos.getDadosFilter(context)))).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = {  PermissionConstants.REDE_CREDENCIADA, PermissionConstants.REDE_CREDENCIADA_CADASTRAR,
			PermissionConstants.REDE_CREDENCIADA_CONSULTAR, PermissionConstants.REDE_CREDENCIADA_ALTERAR,
			PermissionConstants.REDE_CREDENCIADA_DESATIVAR, PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_CONSULTAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR}))
	public Response pesquisar(@BeanParam RedeCredenciadaFilter redeCredenciadaFilter,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(redeCredenciadaService.pesquisarPaginado(redeCredenciadaFilter, 
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.REDE_CREDENCIADA),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.REDE_CREDENCIADA,
			PermissionConstants.REDE_CREDENCIADA_CADASTRAR }))
	public Response cadastrar(@Encoded RedeCredenciada redeCrendeciada,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).entity(redeCredenciadaService.salvar(redeCrendeciada
				, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO,Funcionalidade.REDE_CREDENCIADA))).type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.REDE_CREDENCIADA,
			PermissionConstants.REDE_CREDENCIADA_ALTERAR }))
	public Response alterar(@Encoded RedeCredenciada redeCredenciada,
			@Context SecurityContext context, @Context HttpServletRequest request) {		
		return Response.status(HttpServletResponse.SC_CREATED).entity(redeCredenciadaService.salvar(redeCredenciada
				, ClienteInfos.getClienteInfos(context, request
						,TipoOperacaoAuditoria.ALTERACAO,Funcionalidade.REDE_CREDENCIADA))).type(MediaType.APPLICATION_JSON).build();
	}
}