package br.com.ezvida.rst.web.endpoint.v1;

import java.util.List;
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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.UsuarioEntidadeFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UsuarioEntidade;
import br.com.ezvida.rst.service.UsuarioEntidadeService;
import br.com.ezvida.rst.service.UsuarioService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.core.exception.BusinessErrorException;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/usuario-entidade")
public class UsuarioEntidadeEndpoint extends SegurancaEndpoint<UsuarioEntidade> {

	private static final long serialVersionUID = 4346824422675010465L;

	@Inject
	private UsuarioEntidadeService usuarioEntidadeService;

	@Inject
	private UsuarioService usuarioService;

	@GET
	@Encoded
	@Path("/validarAcessoUsuario")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.USUARIO,
			PermissionConstants.USUARIO_ALTERAR, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR,
			PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR,
			PermissionConstants.USUARIO_ENTIDADE_ALTERAR, PermissionConstants.USUARIO_ENTIDADE_CONSULTAR,
			PermissionConstants.USUARIO_ENTIDADE_DESATIVAR }))
	public Response validarAcessoAoUsuario(@QueryParam("cpf") String cpf, @Context SecurityContext context, @Context HttpServletRequest request) {

		ClienteAuditoria auditoria = ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS);

		if (!usuarioService.isAdm(auditoria.getUsuario()) && usuarioService.isAdm(cpf)) {
			throw new BusinessErrorException(getMensagem("app_rst_usuario_acesso_negado"));
		}

		List<UsuarioEntidade> usuarioEntidade = usuarioEntidadeService.validarAcessoAoUsuario(cpf, ClienteInfos.getDadosFilter(context), auditoria);
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(usuarioEntidade)).build();
	}

	@GET
	@Encoded
	@Path("empresas/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.USUARIO_ENTIDADE,
			PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
			PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR }))
	public Response pesquisarEmpresas(@BeanParam UsuarioEntidadeFilter filtro, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(usuarioEntidadeService.pesquisarEmpresa(filtro, ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS))))
				.build();
	}

	@GET
	@Encoded
	@Path("sindicato/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.USUARIO_ENTIDADE,
			PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
			PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR }))
	public Response pesquisarSindicatos(@BeanParam UsuarioEntidadeFilter filtro, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(usuarioEntidadeService.pesquisarSindicato(filtro, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS))))
				.build();
	}

	@GET
	@Encoded
	@Path("departamento-regional/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.USUARIO_ENTIDADE,
			PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
			PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR }))
	public Response pesquisarDepartamentoRegional(@BeanParam UsuarioEntidadeFilter filtro,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(usuarioEntidadeService.pesquisarDepartamentoRegional(filtro, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS))))
				.build();
	}

	@POST
	@Encoded
	@Path("cadastrar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.USUARIO_ENTIDADE,
			PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, }))
	public Response salvar(@Encoded Set<UsuarioEntidade> usuarioEntidade, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(usuarioEntidadeService.salvar(usuarioEntidade, ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.USUARIOS))))
				.build();
	}

	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.USUARIO_ENTIDADE,
			PermissionConstants.USUARIO_ENTIDADE_DESATIVAR }))
	public Response desativar(@Encoded UsuarioEntidade UsuarioEntidade, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response
				.status(HttpServletResponse.SC_OK).entity(
						serializar(
								usuarioEntidadeService.desativarUsuarioEntidade(UsuarioEntidade,
										ClienteInfos.getClienteInfos(context, request,
												TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.USUARIOS))))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
