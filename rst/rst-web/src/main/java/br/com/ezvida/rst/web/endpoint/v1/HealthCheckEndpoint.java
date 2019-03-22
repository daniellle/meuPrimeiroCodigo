package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.web.util.Status;
import fw.web.endpoint.BaseEndpoint;

@RequestScoped
@Path("/health")
public class HealthCheckEndpoint extends BaseEndpoint<Status> {

	private static final long serialVersionUID = 2201569915753832992L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckEndpoint.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response check() {
		LOGGER.info("Checking status of application...");
		return Response.status(HttpServletResponse.SC_OK).entity(new Status("Ok!", getApplicationVersion())).build();
	}
}
