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

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.RespostaQuestionarioTrabalhador;
import br.com.ezvida.rst.service.RespostaQuestionarioTrabalhadorService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/respostasquestionario")
public class RespostaQuestionarioTrabalhadorEndpoint extends SegurancaEndpoint<RespostaQuestionarioTrabalhador> {

	private static final long serialVersionUID = -4933477155106485998L;

	@Inject
	private RespostaQuestionarioTrabalhadorService respostaQuestionarioTrabalhadorService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR,
			PermissionConstants.TRABALHADOR_CADASTRAR, PermissionConstants.TRABALHADOR_ALTERAR,
			PermissionConstants.TRABALHADOR_DESATIVAR, PermissionConstants.TRABALHADOR_CONSULTAR }))
	public Response buscarPorId(@PathParam("id") long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(respostaQuestionarioTrabalhadorService.getResultadoQuestionario(id,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
								Funcionalidade.QUESTIONARIOS))))
				.build();
	}

}
