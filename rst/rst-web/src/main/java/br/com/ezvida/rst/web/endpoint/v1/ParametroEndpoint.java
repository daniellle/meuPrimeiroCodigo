package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Parametro;
import br.com.ezvida.rst.service.ParametroService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/public/v1/parametro")
public class ParametroEndpoint extends SegurancaEndpoint<Parametro> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParametroEndpoint.class);

	@Inject
	private ParametroService parametroService;

	@GET
	@Encoded
	@Path("/termo-uso")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buscarTermoUso() {
		LOGGER.debug("Buscando Termo de Uso");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(parametroService.getTermoUso()))
				.build();
	}
	
	@GET
	@Encoded
	@Path("/igev")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buscarIgev() {
		LOGGER.debug("Buscando igev");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(parametroService.getIgev()))
				.build();
	}
	
	@GET
	@Encoded
	@Path("/tamanho-maximo-upload-arquivo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pesquisar() {
		LOGGER.debug("Buscando parametro por nome");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(serializar(parametroService.getTamanhoMaximoUpload()))
				.build();
	}

	@GET
	@Encoded
	@Path("/token_acesso_cliente_rst")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTokenAcessoClienteRst() {
		LOGGER.debug("Buscando parametro por nome");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(parametroService.getTokenAcessoClienteRst())).build();
	}
}
