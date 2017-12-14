package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Periodicidade;
import br.com.ezvida.rst.service.PeriodicidadeService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/periodicidade")
public class PeriodicidadeEndpoint extends SegurancaEndpoint<Periodicidade> {

	private static final long serialVersionUID = -3485344843635750996L;

	@Inject
	private PeriodicidadeService periodicidadeService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") String id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(
						periodicidadeService.pesquisarPorId((Long.parseLong(id)), ClienteInfos.getClienteInfos(context,
								request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PROFISSIONAL))))
				.build();
	}

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buscarVersoes(@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(periodicidadeService.listarTodos(ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.PROFISSIONAL))))
				.build();
	}

}
