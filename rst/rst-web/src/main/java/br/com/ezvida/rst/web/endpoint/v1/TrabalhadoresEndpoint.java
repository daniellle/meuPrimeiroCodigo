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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.QuestionarioTrabalhadorFilter;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.TrabalhadorDependente;
import br.com.ezvida.rst.service.QuestionarioTrabalhadorService;
import br.com.ezvida.rst.service.TrabalhadorDependenteService;
import br.com.ezvida.rst.service.TrabalhadorService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/trabalhadores")
public class TrabalhadoresEndpoint extends SegurancaEndpoint<Trabalhador> {

	private static final String CONTENT_VERSION = "Content-Version";

	private static final long serialVersionUID = 3273560908867006426L;

	@Inject
	private TrabalhadorService trabalhadorService;

	@Inject
	private TrabalhadorDependenteService trabalhadorDependenteService;

	@Inject
	private QuestionarioTrabalhadorService questionarioTrabalhadorService;

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR,
			PermissionConstants.TRABALHADOR_ALTERAR, PermissionConstants.TRABALHADOR_DESATIVAR,
			PermissionConstants.TRABALHADOR_CONSULTAR, PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CADASTRAR, PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CONSULTAR, PermissionConstants.TRABALHADOR_DEPENDENTE_DESATIVAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO, PermissionConstants.TRABALHADOR_CERTIFICADO_CADASTRAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_CONSULTAR, PermissionConstants.TRABALHADOR_CERTIFICADO_ALTERAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_DESATIVAR}))
	public Response buscarPorId(@BeanParam TrabalhadorFilter trabalhadorFilter, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(trabalhadorService.buscarPorId(trabalhadorFilter,
						ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA, Funcionalidade.TRABALHADOR),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@GET
	@Path("/meus-dados")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR,
			PermissionConstants.TRABALHADOR_ALTERAR, PermissionConstants.TRABALHADOR_DESATIVAR,
			PermissionConstants.TRABALHADOR_CONSULTAR, PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CADASTRAR, PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CONSULTAR, PermissionConstants.TRABALHADOR_DEPENDENTE_DESATIVAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO, PermissionConstants.TRABALHADOR_CERTIFICADO_CADASTRAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_CONSULTAR, PermissionConstants.TRABALHADOR_CERTIFICADO_ALTERAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_DESATIVAR}))
	public Response buscarMeusDados(@Context SecurityContext context, @Context HttpServletRequest request) {
		ClienteAuditoria auditoria = ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.TRABALHADOR);
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(trabalhadorService.buscarPorCpf(auditoria.getUsuario())))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR,	PermissionConstants.TRABALHADOR_CADASTRAR,
			PermissionConstants.TRABALHADOR_ALTERAR, PermissionConstants.TRABALHADOR_DESATIVAR,
			PermissionConstants.TRABALHADOR_CONSULTAR, PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CADASTRAR, PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CONSULTAR, PermissionConstants.TRABALHADOR_DEPENDENTE_DESATIVAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO, PermissionConstants.TRABALHADOR_CERTIFICADO_CADASTRAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_CONSULTAR, PermissionConstants.TRABALHADOR_CERTIFICADO_ALTERAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_DESATIVAR}))
	public Response pesquisarPaginado(@BeanParam TrabalhadorFilter trabalhadorFilter, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(trabalhadorService.pesquisarPaginado(trabalhadorFilter,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.TRABALHADOR),
						ClienteInfos.getDadosFilter(context))))
				.build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR, PermissionConstants.TRABALHADOR_CADASTRAR }))
	public Response criar(@Encoded Trabalhador trabalhador, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(trabalhadorService.salvar(trabalhador
						, ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.INCLUSAO,Funcionalidade.TRABALHADOR)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Encoded
	@Path("/historico/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR,
			PermissionConstants.TRABALHADOR_CADASTRAR, PermissionConstants.TRABALHADOR_ALTERAR,
			PermissionConstants.TRABALHADOR_DESATIVAR, PermissionConstants.TRABALHADOR_CONSULTAR }))
	public Response pesquisarHistorico(@BeanParam QuestionarioTrabalhadorFilter questionarioTrabalhadorFilter,
			@Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION,
						getApplicationVersion())
				.entity(serializar(questionarioTrabalhadorService.pesquisaPaginada(questionarioTrabalhadorFilter,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
								Funcionalidade.QUESTIONARIOS))))
				.build();
	}

	@POST
	@Encoded
	@Path("{idTrabalhador}/dependente")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CADASTRAR, PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR }))
	public Response criarDependente(@PathParam("idTrabalhador") Long idTrabalhador,
			@Encoded TrabalhadorDependente trabalhadorDependente, @Context SecurityContext context
			,@Context HttpServletRequest request) {
		TrabalhadorDependente trabalhadorDependenteSalvo = trabalhadorDependenteService.salvar(trabalhadorDependente,
				idTrabalhador
				,ClienteInfos.getClienteInfos(context, request
						,TipoOperacaoAuditoria.INCLUSAO,Funcionalidade.TRABALHADOR));
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.entity(trabalhadorDependenteSalvo).build();
	}

	@GET
	@Encoded
	@Path("{id}/dependente")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR,	PermissionConstants.TRABALHADOR_DEPENDENTE_CONSULTAR,
			PermissionConstants.TRABALHADOR_DEPENDENTE_DESATIVAR }))
	public Response buscarTrabalhadorDependente(@PathParam("id") Long id, @Context SecurityContext context
			,@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(trabalhadorDependenteService.pesquisarPorTrabalhador(id
						,ClienteInfos.getClienteInfos(context, request
								,TipoOperacaoAuditoria.CONSULTA,Funcionalidade.TRABALHADOR),
						ClienteInfos.getDadosFilter(context)))).build();
	}

	@GET
	@Encoded
	@Path("/dependente")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR,	PermissionConstants.TRABALHADOR_DEPENDENTE_CONSULTAR,
			PermissionConstants.TRABALHADOR_DEPENDENTE_DESATIVAR }))
	public Response buscarTrabalhadorDependentePorCpf(@QueryParam("cpf") String cpf, @Context SecurityContext context
			,@Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(trabalhadorDependenteService.pesquisarDependentePorCPF(cpf
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.TRABALHADOR)))).build();
	}

	@PUT
	@Encoded
	@Path("/dependente/desativar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR,	PermissionConstants.TRABALHADOR_DEPENDENTE_CONSULTAR,
			PermissionConstants.TRABALHADOR_DEPENDENTE_DESATIVAR }))
	public Response removerDependente(@Encoded TrabalhadorDependente trabalhadorDependente,
			@Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK)
				.entity(trabalhadorDependenteService.desativar(trabalhadorDependente
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.DESATIVACAO,Funcionalidade.TRABALHADOR))).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR }))
	public Response alterar(@Encoded Trabalhador trabalhador, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).entity(trabalhadorService.salvar(trabalhador,
				ClienteInfos.getClienteInfos(context, request,
						TipoOperacaoAuditoria.INCLUSAO,Funcionalidade.TRABALHADOR)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Encoded
	@Path("/dependentes/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_DEPENDENTE,
			PermissionConstants.TRABALHADOR_DEPENDENTE_ALTERAR, PermissionConstants.TRABALHADOR_DEPENDENTE_CADASTRAR,
			PermissionConstants.TRABALHADOR_DEPENDENTE_CONSULTAR, PermissionConstants.TRABALHADOR_DEPENDENTE_DESATIVAR }))
	public Response buscarTrabalhadorDependentePaginado(@BeanParam TrabalhadorFilter trabalhadorFilter
			, @Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(trabalhadorDependenteService.pesquisarPorTrabalhador(trabalhadorFilter
						,ClienteInfos.getClienteInfos(context, request,
								TipoOperacaoAuditoria.CONSULTA,Funcionalidade.TRABALHADOR)))).build();
	}

    @GET
    @Encoded
    @Path("/vacinas-alergias-medicamentos-auto-declarados/{cpf}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buscarVacinasAlergiasMedicamentosAutoDeclarados(@PathParam("cpf") String cpf, @Context SecurityContext context, @Context HttpServletRequest request) {
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion()).entity(serializar(trabalhadorService.buscarVacinasAlergiasMedicamentosAutoDeclarados(cpf)))
            .build();
    }

	@GET
	@Encoded
	@Path("/vidaativa/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = {PermissionConstants.DEPARTAMENTO_REGIONAL, PermissionConstants.DEPARTAMENTO_REGIONAL_CONSULTAR, PermissionConstants.USUARIO }))
	public Response buscarVidaAtiva(@PathParam("id") String id, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(trabalhadorService.buscarTrabalhadorVidaAtiva(id)))
				.build();
	}

}
