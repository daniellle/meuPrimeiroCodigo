package br.com.ezvida.rst.web;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import br.com.ezvida.rst.model.MensagemErro;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.exception.BusinessException;
import fw.security.exception.UnauthenticatedException;
import fw.web.ErroServico;

@Provider
public class TratamentoErroServico extends ErroServico {

	@Override
	public Response toResponse(Exception exception) {

		if (exception instanceof UnauthenticatedException) {
			return Response.status(HttpServletResponse.SC_FORBIDDEN).type(MediaType.APPLICATION_JSON)
					.header("Content-Version", getApplicationVersion())
					.entity(new MensagemErro("403", exception.getMessage(), exception.getCause() != null ? exception.getCause().toString() : ""))
					.build();
		}

		if (exception instanceof RegistroNaoEncontradoException) {
			return Response.status(HttpServletResponse.SC_NOT_FOUND).type(MediaType.APPLICATION_JSON)
					.header("Content-Version", getApplicationVersion())
					.entity(new MensagemErro("404", 
							exception.getMessage(), 
							exception.getCause() != null ? exception.getCause().toString() : "")).build();
		}

		if (exception instanceof BusinessErrorException || exception instanceof BusinessException) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).type(MediaType.APPLICATION_JSON)
					.header("Content-Version", getApplicationVersion())
					.entity(new MensagemErro("400", 
							exception.getMessage(), 
							exception.getCause() != null ? exception.getCause().toString() : "")).build();
		}
		

		return super.toResponse(exception);	
	}
}
