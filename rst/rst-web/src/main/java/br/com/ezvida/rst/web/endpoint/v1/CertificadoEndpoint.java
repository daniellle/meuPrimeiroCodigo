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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.CertificadoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Certificado;
import br.com.ezvida.rst.service.CertificadoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/certificados")
public class CertificadoEndpoint extends SegurancaEndpoint<Certificado> {

	private static final long serialVersionUID = -2967645375737964569L;

	@Inject
	private CertificadoService certificadoService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_CERTIFICADO, PermissionConstants.TRABALHADOR_CERTIFICADO_CADASTRAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_ALTERAR, PermissionConstants.TRABALHADOR_CERTIFICADO_CONSULTAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_DESATIVAR }))
	public Response buscarPorId(@PathParam("id") long id, @Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(certificadoService.buscarPorId(id,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.TRABALHADOR))))
				.build();
	}

	@GET
	@Encoded
	@Path("/paginado")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_CERTIFICADO, PermissionConstants.TRABALHADOR_CERTIFICADO_CADASTRAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_ALTERAR, PermissionConstants.TRABALHADOR_CERTIFICADO_CONSULTAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_DESATIVAR }))
	public Response pesquisarPaginado(@BeanParam CertificadoFilter filter, @Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON).header("Content-Version", getApplicationVersion())
				.entity(serializar(certificadoService.pesquisarPaginado(filter))).build();
	}

	@POST
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_CERTIFICADO, PermissionConstants.TRABALHADOR_CERTIFICADO_CADASTRAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_TRAB_CADASTRAR }))
	public Response criar(@Encoded Certificado certificado, @Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(certificadoService.salvar(certificado,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO, Funcionalidade.TRABALHADOR)))
				.type(MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Encoded
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.TRABALHADOR_CERTIFICADO, PermissionConstants.TRABALHADOR_CERTIFICADO_ALTERAR,
			PermissionConstants.TRABALHADOR_CERTIFICADO_DESATIVAR, PermissionConstants.TRABALHADOR_CERTIFICADO_TRAB_CADASTRAR }))
	public Response alterar(@Encoded Certificado certificado, @Context SecurityContext context, @Context HttpServletRequest request) {
		return Response.status(HttpServletResponse.SC_CREATED)
				.entity(certificadoService.salvar(certificado,
						ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.TRABALHADOR)))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
