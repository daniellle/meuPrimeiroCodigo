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
import br.com.ezvida.rst.dao.filter.SetorFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaSetor;
import br.com.ezvida.rst.service.EmpresaSetorService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-setor")
public class EmpresaSetorEndpoint extends SegurancaEndpoint<EmpresaSetor> {

	private static final long serialVersionUID = 1182987470855502261L;

	@Inject
	private EmpresaSetorService empresaSetorService;
	
	@GET
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_SETOR,
			PermissionConstants.EMPRESA_SETOR_CADASTRAR, PermissionConstants.EMPRESA_SETOR_ALTERAR, 
			PermissionConstants.EMPRESA_SETOR_CONSULTAR, PermissionConstants.EMPRESA_SETOR_DESATIVAR }))
	public Response pesquisarPorPaginado(@BeanParam SetorFilter setorFilter,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaSetorService.pesquisarPorPaginado(setorFilter
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA), ClienteInfos.getDadosFilter(context)))).build();
	}

	@POST
	@Encoded
	@Path("/empresa/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_SETOR,
			PermissionConstants.EMPRESA_SETOR_CADASTRAR }))
	public Response criarEmpresaSetor(@PathParam("id") Long id, @Encoded Set<EmpresaSetor> empresaSetor,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).entity(serializar(empresaSetorService.salvar(id, empresaSetor,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_SETOR,
			PermissionConstants.EMPRESA_SETOR_DESATIVAR }))
	public Response desativarEmpresaSetor(@Encoded EmpresaSetor empresaSetor,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).entity(serializar(empresaSetorService.desativar(empresaSetor,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
