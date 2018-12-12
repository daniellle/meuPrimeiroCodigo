package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.model.dto.RelatorioUsuarioDTO;
import br.com.ezvida.rst.service.RelatorioService;
import fw.web.endpoint.SegurancaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/public/v1/relatorio")
public class RelatorioTesteEndpoint extends SegurancaEndpoint<RelatorioUsuarioDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioTesteEndpoint.class);

    @Inject
    private RelatorioService service;

    @GET
    @Path("/pdf")
    @Produces("application/pdf")
    public Response gerarRelatorio(){

        LOGGER.debug("iniciando a geração do pdf");

        byte[] file = service.gerarRelatorioPDF();
        return Response.status(HttpServletResponse.SC_OK)
            .header("Content-Lenght", file.length)
            .header("Content-Version", getApplicationVersion())
            .header("Content-Disposition",
                "attachment; filename=usuarios.pdf")
            .entity(file).build();
    }

    @GET
    @Path("/csv")
    @Produces("text/csv")
    public Response gerarRelatorioCsv(){

        LOGGER.debug("iniciando a geração do csv");

        byte[] file = service.gerarRelatorioCSV();
        return Response.status(HttpServletResponse.SC_OK)
            .header("Content-Lenght", file.length)
            .header("Content-Version", getApplicationVersion())
            .header("Content-Disposition",
                "attachment; filename=usuarios.csv")
            .entity(file).build();
    }
}
