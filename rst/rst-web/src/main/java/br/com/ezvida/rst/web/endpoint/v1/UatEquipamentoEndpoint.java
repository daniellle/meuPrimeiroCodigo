package br.com.ezvida.rst.web.endpoint.v1;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatVeiculoTipo;
import br.com.ezvida.rst.model.dto.UatEquipamentoDTO;
import br.com.ezvida.rst.service.UatEquipamentoAreaService;
import br.com.ezvida.rst.service.UatEquipamentoService;
import br.com.ezvida.rst.service.UatEquipamentoTipoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/uat-equipamento")
public class UatEquipamentoEndpoint extends SegurancaEndpoint<UatVeiculoTipo> {

	private static final long serialVersionUID = 1L;
	private static final String CONTENT_VERSION = "Content-Version";

	@Inject
	private UatEquipamentoService uatEquipamentoService;
	
	@Inject
	private UatEquipamentoAreaService uatEquipamentoAreaService;
	
	@Inject
	private UatEquipamentoTipoService uatEquipamentoTipoService;
	
	@GET
	@Path("/tipo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllVeiculoTipo(@QueryParam("idArea") Long idArea, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(uatEquipamentoTipoService.listarTodosPorArea(idArea, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
						Funcionalidade.GESTAO_UNIDADE_SESI)))).build();
	}
	
	@GET
	@Path("/area")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllVeiculoTipoAtendimento(@Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(uatEquipamentoAreaService.listarTodos(ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
						Funcionalidade.GESTAO_UNIDADE_SESI)))).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = {PermissionConstants.CAT_ESTRUTURA_CONSULTAR}))
	public Response listAllUatEquipamentosGroupedByArea(@QueryParam("idUat") Long idUat, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(
						uatEquipamentoService.listarTodosEquipamentosPorIdUatAgrupadosPorArea(idUat,
								ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
										Funcionalidade.GESTAO_UNIDADE_SESI),
								ClienteInfos.getDadosFilter(context))))
				.build();
	}
	
	@DELETE
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT_ESTRUTURA_DESATIVAR }))
	public Response desativar(@QueryParam("idEquipamento") Long idEquipamento, @QueryParam("idUat") Long idUat,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		uatEquipamentoService
				.desativar(
						idEquipamento, idUat, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.GESTAO_UNIDADE_SESI),
						ClienteInfos.getDadosFilter(context));
		return Response.status(HttpServletResponse.SC_NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT_ESTRUTURA_CADASTRAR }))
	public Response cadastrar(@Encoded List<UatEquipamentoDTO> listUatEquipamentoDTO, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(uatEquipamentoService.salvar(listUatEquipamentoDTO,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO,
								Funcionalidade.GESTAO_UNIDADE_SESI),
						ClienteInfos.getDadosFilter(context)))
				.type(MediaType.APPLICATION_JSON).build();
	}
	
}
