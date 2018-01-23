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

import br.com.ezvida.rst.dao.filter.QuestionarioTrabalhadorFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.QuestionarioTrabalhador;
import br.com.ezvida.rst.service.QuestionarioTrabalhadorService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/questionario-trabalhador")
public class QuestionarioTrabalhadorEndpoint extends SegurancaEndpoint<QuestionarioTrabalhador> {

	private static final long serialVersionUID = -7120366210629150946L;

	@Inject
	private QuestionarioTrabalhadorService questionarioTrabalhadorService;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pesquisarPorId(@PathParam("id") Long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(questionarioTrabalhadorService.buscarPorId(id, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIO_TRABALHADOR))))
				.build();
	}

	@GET
	@Path("{id}/periodo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response periodo(@PathParam("id") Long idTrabalhador, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(questionarioTrabalhadorService.getUltimoRegistro(idTrabalhador)))
				.build();
	}
	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pesquisar(@BeanParam QuestionarioTrabalhadorFilter questionarioTrabalhadorFilter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(
						questionarioTrabalhadorService.pesquisaPaginada(questionarioTrabalhadorFilter, ClienteInfos.getClienteInfos(context,
								request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response criar(@Encoded QuestionarioTrabalhador questionarioTrabalhador, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		QuestionarioTrabalhador questionarioTrabalhadorSalvo = questionarioTrabalhadorService.salvar(questionarioTrabalhador, ClienteInfos.getClienteInfos(context, request,
				TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.QUESTIONARIOS));
		
		questionarioTrabalhadorSalvo = questionarioTrabalhadorService.buscarPorId(questionarioTrabalhador.getId(), 
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.QUESTIONARIOS));
		
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(serializar(questionarioTrabalhadorSalvo))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterar(@Encoded QuestionarioTrabalhador questionarioTrabalhador, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(questionarioTrabalhadorService.salvar(questionarioTrabalhador, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();

	}
	
}
