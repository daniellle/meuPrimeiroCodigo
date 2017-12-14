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

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.FuncaoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaFuncao;
import br.com.ezvida.rst.service.EmpresaFuncaoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-funcao")
public class EmpresaFuncaoEndpoint extends SegurancaEndpoint<EmpresaFuncao> {

	private static final long serialVersionUID = -1662742967545849288L;

	@Inject
	private EmpresaFuncaoService empresaFuncaoService;

	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_FUNCAO,
			PermissionConstants.EMPRESA_FUNCAO_DESATIVAR, }))
	public Response desativarEmpresaFuncao(@Encoded EmpresaFuncao empresaFuncao, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(serializar(empresaFuncaoService.desativar(empresaFuncao, ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_FUNCAO,
			PermissionConstants.EMPRESA_FUNCAO_CADASTRAR, PermissionConstants.EMPRESA_FUNCAO_ALTERAR,
			PermissionConstants.EMPRESA_FUNCAO_CONSULTAR, PermissionConstants.EMPRESA_FUNCAO_DESATIVAR }))
	public Response buscarEmpresaFuncao(@BeanParam FuncaoFilter funcaoFilter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(
						serializar(
								empresaFuncaoService.pesquisarPorPaginado(funcaoFilter,
										ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
												Funcionalidade.EMPRESA),
										ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Path("/empresa/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_FUNCAO,
			PermissionConstants.EMPRESA_FUNCAO_CADASTRAR }))
	public Response criarEmpresaFuncao(@PathParam("id") Long id, @Encoded Set<EmpresaFuncao> empresaFuncao,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(serializar(
						empresaFuncaoService.salvar(id,
								empresaFuncao, ClienteInfos.getClienteInfos(context, request,
										TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
