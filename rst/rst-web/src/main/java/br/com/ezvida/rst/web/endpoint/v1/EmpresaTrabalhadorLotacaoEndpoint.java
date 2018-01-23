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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.EmpresaTrabalhadorLotacaoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaTrabalhadorLotacao;
import br.com.ezvida.rst.service.EmpresaTrabalhadorLotacaoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-trabalhador-lotacoes")
public class EmpresaTrabalhadorLotacaoEndpoint extends SegurancaEndpoint<EmpresaTrabalhadorLotacao> {

	private static final long serialVersionUID = -6282406320669919507L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorLotacaoEndpoint.class);

	@Inject
	private EmpresaTrabalhadorLotacaoService empresaTrabalhadorLotacaoService;

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO,
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR, 
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR, PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam EmpresaTrabalhadorLotacaoFilter empresaTrabalhadorLotacaoFilter,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Listando EmpresaTrabalhadorLotacaoService paginado");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaTrabalhadorLotacaoService.pesquisarPaginado(empresaTrabalhadorLotacaoFilter,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
								Funcionalidade.EMPRESA))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO,
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR }))
	public Response salvar(@Encoded EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		if(empresaTrabalhadorLotacao.getDataDesligamento()!= null) {
			LOGGER.debug("Alteração EmpresaTrabalhadorLotacao ");
			return Response.status(HttpServletResponse.SC_CREATED)
					.entity(serializar(empresaTrabalhadorLotacaoService.salvar(empresaTrabalhadorLotacao, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO,
							Funcionalidade.EMPRESA))))
					.type(MediaType.APPLICATION_JSON).build();
		} else { 
			LOGGER.debug("Salvando EmpresaTrabalhadorLotacao ");
			return Response.status(HttpServletResponse.SC_CREATED)
					.entity(serializar(empresaTrabalhadorLotacaoService.salvar(empresaTrabalhadorLotacao, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO,
							Funcionalidade.EMPRESA))))
					.type(MediaType.APPLICATION_JSON).build();
		}
			
	}

	@PUT
	@Encoded
	@Path("/remover")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO,
			PermissionConstants.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR }))
	public Response remover(@Encoded EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Removendo Empresa Trabalhador Lotação");
		return Response.status(HttpServletResponse.SC_OK)
				.entity(serializar(empresaTrabalhadorLotacaoService.remover(empresaTrabalhadorLotacao, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
