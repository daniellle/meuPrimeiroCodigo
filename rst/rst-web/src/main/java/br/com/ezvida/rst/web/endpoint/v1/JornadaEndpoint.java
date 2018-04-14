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

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.JornadaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Jornada;
import br.com.ezvida.rst.service.JornadaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/jornada")
public class JornadaEndpoint extends SegurancaEndpoint<Jornada> {
	
	private static final long serialVersionUID = 5129802725216309146L;
	
	@Inject
	private JornadaService jornadaService;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") String id,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(jornadaService.pesquisarPorId((Long.parseLong(id)), ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA)))).build();
	}
	
	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.JORNADA,
			PermissionConstants.JORNADA_CADASTRAR, PermissionConstants.JORNADA_ALTERAR,
			PermissionConstants.JORNADA_CONSULTAR, PermissionConstants.JORNADA_DESATIVAR }))
	public Response pesquisar(@BeanParam JornadaFilter jornadaFilter,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(jornadaService.pesquisarPaginado(jornadaFilter
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.EMPRESA)))).build();
	}
	
}
