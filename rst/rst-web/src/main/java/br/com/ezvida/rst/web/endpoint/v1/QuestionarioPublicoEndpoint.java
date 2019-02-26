package br.com.ezvida.rst.web.endpoint.v1;

import java.nio.charset.StandardCharsets;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Questionario;
import br.com.ezvida.rst.model.QuestionarioTrabalhador;
import br.com.ezvida.rst.service.PerguntaQuestionarioService;
import br.com.ezvida.rst.service.QuestionarioTrabalhadorService;
import br.com.ezvida.rst.service.RespostaQuestionarioTrabalhadorService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/public/v1/questionario")
public class QuestionarioPublicoEndpoint extends SegurancaEndpoint<Questionario> {

	private static final String CONTENT_VERSION = "Content-Version";

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionarioPublicoEndpoint.class);

    @Inject
    private PerguntaQuestionarioService perguntaQuestionarioService;

    @Inject
    private QuestionarioTrabalhadorService questionarioTrabalhadorService;

    @Inject
    private RespostaQuestionarioTrabalhadorService respostaQuestionarioTrabalhadorService;


    @GET
    @Path("/aplicar/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.QUESTIONARIO, PermissionConstants.QUESTIONARIO_CONSULTAR}))
    public Response montarQuestionario(@PathParam("login") String login, @Context SecurityContext context,
                                       @Context HttpServletRequest request) {
        LOGGER.debug("Aplicando questionario no trabalhador");
        getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(perguntaQuestionarioService.gerarQuestionario(login, ClienteInfos.getClienteInfos(context,
                request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.QUESTIONARIOS))))
            .build();
    }

    @POST
    @Encoded
    @Path("/salvar-respostas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.QUESTIONARIO,
        PermissionConstants.QUESTIONARIO_CADASTRAR, PermissionConstants.QUESTIONARIO_CONSULTAR,
        PermissionConstants.QUESTIONARIO_ALTERAR}))
    public Response salvarRespostas(@Encoded QuestionarioTrabalhador questionarioTrabalhador, @Context SecurityContext context,
                                    @Context HttpServletRequest request) {
        LOGGER.debug("Salvando questionario no trabalhador");
        getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_CREATED)
            .entity(serializar(questionarioTrabalhadorService.salvar(questionarioTrabalhador, ClienteInfos.getClienteInfos(context, request,
                TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.QUESTIONARIOS))))
            .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Encoded
    @Path("/buscar-resultado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.TRABALHADOR, PermissionConstants.TRABALHADOR_CONSULTAR,
        PermissionConstants.QUESTIONARIO, PermissionConstants.QUESTIONARIO_CONSULTAR}))
    public Response buscarResultadoQuestionario(@QueryParam("cpf") String cpf, @Context SecurityContext context,
                                                @Context HttpServletRequest request) {
        LOGGER.debug("Buscando resultado do questionario");
        getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION,
                getApplicationVersion())
            .entity(serializar(respostaQuestionarioTrabalhadorService.getResultadoQuestionario(cpf,
                ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
                    Funcionalidade.QUESTIONARIOS))))
            .build();
    }

    @GET
    @Encoded
    @Path("/historico-paginado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.TRABALHADOR, PermissionConstants.TRABALHADOR_ALTERAR,
        PermissionConstants.TRABALHADOR_CONSULTAR, PermissionConstants.QUESTIONARIO, PermissionConstants.QUESTIONARIO_CONSULTAR}))
    public Response pesquisarHistorico(@QueryParam("cpf") String cpf, @QueryParam("pagina") Integer pagina, @QueryParam("qtdRegistro") Integer qtdRegistro,
                                       @Context SecurityContext context, @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION,
                getApplicationVersion())
            .entity(serializar(questionarioTrabalhadorService.pesquisaPaginada(cpf, pagina, qtdRegistro,
                ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
                    Funcionalidade.QUESTIONARIOS))))
            .build();
    }

    @GET
    @Encoded
    @Path("/has-ultimo-registro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.TRABALHADOR, PermissionConstants.TRABALHADOR_ALTERAR,
        PermissionConstants.TRABALHADOR_CONSULTAR, PermissionConstants.QUESTIONARIO, PermissionConstants.QUESTIONARIO_CONSULTAR}))
    public Response hasUltimoRegistro(@QueryParam("cpf") String cpf, @Context SecurityContext context, @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION,
                getApplicationVersion())
            .entity(serializar(questionarioTrabalhadorService.getUltimoRegistro(cpf)))
            .build();
    }

}
