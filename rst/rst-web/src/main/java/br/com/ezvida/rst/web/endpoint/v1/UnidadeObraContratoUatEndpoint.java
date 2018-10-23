package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.model.UnidadeObraContratoUat;
import br.com.ezvida.rst.service.UnidadeObraContratoUatService;
import fw.web.endpoint.SegurancaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/private/v1/unidades-obras-contratos-uat")
public class UnidadeObraContratoUatEndpoint extends SegurancaEndpoint<UnidadeObraContratoUat> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeObraContratoUatEndpoint.class);

    @Inject
    private UnidadeObraContratoUatService unidadeObraContratoUatService;

    @GET
    @Encoded
    @Path("/validar-unidades/{cnpj}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validarUnidadeObraPorEmpresa(@Encoded @PathParam("cnpj") String cnpj){
        LOGGER.debug("Validando a Unidade de Obra");
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header("Content-Version", getApplicationVersion())
            .entity(serializar(unidadeObraContratoUatService.validarPorEmpresa(cnpj))).build();
    }

}
