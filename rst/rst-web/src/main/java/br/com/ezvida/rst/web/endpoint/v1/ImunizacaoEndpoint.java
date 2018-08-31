package br.com.ezvida.rst.web.endpoint.v1;


import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.ImunizacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.model.dto.DoseDTO;
import br.com.ezvida.rst.model.dto.ProximaDoseDTO;
import br.com.ezvida.rst.model.dto.VacinaAutodeclaradaDTO;
import br.com.ezvida.rst.service.ImunizacaoService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@SuppressWarnings("rawtypes")
@RequestScoped
@Path("/private/v1/vacinas-autodeclaradas/")
public class ImunizacaoEndpoint extends SegurancaEndpoint {

	private static final long serialVersionUID = -4386644877611540846L;

	@Inject
    private ImunizacaoService service;

    @GET
    @Path("{id}/")
    @Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_CONSULTAR }))
	public VacinaAutodeclaradaDTO buscarVacinaAutodeclaradaPorID(@PathParam("id") int id, @Context SecurityContext context,
                                       @Context HttpServletRequest request) {
        return this.service.buscarVacinaAutodeclaradaPorID(id,ClienteInfos.getUsuario(context));
    }

    @GET
    @Path("/ultimas-vacinas/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_CONSULTAR }))
    public List<VacinaAutodeclaradaDTO> buscarUltimasVacinas(@PathParam("limit") int limite,@Context SecurityContext context,
                                               @Context HttpServletRequest request) {
        return this.service.buscarUltimasVacinas(limite, ClienteInfos.getUsuario(context));
    }

    @GET
	@Path("historico/")
    @Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_CONSULTAR }))
	public ListaPaginada<VacinaAutodeclaradaDTO> buscarHistoricoVacinasAutodeclaradas(@Context HttpServletRequest request,
			@Context SecurityContext context, @BeanParam ImunizacaoFilter imunizacaoFilter) {

		Usuario usuario = ClienteInfos.getUsuario(context);

		return this.service.buscarHistoricoVacinasAutodeclaradas(usuario.getLogin(), imunizacaoFilter);
    }

    @POST
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_CADASTRAR }))
	public VacinaAutodeclaradaDTO adicionar(@Encoded VacinaAutodeclaradaDTO vacina, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        return this.service.salvarVacinaAutodeclarada(vacina, ClienteInfos.getUsuario(context));
    }

    @PUT
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_ALTERAR }))
	public VacinaAutodeclaradaDTO editar(@Encoded VacinaAutodeclaradaDTO vacina, @Context SecurityContext context
        , @Context HttpServletRequest request) {
        return this.service. editarVacinasAutoDeclaradas(vacina, ClienteInfos.getUsuario(context));
    }

    @DELETE
    @Path("{id}/")
    @Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_DESATIVAR }))
	public VacinaAutodeclaradaDTO deletar(@PathParam("id") int id, @Context SecurityContext context, @Context HttpServletRequest request)
    {
        return this.service.removerVacinaAutodeclarada(id,  ClienteInfos.getUsuario(context));
    }

    @GET
    @Path("/ultimas-proximas-doses/")
    @Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_CONSULTAR }))
	public List<DoseDTO> buscarUltimasProximasDoses(@Context SecurityContext context, @Context HttpServletRequest request) {
		return this.service.buscarProximasDosesMaisRecentes(ClienteInfos.getUsuario(context));
	}

	@GET
	@Path("/mensal/{month}/{year}")
	@Produces(MediaType.APPLICATION_JSON)
	@Autorizacao(permissoes = @Permissao(value = { PermissionConstants.VACINA_AUTODECLARADA, PermissionConstants.VACINA_AUTODECLARADA_CONSULTAR }))
	public List<DoseDTO> buscarVacinasProximaDosesMensal(@Context SecurityContext context, @Context HttpServletRequest request,
			@PathParam("month") Integer month, @PathParam("year") Integer year) {

		Usuario usuario = ClienteInfos.getUsuario(context);

		return this.service.buscarVacinasProximaDosesMensal(usuario.getLogin(), month, year);
    }

}
