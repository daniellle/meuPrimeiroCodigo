package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.CnaeFilter;
import br.com.ezvida.rst.model.Cnae;
import br.com.ezvida.rst.service.CnaeService;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/cnaes")
public class CnaeEndpoint extends SegurancaEndpoint<Cnae> {
	
	private static final long serialVersionUID = 5129802725216309146L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CnaeEndpoint.class);
	
	@Inject
	private CnaeService cnaeService;
	
	@GET
	@Encoded
	@Path("/versoes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buscarVersoes(@Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Cnae por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(cnaeService.buscarVersoes())).build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.CNAE, PermissionConstants.CNAE_CADASTRAR,
			PermissionConstants.CNAE_ALTERAR, PermissionConstants.CNAE_CONSULTAR, PermissionConstants.CNAE_DESATIVAR }))
	public Response pesquisar(@BeanParam CnaeFilter funcaoFilter, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Pesquisando Cnae por filtro");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(cnaeService.pesquisarPaginado(funcaoFilter))).build();
	}
	
}
