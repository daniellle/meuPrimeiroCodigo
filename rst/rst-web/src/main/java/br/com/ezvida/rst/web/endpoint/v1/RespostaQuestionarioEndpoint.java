package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.model.RespostaQuestionario;
import br.com.ezvida.rst.service.RespostaQuestionarioService;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/resposta")
public class RespostaQuestionarioEndpoint extends SegurancaEndpoint<RespostaQuestionario> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4791076363414160630L;

	@Inject
	private RespostaQuestionarioService respostaQuestionarioService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@PathParam("id") Long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(respostaQuestionarioService.pesquisarPorPerguntaQuestionario(id))).build();
	}
	
}
