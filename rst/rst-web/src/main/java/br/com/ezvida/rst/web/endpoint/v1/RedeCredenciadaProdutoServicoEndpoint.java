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
import br.com.ezvida.rst.dao.filter.RedeCredenciadaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.RedeCredenciadaProdutoServico;

import br.com.ezvida.rst.service.RedeCredenciadaProdutoServicoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/rede-credenciada-produto-servico")
public class RedeCredenciadaProdutoServicoEndpoint extends SegurancaEndpoint<RedeCredenciadaProdutoServico> {

	private static final long serialVersionUID = -5489784600619320498L;
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(RedeCredenciadaProdutoServicoEndpoint.class);

	 @Inject
	private RedeCredenciadaProdutoServicoService redeCredenciadaProdutoServicoService;
	
	 @GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_CONSULTAR, 
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR }))
	public Response pesquisar(@BeanParam RedeCredenciadaFilter redeCredenciadaFilter, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Jornada por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(redeCredenciadaProdutoServicoService.retornarPorRedeCredenciada(redeCredenciadaFilter
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.PROFISSIONAL)))).build();
	}
	 
	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR }))
	public Response desativar(@Encoded RedeCredenciadaProdutoServico redeCredenciadaProdutoServico
			, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Salvando Empresa Setores ");
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(serializar(redeCredenciadaProdutoServicoService
						.desativarRedeCredenciadaProdutoServico(redeCredenciadaProdutoServico
								, ClienteInfos.getClienteInfos(context, request,
										TipoOperacaoAuditoria.DESATIVACAO,Funcionalidade.PROFISSIONAL))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Encoded
	@Path("/redecredenciada/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO,
			PermissionConstants.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR }))
	public Response salvar(@PathParam("id") Long id, @Encoded Set<RedeCredenciadaProdutoServico> produtoServico
			, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Criando Produtos Servicos");
		return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(redeCredenciadaProdutoServicoService.salvar(id, produtoServico
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.INCLUSAO,Funcionalidade.PROFISSIONAL)))).build();
	}
		

}
