package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.model.EmpresaTrabalhadorLotacao;
import br.com.ezvida.rst.service.EmpresaTrabalhadorLotacaoService;
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
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/public/v1/empresa-trabalhador-lotacoes")
public class EmpresaTrabalhadorLotacaoPublicoEndpoint extends SegurancaEndpoint<EmpresaTrabalhadorLotacao> {

    private static final long serialVersionUID = -6282406320669919507L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorLotacaoPublicoEndpoint.class);

    @Inject
    private EmpresaTrabalhadorLotacaoService empresaTrabalhadorLotacaoService;

    @GET
    @Encoded
    @Path("/validar-trabalhador/{cpf}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validarEmpregado(@Encoded @PathParam("cpf") String cpf, @Context SecurityContext context, @Context HttpServletRequest request) {
        LOGGER.debug("Validando trabalhador");

        return Response.status(HttpServletResponse.SC_OK)
            .entity(serializar(empresaTrabalhadorLotacaoService.validarTrabalhador(cpf)))
            .type(MediaType.APPLICATION_JSON).build();
    }
}
