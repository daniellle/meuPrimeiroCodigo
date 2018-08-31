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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.SindicatoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Sindicato;
import br.com.ezvida.rst.service.SindicatoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/sindicatos")
public class SindicatoEndpoint extends SegurancaEndpoint<Sindicato> {

	private static final long serialVersionUID = -5242832488603150874L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SindicatoEndpoint.class);

	@Inject
	private SindicatoService sindicatoService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.SINDICATO,
			PermissionConstants.SINDICATO_ALTERAR, PermissionConstants.SINDICATO_CONSULTAR, 
			PermissionConstants.SINDICATO_DESATIVAR }))
	public Response buscar(@PathParam("id") String id, @Context SecurityContext context,
			@Context HttpServletRequest request ) {
		LOGGER.debug("Pesquisando Sindicatos por id");
		
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(sindicatoService.pesquisarPorId(Long.parseLong(id),
						ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.SINDICATO),	ClienteInfos.getDadosFilter(context)))).build();
	}
	
	
	@GET
	@Path("verificar/{cnpj}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.SINDICATO,
			PermissionConstants.SINDICATO_ALTERAR, PermissionConstants.SINDICATO_CONSULTAR,
			PermissionConstants.SINDICATO_DESATIVAR }))
	public Response buscarPorCnpj(@PathParam("cnpj") String cnpj, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Sindicatos por id");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(sindicatoService.pesquisarPorCNPJ(cnpj,
						ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.SINDICATO)))).build();
	}
	
	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.SINDICATO,
			PermissionConstants.SINDICATO_CADASTRAR, PermissionConstants.SINDICATO_ALTERAR,
			PermissionConstants.SINDICATO_CONSULTAR, PermissionConstants.SINDICATO_DESATIVAR }))
	public Response listarTodos() {
		LOGGER.debug("Listando todos Sindicatos");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(sindicatoService.listarTodos()))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.SINDICATO,
			PermissionConstants.SINDICATO_CADASTRAR, PermissionConstants.SINDICATO_ALTERAR,
			PermissionConstants.SINDICATO_CONSULTAR, PermissionConstants.SINDICATO_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam SindicatoFilter sindicatoFilter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Sindicatos por filtro");

	
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(sindicatoService.pesquisarPaginado(sindicatoFilter,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
								sindicatoFilter.isAplicarDadosFilter() ? Funcionalidade.SINDICATO : Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.SINDICATO, PermissionConstants.SINDICATO_CADASTRAR }))
	public Response cadastrar(@Encoded Sindicato sindicato, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Cadastrando Sindicato");
		return Response.status(HttpServletResponse.SC_CREATED).entity(sindicatoService.salvar(sindicato,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO,Funcionalidade.SINDICATO))).type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.SINDICATO, PermissionConstants.SINDICATO_ALTERAR }))
	public Response alterar(@Encoded Sindicato sindicato, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Alterando Sindicato");
		return Response.status(HttpServletResponse.SC_OK).entity(sindicatoService.salvar(sindicato,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO,Funcionalidade.SINDICATO))).type(MediaType.APPLICATION_JSON).build();
	}
}
