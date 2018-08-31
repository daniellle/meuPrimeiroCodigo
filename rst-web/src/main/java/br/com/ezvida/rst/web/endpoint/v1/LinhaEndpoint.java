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
import br.com.ezvida.rst.dao.filter.LinhaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Linha;
import br.com.ezvida.rst.service.LinhaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/linhas")
public class LinhaEndpoint extends SegurancaEndpoint<Linha> {

	private static final long serialVersionUID = 2986073745642824124L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LinhaEndpoint.class);

	@Inject
	private LinhaService linhaService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@PathParam("id") long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Buscando Linha por id");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(linhaService.buscarPorId(id)))
				.build();
	}

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listarTodos(@Context SecurityContext context, @Context HttpServletRequest request,
			@BeanParam LinhaFilter linhaFilter) {
		LOGGER.debug("Listando todas as Linhas");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(linhaService.listarTodos(linhaFilter, ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PRODUTOS_SERVICOS), ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.LINHA, PermissionConstants.LINHA_CADASTRAR }))
	public Response criar(@Encoded Linha linha, @Context SecurityContext context, @Context HttpServletRequest request) {
		LOGGER.debug("Criando Linhas");
		return Response.status(HttpServletResponse.SC_CREATED).entity(linhaService.salvar(linha))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Encoded
	@Path("/unidade-sesi/departamento/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CAT_PRODUTOS_SERVICOS,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_CADASTRAR, PermissionConstants.CAT_PRODUTOS_SERVICOS_ALTERAR,
			PermissionConstants.CAT_PRODUTOS_SERVICOS_CONSULTAR, PermissionConstants.CAT_PRODUTOS_SERVICOS_DESATIVAR,
			PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response buscarLinhasPorIdDepartamentoUat(@PathParam("id") long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Linhas por Id Unidade Sesi");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(
						linhaService.buscarLinhasPorIdDepartamentoUat(id, ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Encoded
	@Path("/unidade-sesi/{ids}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.PESQUISA_SESI_CONSULTAR }))
	public Response buscarLinhasPorIdUat(@PathParam("ids") String ids, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Linhas por Id Unidade Sesi");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(linhaService.buscarLinhasPorIdUat(ids, ClienteInfos.getDadosFilter(context))))
				.build();
	}

}
