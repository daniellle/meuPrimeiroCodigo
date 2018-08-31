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
import br.com.ezvida.rst.dao.filter.ParceiroFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.ParceiroProdutoServico;
import br.com.ezvida.rst.service.ParceiroProdutoServicoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/parceiro-produto-servico")
public class ParceiroProdutoServicoEndpoint extends SegurancaEndpoint<ParceiroProdutoServico> {

	private static final long serialVersionUID = -8030703985457293628L;
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroProdutoServicoEndpoint.class);

	 @Inject
	private ParceiroProdutoServicoService parceiroProdutoServicoService;
	
	 @GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PARCEIRO_PRODUTO_SERVICO,
			PermissionConstants.PARCEIRO_PRODUTO_SERVICO_CADASTRAR, PermissionConstants.PARCEIRO_PRODUTO_SERVICO_ALTERAR, 
			PermissionConstants.PARCEIRO_PRODUTO_SERVICO_CONSULTAR, PermissionConstants.PARCEIRO_PRODUTO_SERVICO_DESATIVAR }))
	public Response pesquisar(@BeanParam ParceiroFilter parceiroFilter, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Produto por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(parceiroProdutoServicoService.retornarPorParceiroProdutoServico(parceiroFilter
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PARCEIRO_CREDENCIADO)))).build();
	}
	
	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PARCEIRO_PRODUTO_SERVICO,
			PermissionConstants.PARCEIRO_PRODUTO_SERVICO_DESATIVAR }))
	public Response desativar(@Encoded ParceiroProdutoServico parceiroProdutoServico
			, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Salvando Parceiro Produto ");
		return Response.status(HttpServletResponse.SC_OK).entity(serializar(parceiroProdutoServicoService
				.desativarParceiroProdutoServico(parceiroProdutoServico
						,ClienteInfos.getClienteInfos(context, request,TipoOperacaoAuditoria.CONSULTA
								, Funcionalidade.PARCEIRO_CREDENCIADO))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Encoded
	@Path("/redecredenciada/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PARCEIRO_PRODUTO_SERVICO,
			PermissionConstants.PARCEIRO_PRODUTO_SERVICO_CADASTRAR }))
	public Response criarEmpresaCbo(@PathParam("id") Long id, @Encoded Set<ParceiroProdutoServico> produtoServico
			, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Criando Produtos Servicos");
		return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(parceiroProdutoServicoService.salvar(id, produtoServico
						,ClienteInfos.getClienteInfos(context, request,TipoOperacaoAuditoria.CONSULTA
								, Funcionalidade.PARCEIRO_CREDENCIADO)))).build();
	}
		
	
}
