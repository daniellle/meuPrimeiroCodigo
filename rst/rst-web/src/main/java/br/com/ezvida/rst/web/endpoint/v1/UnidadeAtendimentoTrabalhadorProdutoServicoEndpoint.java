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
import br.com.ezvida.rst.dao.filter.ProdutoServicoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhadorProdutoServico;
import br.com.ezvida.rst.service.UnidadeAtendimentoTrabalhadorProdutoServicoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/uat-produto-servico")
public class UnidadeAtendimentoTrabalhadorProdutoServicoEndpoint
		extends SegurancaEndpoint<UnidadeAtendimentoTrabalhadorProdutoServico> {

	private static final long serialVersionUID = -7236990223766722921L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UnidadeAtendimentoTrabalhadorProdutoServicoEndpoint.class);

	@Inject
	private UnidadeAtendimentoTrabalhadorProdutoServicoService uatProdutoServicoService;

	@GET
	@Encoded
	@Path("/pesquisa/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT_PRODUTOS_SERVICOS,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_CADASTRAR, PermissionConstants.CAT_PRODUTOS_SERVICOS_ALTERAR,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_CONSULTAR, PermissionConstants.CAT_PRODUTOS_SERVICOS_DESATIVAR }))
	public Response pesquisarProdutoServico(@BeanParam ProdutoServicoFilter filter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(uatProdutoServicoService.pesquisarProdutoServicoPorDepartamento(filter, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.CAT),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT_PRODUTOS_SERVICOS,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_CADASTRAR, PermissionConstants.CAT_PRODUTOS_SERVICOS_ALTERAR,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_CONSULTAR, PermissionConstants.CAT_PRODUTOS_SERVICOS_DESATIVAR }))
	public Response pesquisarAdicionados(@BeanParam ProdutoServicoFilter unidAtendTrabalhadorFilter,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Unidades Sesi produto e servico por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(uatProdutoServicoService.retornarPorUat(unidAtendTrabalhadorFilter, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.CAT))))
				.build();
	}

	@POST
	@Encoded
	@Path("/uat/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT_PRODUTOS_SERVICOS,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_CADASTRAR }))
	public Response salvar(@PathParam("id") Long id,
			@Encoded Set<UnidadeAtendimentoTrabalhadorProdutoServico> produtoServico, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Criando Produtos e Servicos");
		return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(uatProdutoServicoService.salvar(id, produtoServico, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.CAT))))
				.build();
	}

	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	 @Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT_PRODUTOS_SERVICOS,
			 PermissionConstants.CAT_PRODUTOS_SERVICOS_DESATIVAR }))
	public Response desativar(@Encoded UnidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(serializar(uatProdutoServicoService.desativarUatProdutoServico(uatProdutoServico, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.CAT))))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
