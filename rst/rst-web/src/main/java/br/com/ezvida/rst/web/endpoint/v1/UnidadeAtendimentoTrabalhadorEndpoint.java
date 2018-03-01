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
import br.com.ezvida.rst.dao.filter.EnderecoFilter;
import br.com.ezvida.rst.dao.filter.UnidAtendTrabalhadorFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.service.UnidadeAtendimentoTrabalhadorService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/uats")
public class UnidadeAtendimentoTrabalhadorEndpoint extends SegurancaEndpoint<UnidadeAtendimentoTrabalhador> {

	private static final long serialVersionUID = 2801098260977143337L;


	@Inject
	private UnidadeAtendimentoTrabalhadorService unidAtendTrabalhadorService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("id") String id, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(unidAtendTrabalhadorService.pesquisarPorId(Long.parseLong(id),
						ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.CAT),
							ClienteInfos.getDadosFilter(context)))).build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT, PermissionConstants.CAT_CADASTRAR }))
	public Response cadastrar(@Encoded UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(unidAtendTrabalhadorService.salvar(unidadeAtendimentoTrabalhador,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.CAT),
						ClienteInfos.getDadosFilter(context)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listarTodos(@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(unidAtendTrabalhadorService
						.pesquisarTodos(ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.CAT)))).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT, PermissionConstants.CAT_CADASTRAR,
			PermissionConstants.CAT_ALTERAR, PermissionConstants.CAT_CONSULTAR, PermissionConstants.CAT_DESATIVAR,
			PermissionConstants.CAT_PRODUTOS_SERVICOS, PermissionConstants.CAT_PRODUTOS_SERVICOS_CADASTRAR,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_ALTERAR, PermissionConstants.CAT_PRODUTOS_SERVICOS_CONSULTAR,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_DESATIVAR}))
	public Response pesquisarPaginado(@BeanParam UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter, 
			@Context SecurityContext context, @Context HttpServletRequest request) {

		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(unidAtendTrabalhadorService.pesquisaPaginada(unidAtendTrabalhadorFilter,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.CAT),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = {  PermissionConstants.CAT, PermissionConstants.CAT_ALTERAR }))
	public Response alterar(@Encoded UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador
			,@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(unidAtendTrabalhadorService.salvar(unidadeAtendimentoTrabalhador,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.CAT),
						ClienteInfos.getDadosFilter(context)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Encoded
	@Path("/endereco")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response pesquisarPorEndereco(@BeanParam EnderecoFilter enderecoFilter, 
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(unidAtendTrabalhadorService.pesquisarPorEndereco(enderecoFilter,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.CAT),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

}
