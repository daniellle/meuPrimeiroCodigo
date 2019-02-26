package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.service.EmpresaService;
import fw.web.endpoint.SegurancaEndpoint;


@RequestScoped
@Path("/public/v1/empresas")
public class EmpresaPublicoEndpoint extends SegurancaEndpoint<Empresa>{

    private static final long serialVersionUID = -3318279439764362012L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaPublicoEndpoint.class);

    @Inject
    private EmpresaService empresaService;

    @GET
    @Path("/empresaFornecedor/{cnpj}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response pesquisarEmpresaPorCnpj(@PathParam("cnpj") String cnpj, @Context SecurityContext context, @Context HttpServletRequest request) {
        LOGGER.debug("Pesquisando Empresa por CNPJ");
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
            .entity(serializar(empresaService.buscarEmpresaCadastroPorCnpj(cnpj)))
            .build();
    }
}
