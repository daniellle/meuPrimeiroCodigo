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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.EmpresaLotacaoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaLotacao;
import br.com.ezvida.rst.service.EmpresaLotacaoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-lotacao")
public class EmpresaLotacaoEndpoint extends SegurancaEndpoint<EmpresaLotacao> {

	private static final long serialVersionUID = 2924271070647871227L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaLotacaoEndpoint.class);

	@Inject
	private EmpresaLotacaoService empresaLotacaoService;

	@GET
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_LOTACAO,
			PermissionConstants.EMPRESA_LOTACAO_CADASTRAR, PermissionConstants.EMPRESA_LOTACAO_ALTERAR, 
			PermissionConstants.EMPRESA_LOTACAO_CONSULTAR, PermissionConstants.EMPRESA_LOTACAO_DESATIVAR }))
	public Response buscarEmpresaLotacoes(@BeanParam EmpresaLotacaoFilter empresaLotacaoFilter, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Lotacoes por Empresas");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaLotacaoService.pesquisarEmpresaLotacaoPaginada(empresaLotacaoFilter,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA)))).build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_LOTACAO,
			PermissionConstants.EMPRESA_LOTACAO_CADASTRAR }))
	public Response criarEmpresaLotacoes(@Encoded Set<EmpresaLotacao> empresaLotacao, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Salvando Empresa Lotacao ");
		return Response.status(HttpServletResponse.SC_CREATED).entity(serializar(empresaLotacaoService
				.salvar(empresaLotacao,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_LOTACAO,
			PermissionConstants.EMPRESA_LOTACAO_DESATIVAR }))
	public Response desativarEmpresaLotacao(@Encoded EmpresaLotacao empresaLotacao, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Desativando Empresa Lotacao ");
		return Response.status(HttpServletResponse.SC_OK).entity(empresaLotacaoService
				.desativar(empresaLotacao,ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))).type(MediaType.APPLICATION_JSON)
				.build();
	}

}
