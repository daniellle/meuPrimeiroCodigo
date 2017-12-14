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
import br.com.ezvida.rst.dao.filter.DepartamentoRegionalFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.DepartamentoRegionalProdutoServico;
import br.com.ezvida.rst.service.DepartamentoRegionalProdutoServicoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/departamento-regional-produto-servico")
public class DepartamentoRegionalProdutoServicoEndpoint extends SegurancaEndpoint<DepartamentoRegionalProdutoServico> {

	private static final long serialVersionUID = -4539657833061606431L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoRegionalProdutoServicoEndpoint.class);
	
	@Inject
	private DepartamentoRegionalProdutoServicoService departamentoRegionalProdutoServicoService;
	
//	ClienteAuditoria auditoria = new ClienteAuditoria();
	
	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_ALTERAR, 
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CONSULTAR,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR }))
	public Response pesquisar(@BeanParam DepartamentoRegionalFilter departamentoRegionalFilter
			,@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando departamento regional produto e servico por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(departamentoRegionalProdutoServicoService.retornarPorDepartamentoRegional(departamentoRegionalFilter
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.DEPARTAMENTO_REGIONAL)))).build();
	}

	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR }))
	public Response desativar(@Encoded DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico
			,@Context SecurityContext context, @Context HttpServletRequest request) {		
		return Response.status(HttpServletResponse.SC_CREATED).entity(
				serializar(departamentoRegionalProdutoServicoService
						.desativarDepartamentoRegionalProdutoServico(departamentoRegionalProdutoServico
								,ClienteInfos.getClienteInfos(context, request,
										TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.DEPARTAMENTO_REGIONAL))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Encoded
	@Path("/departamentoregional/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
			PermissionConstants.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR }))
	public Response salvar(@PathParam("id") Long id,
			@Encoded Set<DepartamentoRegionalProdutoServico> produtoServico,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Criando Produtos e Servicos");
		return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(departamentoRegionalProdutoServicoService.salvar(id, produtoServico,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.DEPARTAMENTO_REGIONAL)))).build();
	}
		
}
