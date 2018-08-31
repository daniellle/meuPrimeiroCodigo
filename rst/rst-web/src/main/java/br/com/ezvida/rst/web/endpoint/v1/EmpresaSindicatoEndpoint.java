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
import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaSindicato;
import br.com.ezvida.rst.service.EmpresaSindicatoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-sindicatos")
public class EmpresaSindicatoEndpoint extends SegurancaEndpoint<EmpresaSindicato> {

	private static final long serialVersionUID = 7827122267841619608L;

	@Inject
	private EmpresaSindicatoService empresaSindicatoService;

	@GET
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_SINDICATO,
			PermissionConstants.EMPRESA_SINDICATO_CADASTRAR, PermissionConstants.EMPRESA_SINDICATO_ALTERAR,
			PermissionConstants.EMPRESA_SINDICATO_CONSULTAR, PermissionConstants.EMPRESA_SINDICATO_DESATIVAR, }))
	public Response buscarEmpresaSindicato(@BeanParam EmpresaFilter empresaFilter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(
						serializar(
								empresaSindicatoService.pesquisarPorEmpresa(empresaFilter,
										ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
												Funcionalidade.EMPRESA),
										ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Path("{idEmpresa}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_SINDICATO,
			PermissionConstants.EMPRESA_SINDICATO_CADASTRAR }))
	public Response salvarEmpresaSindicato(@PathParam("idEmpresa") Long idEmpresa,
			@Encoded EmpresaSindicato empresaSindicato, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		TipoOperacaoAuditoria tipoOperacaoAuditoria = empresaSindicato.getId() == null ? TipoOperacaoAuditoria.INCLUSAO
				: TipoOperacaoAuditoria.ALTERACAO;
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(serializar(empresaSindicatoService.salvar(idEmpresa, empresaSindicato,
						ClienteInfos.getClienteInfos(context, request, tipoOperacaoAuditoria, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Path("{idEmpresa}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_SINDICATO,
			PermissionConstants.EMPRESA_SINDICATO_ALTERAR }))
	public Response alterarEmpresaSindicato(@PathParam("idEmpresa") Long idEmpresa,
			@Encoded EmpresaSindicato empresaSindicato, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(serializar(empresaSindicatoService.salvar(idEmpresa, empresaSindicato, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Path("/remover")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_SINDICATO,
			PermissionConstants.EMPRESA_SINDICATO_DESATIVAR }))
	public Response removerEmpresaSindicato(@Encoded EmpresaSindicato empresaSindicato,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(serializar(empresaSindicatoService.remover(empresaSindicato, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
