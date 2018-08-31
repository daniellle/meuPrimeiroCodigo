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
import br.com.ezvida.rst.dao.filter.EmpresaTrabalhadorFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.service.EmpresaTrabalhadorService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/empresa-trabalhadores")
public class EmpresaTrabalhadorEndpont extends SegurancaEndpoint<EmpresaTrabalhador> {

	private static final long serialVersionUID = -5514594451261287841L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorEndpont.class);

	@Inject
	private EmpresaTrabalhadorService empresaTrabalhadorService;

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_TRABALHADOR,
			PermissionConstants.EMPRESA_TRABALHADOR_CADASTRAR, PermissionConstants.EMPRESA_TRABALHADOR_ALTERAR,
			PermissionConstants.EMPRESA_TRABALHADOR_CONSULTAR, PermissionConstants.EMPRESA_TRABALHADOR_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam EmpresaTrabalhadorFilter empresaTrabalhadorFilter,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Listando Trabalhadores associados a empresa");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaTrabalhadorService.pesquisarPaginado(
						empresaTrabalhadorFilter, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@PathParam("id") String id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Buscando EmpresaTrabalhador por id");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaTrabalhadorService.pesquisarPorId(Long.parseLong(id)))).build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_TRABALHADOR,
			PermissionConstants.EMPRESA_TRABALHADOR_CADASTRAR }))
	public Response salvar(@Encoded EmpresaTrabalhador empresaTrabalhador, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		if (empresaTrabalhador.getDataDemissao() != null) {
			LOGGER.debug("Alterando Empresa Trabalhador ");
			return Response
					.status(HttpServletResponse.SC_CREATED).entity(
							serializar(
									empresaTrabalhadorService.salvar(empresaTrabalhador,
											ClienteInfos.getClienteInfos(context, request,
													TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.EMPRESA))))
					.type(MediaType.APPLICATION_JSON).build();
		} else {
			LOGGER.debug("Salvando Empresa Trabalhador ");
			return Response
					.status(HttpServletResponse.SC_CREATED).entity(
							serializar(
									empresaTrabalhadorService.salvar(empresaTrabalhador,
											ClienteInfos.getClienteInfos(context, request,
													TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.EMPRESA))))
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_TRABALHADOR,
			PermissionConstants.EMPRESA_TRABALHADOR_ALTERAR }))
	public void alterar(@Encoded EmpresaTrabalhador empresaTrabalhador, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		salvar(empresaTrabalhador, context, request);
	}

	@PUT
	@Encoded
	@Path("/remover")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.EMPRESA_TRABALHADOR,
			PermissionConstants.EMPRESA_TRABALHADOR_DESATIVAR }))
	public Response remover(@Encoded EmpresaTrabalhador empresaTrabalhador, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Removendo Empresa Trabalhador ");
		return Response.status(HttpServletResponse.SC_OK)
				.entity(serializar(empresaTrabalhadorService.remover(empresaTrabalhador, ClienteInfos
						.getClienteInfos(context, request, TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Encoded
	@Path("/trabalhador/{cpf}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.USUARIO_ENTIDADE,
			PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
			PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR }))
	public Response pesquisarPorTrabalhadorCpf(@PathParam("cpf") String cpf, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Buscando Empresas associados a Trabalhadores por CPF");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(empresaTrabalhadorService.pesquisarPorTrabalhadorCpf(cpf))).build();
	}
}
