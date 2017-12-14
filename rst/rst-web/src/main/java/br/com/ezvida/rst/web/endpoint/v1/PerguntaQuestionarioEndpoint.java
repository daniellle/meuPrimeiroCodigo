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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.PerguntaQuestionarioFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.PerguntaQuestionario;
import br.com.ezvida.rst.service.PerguntaQuestionarioService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/montar-questionario")
public class PerguntaQuestionarioEndpoint extends SegurancaEndpoint<PerguntaQuestionario> {

	private static final long serialVersionUID = -3312235494273477952L;

	@Inject
	private PerguntaQuestionarioService perguntaQuestionarioService;

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO, PermissionConstants.QUESTIONARIO_CONSULTAR }))
	public Response pesquisar(@BeanParam PerguntaQuestionarioFilter perguntaFilter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion()).entity(
						serializar(
								perguntaQuestionarioService.perguntaQuestionario(perguntaFilter,
										ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
												Funcionalidade.QUESTIONARIOS),
										ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response montarQuestionario(@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(perguntaQuestionarioService.montarQuestionario(ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/pergunta-resposta")
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO,
			PermissionConstants.QUESTIONARIO_CADASTRAR, PermissionConstants.QUESTIONARIO_ALTERAR }))
	public Response associarPerguntaQuestionario(@Encoded PerguntaQuestionario perguntaQuestionario,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response
				.status(HttpServletResponse.SC_CREATED).entity(
						perguntaQuestionarioService
								.associarPerguntaQuestionario(perguntaQuestionario,
										ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO,
												Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Path("pergunta-desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO, PermissionConstants.QUESTIONARIO_DESATIVAR }))
	public Response desativar(@Encoded PerguntaQuestionario perguntaQuestionario, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED).entity(serializar(perguntaQuestionarioService.desativarPerguntaQuestionario(perguntaQuestionario,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
