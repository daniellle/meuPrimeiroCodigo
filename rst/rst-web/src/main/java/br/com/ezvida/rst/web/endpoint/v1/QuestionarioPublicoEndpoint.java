package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.model.Questionario;
import br.com.ezvida.rst.model.QuestionarioTrabalhador;
import br.com.ezvida.rst.service.PerguntaQuestionarioService;
import br.com.ezvida.rst.service.QuestionarioTrabalhadorService;
import br.com.ezvida.rst.service.RespostaQuestionarioTrabalhadorService;
import com.google.common.base.Charsets;
import fw.web.endpoint.SegurancaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/public/v1/questionario")
public class QuestionarioPublicoEndpoint extends SegurancaEndpoint<Questionario> {
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
    public Response montarQuestionario(@PathParam("login") String login, @Context HttpServletRequest request) {
        LOGGER.debug("Aplicando questionario no trabalhador");
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(perguntaQuestionarioService.gerarQuestionario(login, null)))
            .build();
    }

    @POST
    @Encoded
    @Path("/salvar-respostas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response salvarRespostas(@Encoded QuestionarioTrabalhador questionarioTrabalhador,
                                    @Context HttpServletRequest request) {
        LOGGER.debug("Salvando questionario no trabalhador");
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_CREATED)
            .entity(serializar(questionarioTrabalhadorService.salvar(questionarioTrabalhador, null)))
            .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Encoded
    @Path("/buscar-resultado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buscarResultadoQuestionario(@QueryParam("cpf") String cpf, @Context HttpServletRequest request) {
        LOGGER.debug("Buscando resultado do questionario");
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version",
                getApplicationVersion())
            .entity(serializar(respostaQuestionarioTrabalhadorService.getResultadoQuestionario(cpf, null)))
            .build();
    }

    @GET
    @Encoded
    @Path("/historico-paginado")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pesquisarHistorico(@QueryParam("cpf") String cpf, @QueryParam("pagina") Integer pagina, @QueryParam("qtdRegistro") Integer qtdRegistro, @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version",
                getApplicationVersion())
            .entity(serializar(questionarioTrabalhadorService.pesquisaPaginada(cpf, pagina, qtdRegistro, null)))
            .build();
    }

    @GET
    @Encoded
    @Path("/has-ultimo-registro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hasUltimoRegistro(@QueryParam("cpf") String cpf, @Context HttpServletRequest request) {
        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version",
                getApplicationVersion())
            .entity(serializar(questionarioTrabalhadorService.getUltimoRegistro(cpf)))
            .build();
    }

}
