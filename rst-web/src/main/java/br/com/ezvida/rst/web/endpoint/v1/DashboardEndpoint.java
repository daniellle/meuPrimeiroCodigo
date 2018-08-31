package br.com.ezvida.rst.web.endpoint.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import br.com.ezvida.rst.model.Dashboard;
import fw.security.binding.Autorizacao;
import fw.security.binding.Papel;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/dashboards")
public class DashboardEndpoint extends SegurancaEndpoint<Dashboard> {

    private static final long serialVersionUID = -3618279439764362012L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardEndpoint.class);

    private static final Random RANDOM = new Random();

  //@formatter:off
    @GET
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Autorizacao(
			papeis = @Papel(value = { "gestor dn", "suporte" }), permissoes = @Permissao(value = { "alterar",
					"app:configuracoes" })
    )
    //@formatter:on
    public Response getGraficos(@Context HttpServletRequest request, @Context SecurityContext context, @Context UriInfo info) {

        LOGGER.debug("Processando autenticação do usuário no request {}", context.getUserPrincipal());

        getResponse().setCharacterEncoding(Charsets.UTF_8.displayName());

        List<Dashboard> dashboards = new ArrayList<>();

        dashboards.add(new Dashboard(RandomStringUtils.random(8), "card-primary", "#20a8d8", "line", Math.round((RANDOM.nextDouble() * 2000.0d))));
        dashboards.add(new Dashboard(RandomStringUtils.random(8), "card-info", "#63c2de", "line", Math.round((RANDOM.nextDouble() * 2000.0d))));
        dashboards.add(new Dashboard(RandomStringUtils.random(8), "card-warning", "rgba(255,255,255,.2)", "line",
                Math.round((RANDOM.nextDouble() * 2000.0d))));
        dashboards.add(
                new Dashboard(RandomStringUtils.random(8), "card-danger", "rgba(255,255,255,.3)", "bar", Math.round((RANDOM.nextDouble() * 2000.0d)))
                        .adicionar("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16").adicionar(78d, 81d, 80d,
                                45d, 34d, 12d, 40d, 78d, 81d, 80d, 45d, 34d, 12d, 40d, 12d, 40d));

        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
                .entity(serializar(dashboards)).build();

    }

}
