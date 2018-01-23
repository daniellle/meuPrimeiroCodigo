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
import br.com.ezvida.rst.dao.filter.ClassificacaoPontuacaoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Certificado;
import br.com.ezvida.rst.model.ClassificacaoPontuacao;
import br.com.ezvida.rst.service.ClassificacaoPontuacaoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/classificacoes")
public class ClassificacaoPontuacaoEndpoint extends SegurancaEndpoint<Certificado> {

	private static final long serialVersionUID = 3144716877202310861L;

	@Inject
	private ClassificacaoPontuacaoService classificacaoPontuacaoService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CLASSIFICACAO_PONTUACAO,
			PermissionConstants.CLASSIFICACAO_PONTUACAO_ALTERAR, PermissionConstants.CLASSIFICACAO_PONTUACAO_DESATIVAR,
			PermissionConstants.CLASSIFICACAO_PONTUACAO_CONSULTAR }))
	public Response buscarPorId(@PathParam("id") long id, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(classificacaoPontuacaoService.buscarPorId(id, ClienteInfos.getClienteInfos(context,
						request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CLASSIFICACAO_PONTUACAO,
			PermissionConstants.CLASSIFICACAO_PONTUACAO_CADASTRAR, PermissionConstants.CLASSIFICACAO_PONTUACAO_ALTERAR,
			PermissionConstants.CLASSIFICACAO_PONTUACAO_CONSULTAR, PermissionConstants.CLASSIFICACAO_PONTUACAO_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam ClassificacaoPontuacaoFilter filter, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version",
						getApplicationVersion())
				.entity(serializar(classificacaoPontuacaoService.pesquisaPaginada(filter, ClienteInfos.getClienteInfos(
						context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CLASSIFICACAO_PONTUACAO,
			PermissionConstants.CLASSIFICACAO_PONTUACAO_CADASTRAR }))
	public Response criar(@Encoded ClassificacaoPontuacao classificacaoPontuacao, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(classificacaoPontuacaoService.salvar(classificacaoPontuacao, ClienteInfos.getClienteInfos(
						context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Path("/editar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CLASSIFICACAO_PONTUACAO,
			PermissionConstants.CLASSIFICACAO_PONTUACAO_ALTERAR }))
	public Response alterar(@Encoded ClassificacaoPontuacao classificacaoPontuacao, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(classificacaoPontuacaoService.salvar(classificacaoPontuacao, ClienteInfos.getClienteInfos(
						context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	@PUT
	@Encoded
	@Path("/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CLASSIFICACAO_PONTUACAO,
			PermissionConstants.CLASSIFICACAO_PONTUACAO_DESATIVAR }))
	public Response desativar(@Encoded ClassificacaoPontuacao classificacaoPontuacao, @Context SecurityContext context,
			@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(classificacaoPontuacaoService.desativar(classificacaoPontuacao, ClienteInfos.getClienteInfos(
						context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.QUESTIONARIOS)))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
