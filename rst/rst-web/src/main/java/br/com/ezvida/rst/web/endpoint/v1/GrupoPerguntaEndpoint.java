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
import br.com.ezvida.rst.dao.filter.GrupoPerguntaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.GrupoPergunta;
import br.com.ezvida.rst.service.GrupoPerguntaService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/grupos-perguntas")
public class GrupoPerguntaEndpoint extends SegurancaEndpoint<GrupoPergunta> {

	private static final long serialVersionUID = 8189366581496732058L;

	@Inject
	private GrupoPerguntaService grupoPerguntaService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.GRUPO_PERGUNTA,
			PermissionConstants.GRUPO_PERGUNTA_ALTERAR, PermissionConstants.GRUPO_PERGUNTA_DESATIVAR,
			PermissionConstants.GRUPO_PERGUNTA_CONSULTAR }))
	public Response buscarPorId(@PathParam("id") long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(grupoPerguntaService.buscarPorId(id, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.GRUPO_PERGUNTA,
			PermissionConstants.GRUPO_PERGUNTA_CADASTRAR, PermissionConstants.GRUPO_PERGUNTA_ALTERAR,
			PermissionConstants.GRUPO_PERGUNTA_CONSULTAR, PermissionConstants.GRUPO_PERGUNTA_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam GrupoPerguntaFilter filter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(grupoPerguntaService.pesquisaPaginada(filter, ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.GRUPO_PERGUNTA,
			PermissionConstants.GRUPO_PERGUNTA_CADASTRAR }))
	public Response criar(@Encoded GrupoPergunta grupoPergunta, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(grupoPerguntaService.salvar(grupoPergunta, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Path("/editar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.GRUPO_PERGUNTA,
			PermissionConstants.GRUPO_PERGUNTA_ALTERAR }))
	public Response alterar(@Encoded GrupoPergunta grupoPergunta, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(grupoPerguntaService.salvar(grupoPergunta, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.GRUPO_PERGUNTA,
			PermissionConstants.GRUPO_PERGUNTA_DESATIVAR }))
	public Response desativar(@Encoded GrupoPergunta grupoPergunta, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(grupoPerguntaService.desativarGrupoPergunta(grupoPergunta, ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
