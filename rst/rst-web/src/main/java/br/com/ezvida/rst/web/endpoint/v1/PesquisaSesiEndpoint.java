package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
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
import br.com.ezvida.rst.dao.filter.PesquisaSesiFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.service.PesquisaSesiService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/pesquisa-sesi")
public class PesquisaSesiEndpoint extends SegurancaEndpoint<UnidadeAtendimentoTrabalhador> {

	private static final long serialVersionUID = -2927173264301900443L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PesquisaSesiEndpoint.class);

	@Inject
	private PesquisaSesiService pesquisaSesiService;

	@GET
	@Encoded
	@Path("/unidades-sesi")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response buscarUnidadesSesi(@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(pesquisaSesiService.buscarUnidadesSesi(ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PESQUISA_SESI),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Path("/unidades-sesi/endereco/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response buscarEnderecoUnidadeSesi(@PathParam("id") String id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(pesquisaSesiService.buscarEnderecoUnidadeSesi(
						Long.parseLong(id), ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PESQUISA_SESI),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response pesquisarPaginado(@BeanParam PesquisaSesiFilter pesquisaSesiFilter,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Unidades Sesi Produto Serviço por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(pesquisaSesiService.pesquisarPaginado(
						pesquisaSesiFilter, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PESQUISA_SESI),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

}
