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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.QuestionarioFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Questionario;
import br.com.ezvida.rst.service.PerguntaQuestionarioService;
import br.com.ezvida.rst.service.QuestionarioService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/questionario")
public class QuestionarioEndpoint extends SegurancaEndpoint<Questionario> {

	private static final long serialVersionUID = -3437530326338639874L;

	@Inject
	private QuestionarioService questionarioService;
	
	@Inject
	private PerguntaQuestionarioService perguntaQuestionarioService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO,
			PermissionConstants.QUESTIONARIO_CONSULTAR }))
	public Response pesquisarPorId(@PathParam("id") Long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(questionarioService.pesquisarPorId(id, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO,
			PermissionConstants.QUESTIONARIO_CADASTRAR, PermissionConstants.QUESTIONARIO_ALTERAR,
			PermissionConstants.QUESTIONARIO_CONSULTAR, PermissionConstants.QUESTIONARIO_DESATIVAR }))
	public Response pesquisar(@BeanParam QuestionarioFilter questionarioFilter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(
						questionarioService.pesquisarPaginado(questionarioFilter, ClienteInfos.getClienteInfos(context,
								request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	 @Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO,
			 PermissionConstants.QUESTIONARIO_CADASTRAR }))
	public Response criar(@Encoded Questionario questionario, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(questionarioService.salvar(questionario, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Encoded
	@Path("/publicar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	 @Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO,
			 PermissionConstants.QUESTIONARIO_CADASTRAR }))
	public Response publicar(@Encoded Questionario questionario, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(questionarioService.publicar(questionario, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	 @Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO,
			 PermissionConstants.QUESTIONARIO_ALTERAR }))
	public Response alterar(@Encoded Questionario questionario, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(questionarioService.salvar(questionario, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();

	}
	
	@GET
	@Path("/aplicar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response montarQuestionario(@Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(perguntaQuestionarioService.montarQuestionario(ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}
	
	@GET
	@Encoded
	@Path("/versoes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buscarVersoes(@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(questionarioService.buscarVersoes())).build();
	}
	
	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.QUESTIONARIO,
			PermissionConstants.QUESTIONARIO_DESATIVAR }))
	public Response desativar(@Encoded Questionario questionario, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).entity(serializar(questionarioService.desativar(questionario,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.EMPRESA))))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
