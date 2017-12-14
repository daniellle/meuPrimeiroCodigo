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
import br.com.ezvida.rst.dao.filter.CboFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaCbo;
import br.com.ezvida.rst.service.EmpresaCboService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-cbo")
public class EmpresaCboEndpoint extends SegurancaEndpoint<EmpresaCbo> {

	private static final long serialVersionUID = -9194596409566860486L;

	

	@Inject
	private EmpresaCboService empresaCboService;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") Long id,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaCboService.pesquisarPorId(id
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA)))).build();
	}
	
	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_CARGO,
			PermissionConstants.EMPRESA_CARGO_CADASTRAR, PermissionConstants.EMPRESA_CARGO_ALTERAR,
			PermissionConstants.EMPRESA_CARGO_CONSULTAR, PermissionConstants.EMPRESA_CARGO_DESATIVAR, }))
	public Response pesquisar(@BeanParam CboFilter cboFilter,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaCboService.retornarPorPaginado(cboFilter,
						ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA),ClienteInfos.getDadosFilter(context)))).build();
	}


	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_CARGO,
			PermissionConstants.EMPRESA_CARGO_DESATIVAR }))
	public Response desativar(@Encoded EmpresaCbo empresaCbo, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).entity(serializar(empresaCboService.desativarEmpresaCbo(empresaCbo,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Encoded
	@Path("/empresa/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_CARGO, PermissionConstants.EMPRESA_CARGO_CADASTRAR }))
	public Response criarEmpresaCbo(@PathParam("id") Long id, @Encoded Set<EmpresaCbo> emCbos, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaCboService.salvar(id, emCbos,
						ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.EMPRESA)))).build();
	}
}
