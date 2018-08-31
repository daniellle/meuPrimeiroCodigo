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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.auditoria.model.Auditoria;
import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.AuditoriaFilter;
import br.com.ezvida.rst.service.AuditoriaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/auditoria")
public class AuditoriaEndpoint extends SegurancaEndpoint<Auditoria> {

	private static final long serialVersionUID = -8235323477706977946L;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@GET
	@Encoded
	@Path("/pesquisar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.AUDITORIA, PermissionConstants.AUDITORIA_CONSULTAR }))
	public Response pesquisar(@BeanParam AuditoriaFilter auditoriaFilter, @Context SecurityContext context,
			@Context HttpServletRequest request) {	
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(
						auditoriaService.pesquisarPorFiltro(auditoriaFilter, ClienteInfos.getDadosFilter(context), ClienteInfos.getUsuario(context))))
				.build();
	}


}
