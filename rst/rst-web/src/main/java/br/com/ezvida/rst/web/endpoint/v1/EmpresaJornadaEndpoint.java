package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
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
import br.com.ezvida.rst.model.EmpresaJornada;
import br.com.ezvida.rst.service.EmpresaJornadaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-jornada")
public class EmpresaJornadaEndpoint extends SegurancaEndpoint<EmpresaJornada> {

	private static final long serialVersionUID = 1182987470855502261L;
	
	@Inject
	private EmpresaJornadaService empresaJornadaService;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") Long id,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaJornadaService.pesquisarPorId(id, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA)))).build();
	}
	
	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_JORNADA,
			PermissionConstants.EMPRESA_JORNADA_DESATIVAR, PermissionConstants.EMPRESA_JORNADA_ALTERAR, 
			PermissionConstants.EMPRESA_JORNADA_CADASTRAR, PermissionConstants.EMPRESA_JORNADA_CONSULTAR }))
	public Response pesquisar(@BeanParam EmpresaFilter empresaFilter,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaJornadaService.retornarPorEmpresa(empresaFilter,
						ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context)))).build();
	}
	
	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_JORNADA,
			PermissionConstants.EMPRESA_JORNADA_DESATIVAR }))
	public Response desativar(@Encoded EmpresaJornada empresaJornada,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).entity(serializar(empresaJornadaService.desativarEmpresaJornada(empresaJornada,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}
	

}
