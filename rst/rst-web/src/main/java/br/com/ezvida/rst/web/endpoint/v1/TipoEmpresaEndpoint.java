package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Token;
import br.com.ezvida.rst.service.TipoEmpresaService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/tiposEmpresas")
public class TipoEmpresaEndpoint extends SegurancaEndpoint<Token> {

	private static final long serialVersionUID = -5946935555743247998L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TipoEmpresaEndpoint.class);

	@Inject
	private TipoEmpresaService tipoEmpresaService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarTodos() {
		LOGGER.debug("Pesquisando Tipos Empresa");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(tipoEmpresaService.listarTodos()))
				.build();
	}
}
