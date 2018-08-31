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
import br.com.ezvida.rst.dao.filter.ProdutoServicoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.ProdutoServico;
import br.com.ezvida.rst.service.ProdutoServicoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/produtos-servicos")
public class ProdutoServicoEndpoint extends SegurancaEndpoint<ProdutoServico> {

	private static final long serialVersionUID = -6240352292541016175L;

	@Inject
	private ProdutoServicoService produtoServicoService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PRODUTO_SERVICO,
			PermissionConstants.PRODUTO_SERVICO_ALTERAR, PermissionConstants.PRODUTO_SERVICO_DESATIVAR,
			PermissionConstants.PRODUTO_SERVICO_CONSULTAR }))
	public Response buscarPorId(@PathParam("id") long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(
						produtoServicoService.buscarPorId(id,
								ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
										Funcionalidade.PRODUTOS_SERVICOS),
								ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PRODUTO_SERVICO,
			PermissionConstants.PRODUTO_SERVICO_CADASTRAR, PermissionConstants.PRODUTO_SERVICO_ALTERAR,
			PermissionConstants.PRODUTO_SERVICO_DESATIVAR, PermissionConstants.PRODUTO_SERVICO_CONSULTAR }))
	public Response listarTodos(@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(produtoServicoService.listarTodos())).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PRODUTO_SERVICO,
			PermissionConstants.PRODUTO_SERVICO_CADASTRAR, PermissionConstants.PRODUTO_SERVICO_ALTERAR,
			PermissionConstants.PRODUTO_SERVICO_CONSULTAR, PermissionConstants.PRODUTO_SERVICO_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam ProdutoServicoFilter filter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(
						produtoServicoService.pesquisarPaginado(filter,
								ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
										Funcionalidade.PRODUTOS_SERVICOS),
								ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PRODUTO_SERVICO,
			PermissionConstants.PRODUTO_SERVICO_CADASTRAR }))
	public Response criar(@Encoded ProdutoServico produtoServico, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(produtoServicoService.salvar(produtoServico, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.PRODUTOS_SERVICOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PRODUTO_SERVICO,
			PermissionConstants.PRODUTO_SERVICO_ALTERAR }))
	public Response alterar(@Encoded ProdutoServico produtoServico, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(produtoServicoService.salvar(produtoServico, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.PRODUTOS_SERVICOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Encoded
	@Path("/todos")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response pesquisarSemPaginacao(@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(produtoServicoService.pesquisarSemPaginacao(ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PESQUISA_SESI),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}
	
	@GET
	@Encoded
	@Path("/unidade-sesi/{ids}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response buscarProdutosPorIdUat(@PathParam("ids") String ids, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(produtoServicoService.buscarProdutosPorIdUat(ids, ClienteInfos.getDadosFilter(context))))
				.build();
	}

}
