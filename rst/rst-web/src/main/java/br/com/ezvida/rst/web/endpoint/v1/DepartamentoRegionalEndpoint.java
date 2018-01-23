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
import br.com.ezvida.rst.dao.filter.DepartamentoRegionalFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.service.DepartamentoRegionalService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/departamentos-regionais")
public class DepartamentoRegionalEndpoint extends SegurancaEndpoint<DepartamentoRegional> {

	private static final long serialVersionUID = -4410708943008367355L;
	
	@Inject
	private DepartamentoRegionalService departamentoRegionalService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("id") String id, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(departamentoRegionalService.pesquisarPorId(Long.parseLong(id)
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.DEPARTAMENTO_REGIONAL))))
				.build();
	}

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listarTodos(@Context SecurityContext context
			, @Context HttpServletRequest request, @BeanParam DepartamentoRegionalFilter departamentoRegionalFilter) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version"
				, getApplicationVersion())
				.entity(serializar(departamentoRegionalService.listarTodos(Situacao.TODOS, 
						ClienteInfos.getDadosFilter(context), departamentoRegionalFilter))).build();
	}

	@GET
	@Encoded
	@Path("/ativos")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listarTodosAtivos(@Context SecurityContext context
			, @Context HttpServletRequest request, @BeanParam DepartamentoRegionalFilter departamentoRegionalFilter) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(departamentoRegionalService.listarTodos(Situacao.ATIVO,
						ClienteInfos.getDadosFilter(context),departamentoRegionalFilter))).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.DEPARTAMENTO_REGIONAL,
			PermissionConstants.DEPARTAMENTO_REGIONAL_CADASTRAR, PermissionConstants.DEPARTAMENTO_REGIONAL_ALTERAR, 
			PermissionConstants.DEPARTAMENTO_REGIONAL_CONSULTAR, PermissionConstants.DEPARTAMENTO_REGIONAL_DESATIVAR,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_ALTERAR,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CONSULTAR,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam DepartamentoRegionalFilter departamentoRegionalFilter
			, @Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(departamentoRegionalService.pesquisarPaginado(departamentoRegionalFilter
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.DEPARTAMENTO_REGIONAL), ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.DEPARTAMENTO_REGIONAL,
			PermissionConstants.DEPARTAMENTO_REGIONAL_CADASTRAR }))
	public Response cadastrar(@Encoded DepartamentoRegional departamentoRegional, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).entity(departamentoRegionalService
				.salvar(departamentoRegional,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.DEPARTAMENTO_REGIONAL)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.DEPARTAMENTO_REGIONAL,
			PermissionConstants.DEPARTAMENTO_REGIONAL_ALTERAR }))
	public Response alterar(@Encoded DepartamentoRegional departamentoRegional, @Context SecurityContext context
			, @Context HttpServletRequest request) {		
		return Response.status(HttpServletResponse.SC_OK).entity(departamentoRegionalService
				.salvar(departamentoRegional,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.DEPARTAMENTO_REGIONAL)))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
