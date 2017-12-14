package br.com.ezvida.rst.web.endpoint.v1;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;

import br.com.ezvida.rst.enums.DadosArquetipo;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.service.RESService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.core.exception.BusinessException;
import fw.web.endpoint.SegurancaEndpoint;

@SuppressWarnings("rawtypes")
@RequestScoped
@Path("/private/v1/res")
public class RESEndpoint extends SegurancaEndpoint {

	private static final long serialVersionUID = 6125064740436604208L;
	
	@Inject
	private RESService service;

	@GET
	@Path("/paciente/{cpf}")
	@Produces(MediaType.APPLICATION_JSON)
	public Object buscarPaciente(@PathParam("cpf") String cpf, @Context SecurityContext context, @Context HttpServletRequest request) {
		return this.service.buscarDadosPaciente(cpf, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.RES));
	}

	@GET
	@Path("/historico")
	@Produces(MediaType.APPLICATION_JSON)
	public Object buscarHistoricoDeAtendimentos(@Context UriInfo uriInfo, @Context SecurityContext context, @Context HttpServletRequest request) {
	    return this.service.buscarHistoricoParaPaciente(
	            uriInfo.getQueryParameters().getFirst("cpf"),
				uriInfo.getQueryParameters().getFirst("de"), 
				uriInfo.getQueryParameters().getFirst("pagina"), 
				ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.RES),
				ClienteInfos.getUsuario(context));
	}

	@GET
	@Path("/encontro/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Object buscarEncontroMedico(@PathParam("identifier") String id, @Context SecurityContext context, @Context HttpServletRequest request) {
		return this.service.buscarEncontro(id, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.RES));
	}

	@GET
	@Path("/registro/{dado}")
	@Produces(MediaType.APPLICATION_JSON)
	//@Autorizacao(permissoes = @Permissao(value="trabalhador_consultar"))
	public Object buscarHistoricoDeDadoDoTrabalhador(@PathParam("dado") DadosArquetipo dado, @Context UriInfo uriInfo, @Context SecurityContext context, @Context HttpServletRequest request) {
		MultivaluedMap<String, String> query = uriInfo.getQueryParameters();

		if (query == null || query.isEmpty()) {
			throw new BusinessException("Nenhum parâmetro de busca informado");
		}

		validaParametrosObrigatorios(query);

		if (StringUtils.isNotEmpty(query.getFirst("de")) || 
	        StringUtils.isNotEmpty(query.getFirst("ate")) || 
	        StringUtils.isNotEmpty(query.getFirst("quantidade")) || 
	        StringUtils.isNotEmpty(query.getFirst("todos"))) {
			return service.buscarHistoricoRegistrosParaDado(dado, query, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.RES));
		} else {
			return service.buscarUltimoRegistroParaDado(query.getFirst("cpf"), dado, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.RES));
		}
	}

	private void validaParametrosObrigatorios(MultivaluedMap<String, String> query) {
		if (query.getFirst("cpf") == null || query.getFirst("cpf").isEmpty()) {
			throw new BusinessException("CPF não informado");
		}
	}

}
