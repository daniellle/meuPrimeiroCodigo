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
import br.com.ezvida.rst.dao.filter.ParceiroFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Parceiro;
import br.com.ezvida.rst.service.ParceiroService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/parceiros-credenciados")
public class ParceiroEndpoint extends SegurancaEndpoint<Parceiro> {

	private static final long serialVersionUID = -472071417558030683L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroEndpoint.class);
	
	@Inject
	private ParceiroService parceiroService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") Long id, @Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(parceiroService.pesquisarPorId(id
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PARCEIRO_CREDENCIADO)))).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(
	        permissoes = @Permissao(value = { PermissionConstants.PARCEIRO_CREDENCIADO,
	        		PermissionConstants.PARCEIRO_CREDENCIADO_CADASTRAR, PermissionConstants.PARCEIRO_CREDENCIADO_ALTERAR, 
	        		PermissionConstants.PARCEIRO_CREDENCIADO_CONSULTAR, PermissionConstants.PARCEIRO_CREDENCIADO_DESATIVAR,
	        		PermissionConstants.PARCEIRO_PRODUTO_SERVICO, PermissionConstants.PARCEIRO_PRODUTO_SERVICO_CADASTRAR,
	        		PermissionConstants.PARCEIRO_PRODUTO_SERVICO_ALTERAR, PermissionConstants.PARCEIRO_PRODUTO_SERVICO_CONSULTAR,
	        		PermissionConstants.PARCEIRO_PRODUTO_SERVICO_DESATIVAR })
	    )
	public Response pesquisar(@BeanParam ParceiroFilter parceiroFilter,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Parceiro por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(parceiroService.pesquisarPaginado(parceiroFilter
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PARCEIRO_CREDENCIADO),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PARCEIRO_CREDENCIADO,
			PermissionConstants.PARCEIRO_CREDENCIADO_CADASTRAR }))
	public Response cadastrar(@Encoded Parceiro parceiro, 
			@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Cadastrando Parceiro");
		return Response.status(HttpServletResponse.SC_CREATED).entity(parceiroService.salvar(parceiro
				,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.PARCEIRO_CREDENCIADO))).type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PARCEIRO_CREDENCIADO,
			PermissionConstants.PARCEIRO_CREDENCIADO_ALTERAR }))
	public Response alterar(@Encoded Parceiro parceiro, 
			@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Alterando Parceiro");
		return Response.status(HttpServletResponse.SC_OK).entity(parceiroService.salvar(parceiro
				,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.PARCEIRO_CREDENCIADO))).type(MediaType.APPLICATION_JSON).build();
	}
}
