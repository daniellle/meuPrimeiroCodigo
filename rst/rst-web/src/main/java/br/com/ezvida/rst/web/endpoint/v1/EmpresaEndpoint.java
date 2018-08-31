package br.com.ezvida.rst.web.endpoint.v1;

import java.util.Set;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaJornada;
import br.com.ezvida.rst.service.EmpresaJornadaService;
import br.com.ezvida.rst.service.EmpresaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresas")
public class EmpresaEndpoint extends SegurancaEndpoint<Empresa> {

	private static final long serialVersionUID = -5242832488603150874L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaEndpoint.class);

	@Inject
	private EmpresaService empresaService;

	@Inject
	private EmpresaJornadaService empresaJornadaService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("id") Long id, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Empresas por id");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaService.pesquisarPorId(id,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listarTodos(@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Listando todos Empresas");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaService.listarTodos())).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA,
			PermissionConstants.EMPRESA_CADASTRAR, PermissionConstants.EMPRESA_ALTERAR,
			PermissionConstants.EMPRESA_CONSULTAR, PermissionConstants.EMPRESA_DESATIVAR,
			PermissionConstants.EMPRESA_SINDICATO, PermissionConstants.EMPRESA_SINDICATO_CADASTRAR,
			PermissionConstants.EMPRESA_SINDICATO_ALTERAR, PermissionConstants.EMPRESA_SINDICATO_CONSULTAR,
			PermissionConstants.EMPRESA_SINDICATO_DESATIVAR, PermissionConstants.EMPRESA_JORNADA,
			PermissionConstants.EMPRESA_JORNADA_CADASTRAR, PermissionConstants.EMPRESA_JORNADA_ALTERAR,
			PermissionConstants.EMPRESA_JORNADA_CONSULTAR, PermissionConstants.EMPRESA_JORNADA_DESATIVAR,
			PermissionConstants.EMPRESA_CARGO, PermissionConstants.EMPRESA_CARGO_CADASTRAR,
			PermissionConstants.EMPRESA_CARGO_ALTERAR, PermissionConstants.EMPRESA_CARGO_CONSULTAR,
			PermissionConstants.EMPRESA_CARGO_DESATIVAR, PermissionConstants.EMPRESA_FUNCAO,
			PermissionConstants.EMPRESA_FUNCAO_CADASTRAR, PermissionConstants.EMPRESA_FUNCAO_ALTERAR,
			PermissionConstants.EMPRESA_FUNCAO_CONSULTAR, PermissionConstants.EMPRESA_FUNCAO_DESATIVAR,
			PermissionConstants.EMPRESA_SETOR, PermissionConstants.EMPRESA_SETOR_CADASTRAR,
			PermissionConstants.EMPRESA_SETOR_ALTERAR, PermissionConstants.EMPRESA_SETOR_CONSULTAR,
			PermissionConstants.EMPRESA_SETOR_DESATIVAR, PermissionConstants.EMPRESA_LOTACAO, 
			PermissionConstants.EMPRESA_LOTACAO_CADASTRAR, PermissionConstants.EMPRESA_LOTACAO_ALTERAR,
			PermissionConstants.EMPRESA_LOTACAO_CONSULTAR, PermissionConstants.EMPRESA_LOTACAO_DESATIVAR,
			PermissionConstants.EMPRESA_TRABALHADOR, PermissionConstants.EMPRESA_TRABALHADOR_CADASTRAR,
			PermissionConstants.EMPRESA_TRABALHADOR_ALTERAR, PermissionConstants.EMPRESA_TRABALHADOR_CONSULTAR,
			PermissionConstants.EMPRESA_TRABALHADOR_DESATIVAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO,
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam EmpresaFilter empresaFilter, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Empresa por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaService.pesquisarPaginado(empresaFilter,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}
	
	@GET
	@Path("/minha-empresa")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA,
			PermissionConstants.EMPRESA_CADASTRAR, PermissionConstants.EMPRESA_ALTERAR,
			PermissionConstants.EMPRESA_CONSULTAR, PermissionConstants.EMPRESA_DESATIVAR,
			PermissionConstants.EMPRESA_SINDICATO, PermissionConstants.EMPRESA_SINDICATO_CADASTRAR,
			PermissionConstants.EMPRESA_SINDICATO_ALTERAR, PermissionConstants.EMPRESA_SINDICATO_CONSULTAR,
			PermissionConstants.EMPRESA_SINDICATO_DESATIVAR, PermissionConstants.EMPRESA_JORNADA,
			PermissionConstants.EMPRESA_JORNADA_CADASTRAR, PermissionConstants.EMPRESA_JORNADA_ALTERAR,
			PermissionConstants.EMPRESA_JORNADA_CONSULTAR, PermissionConstants.EMPRESA_JORNADA_DESATIVAR,
			PermissionConstants.EMPRESA_CARGO, PermissionConstants.EMPRESA_CARGO_CADASTRAR,
			PermissionConstants.EMPRESA_CARGO_ALTERAR, PermissionConstants.EMPRESA_CARGO_CONSULTAR,
			PermissionConstants.EMPRESA_CARGO_DESATIVAR, PermissionConstants.EMPRESA_FUNCAO,
			PermissionConstants.EMPRESA_FUNCAO_CADASTRAR, PermissionConstants.EMPRESA_FUNCAO_ALTERAR,
			PermissionConstants.EMPRESA_FUNCAO_CONSULTAR, PermissionConstants.EMPRESA_FUNCAO_DESATIVAR,
			PermissionConstants.EMPRESA_SETOR, PermissionConstants.EMPRESA_SETOR_CADASTRAR,
			PermissionConstants.EMPRESA_SETOR_ALTERAR, PermissionConstants.EMPRESA_SETOR_CONSULTAR,
			PermissionConstants.EMPRESA_SETOR_DESATIVAR, PermissionConstants.EMPRESA_LOTACAO, 
			PermissionConstants.EMPRESA_LOTACAO_CADASTRAR, PermissionConstants.EMPRESA_LOTACAO_ALTERAR,
			PermissionConstants.EMPRESA_LOTACAO_CONSULTAR, PermissionConstants.EMPRESA_LOTACAO_DESATIVAR,
			PermissionConstants.EMPRESA_TRABALHADOR, PermissionConstants.EMPRESA_TRABALHADOR_CADASTRAR,
			PermissionConstants.EMPRESA_TRABALHADOR_ALTERAR, PermissionConstants.EMPRESA_TRABALHADOR_CONSULTAR,
			PermissionConstants.EMPRESA_TRABALHADOR_DESATIVAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO,
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR }))
	public Response pesquisarMinhaEmpresa(@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando por Minha Empresa");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaService.pesquisarMinhaEmpresa(ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA, PermissionConstants.EMPRESA_CADASTRAR }))
	public Response cadastrar(@Encoded Empresa empresa, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Cadastrando Empresa");
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(empresaService.salvar(empresa,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA, PermissionConstants.EMPRESA_ALTERAR }))
	public Response alterar(@Encoded Empresa empresa, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Alterando Empresa");
		return Response.status(HttpServletResponse.SC_OK)
				.entity(empresaService.salvar(empresa,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Encoded
	@Path("{id}/jornada")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_JORNADA, PermissionConstants.EMPRESA_JORNADA_CADASTRAR }))
	public Response criarEmpresaJornada(@PathParam("id") Long id, @Encoded Set<EmpresaJornada> emJonadas, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Criando Jornada");
		return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaJornadaService.salvar(id, emJonadas,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.EMPRESA))))
				.build();
	}

	@GET
	@Path("{id}/jornada")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarEmpresaFuncao(@PathParam("id") Long idEmpresa, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Funcoes por Empresas");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaJornadaService.pesquisarPorEmpresa(idEmpresa,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA))))
				.build();
	}
}
