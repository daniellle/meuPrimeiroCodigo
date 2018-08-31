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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.LinhaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.TipoCurso;
import br.com.ezvida.rst.service.TipoCursoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/tipo-cursos")
public class TipoCursoEndpoint  extends SegurancaEndpoint<TipoCurso> {
	
	private static final long serialVersionUID = 5799835749090417219L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LinhaEndpoint.class);
	
	@Inject
	private TipoCursoService tipoCursoService;
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@PathParam("id") long id, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Buscando Linha por id");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(tipoCursoService.buscarPorId(id))).build();
	}

	@GET
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listarTodos(@Context SecurityContext context
			, @Context HttpServletRequest request, @BeanParam LinhaFilter linhaFilter) {
		LOGGER.debug("Listando todas as Linhas");
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
				.header("Content-Version", getApplicationVersion())
				.entity(serializar(tipoCursoService.listarTodos()))
				.build();
	}
	
	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_CERTIFICADO,
			PermissionConstants.TRABALHADOR_CERTIFICADO_CADASTRAR }))
	public Response criar(@Encoded TipoCurso tipoCurso, @Context SecurityContext context
			, @Context HttpServletRequest request) {
		LOGGER.debug("Criando Linhas");
		return Response.status(HttpServletResponse.SC_CREATED).entity(tipoCursoService
				.salvar(tipoCurso,ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO
						,Funcionalidade.TRABALHADOR))).type(MediaType.APPLICATION_JSON).build();
	}

}
