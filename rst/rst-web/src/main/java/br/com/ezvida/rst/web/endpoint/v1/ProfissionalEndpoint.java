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
import br.com.ezvida.rst.dao.filter.ProfissionalFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Profissional;
import br.com.ezvida.rst.service.ProfissionalService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;


@RequestScoped
@Path("/private/v1/profissionais")
public class ProfissionalEndpoint extends SegurancaEndpoint<Profissional> {

	private static final long serialVersionUID = 3052681376413750631L;
	
	@Inject
	private ProfissionalService profissionaisService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") String id, @Context SecurityContext context
			, @Context HttpServletRequest request ) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(profissionaisService.pesquisarPorId((Long.parseLong(id))
						,ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA
								,Funcionalidade.PROFISSIONAL)))).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PROFISSIONAL,
			PermissionConstants.PROFISSIONAL_CADASTRAR, PermissionConstants.PROFISSIONAL_ALTERAR, 
			PermissionConstants.PROFISSIONAL_CONSULTAR, PermissionConstants.PROFISSIONAL_DESATIVAR }))
	public Response pesquisar(@BeanParam ProfissionalFilter profissionalFilter, @Context SecurityContext context
			, @Context HttpServletRequest request) {	
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(profissionaisService.pesquisarPaginado(profissionalFilter
						, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PROFISSIONAL),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PROFISSIONAL,
			PermissionConstants.PROFISSIONAL_CADASTRAR }))
	public Response criar(@Encoded Profissional profissional, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(profissionaisService.salvar(profissional,
						ClienteInfos.getClienteInfos(context, request,TipoOperacaoAuditoria.INCLUSAO
								,Funcionalidade.PROFISSIONAL)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PROFISSIONAL, PermissionConstants.PROFISSIONAL_ALTERAR }))
	public Response alterar(@Encoded Profissional profissional, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(profissionaisService.salvar(profissional,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO
								,Funcionalidade.PROFISSIONAL)))
				.type(MediaType.APPLICATION_JSON).build();

	}
}
