package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.InformacaoSaude;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.service.RESService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.core.exception.BusinessException;
import fw.web.endpoint.SegurancaEndpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
@RequestScoped
@Path("/private/v1/saude")
public class RESEndpoint extends SegurancaEndpoint {

    private static final long serialVersionUID = 6125064740436604208L;

    @Inject
    private RESService service;

    @GET
    @Path("/paciente/{cpf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object buscarPaciente(@PathParam("cpf") String cpf, @Context SecurityContext context,
                                 @Context HttpServletRequest request) {
        return this.service.buscarPaciente(cpf, ClienteInfos.getClienteInfos(context, request,
            TipoOperacaoAuditoria.CONSULTA,
            Funcionalidade.RES));
    }

    @GET
    @Path("/fichaMedica")
    @Produces(MediaType.APPLICATION_JSON)
    public Object buscarHistoricoDeAtendimentos(@Context UriInfo uriInfo, @Context SecurityContext context,
                                                @Context HttpServletRequest request) {
        return this.service.buscarFichasMedicasParaPaciente(
            uriInfo.getQueryParameters()
                .getFirst("cpf"),
            uriInfo.getQueryParameters()
                .getFirst("de"),
            uriInfo.getQueryParameters()
                .getFirst("pagina"),
            ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.RES),
            ClienteInfos.getUsuario(context));
    }

    @GET
    @Path("/fichaMedica/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object buscarEncontroMedico(@PathParam("identifier") String id, @Context SecurityContext context,
                                       @Context HttpServletRequest request) {
        return this.service.buscarFichaMedica(id, ClienteInfos.getClienteInfos(context, request,
            TipoOperacaoAuditoria.CONSULTA,
            Funcionalidade.RES));
    }

    @GET
    @Path("/informacao/historico")
    @Produces(MediaType.APPLICATION_JSON)
    public Object buscarHistoricoDeInformacaoSaudeTrabalhador(
                                                              @Context UriInfo uriInfo,
                                                              @Context SecurityContext context,
                                                              @Context HttpServletRequest request) {

        MultivaluedMap<String, String> query = uriInfo.getQueryParameters();

        if (query == null || query.isEmpty()) {
            throw new BusinessException("Nenhum parâmetro de busca informado");
        }

        validaParametrosObrigatorios(query);
        if (query.get("informacao") == null || query.get("informacao").isEmpty()) {
            throw new BusinessException("Nenhuma informação de saúde informada");
        }
        List<String> informacoes = query.get("informacao");

        try {
            List<InformacaoSaude> informacoesSaude = informacoes.stream()
                .map(InformacaoSaude::fromString)
                .collect(Collectors.toList());
            ClienteAuditoria infos = ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA,
                Funcionalidade.RES);
            return service.buscarHistoricoParaInformacaoSaude(informacoesSaude, query, infos);

        } catch (IllegalArgumentException e) {
            throw new BusinessException("Uma das informações de saúde buscada não é conhecida");
        }
    }

    private void validaParametrosObrigatorios(MultivaluedMap<String, String> query) {
        if (query.getFirst("cpf") == null || query.getFirst("cpf")
            .isEmpty()) {
            throw new BusinessException("CPF não informado");
        }
    }

    @GET
    @Path("/informacao")
    @Produces(MediaType.APPLICATION_JSON)
    public Object buscarInformacaoSaudeTrabalhador(@Context UriInfo uriInfo,
                                                   @Context SecurityContext context,
                                                   @Context HttpServletRequest request) {
        MultivaluedMap<String, String> query = uriInfo.getQueryParameters();

        if (query == null || query.isEmpty()) {
            throw new BusinessException("Nenhum parâmetro de busca informado");
        }

        validaParametrosObrigatorios(query);
        if (query.get("informacao") == null || query.get("informacao").isEmpty()) {
            throw new BusinessException("Nenhuma informação de saúde informada");
        }
        List<String> informacoes = query.get("informacao");

        try {
            List<InformacaoSaude> informacoesSaude = informacoes.stream()
                .map(InformacaoSaude::fromString)
                .collect(Collectors.toList());

            ClienteAuditoria clienteAuditoria = ClienteInfos.getClienteInfos(context, request,
                TipoOperacaoAuditoria.CONSULTA,
                Funcionalidade.RES);
            return service.buscarUltimoRegistroInformacaoSaude(query.getFirst("cpf"),
                informacoesSaude,
                clienteAuditoria);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Uma das informações de saúde buscada não é conhecida");
        }

    }

}
