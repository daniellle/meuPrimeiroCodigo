package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.dto.RelatorioUsuarioDTO;
import br.com.ezvida.rst.service.RelatorioService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

@RequestScoped
@Path("/private/v1/relatorio")
public class RelatorioTesteEndpoint extends SegurancaEndpoint<RelatorioUsuarioDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioTesteEndpoint.class);

    @Inject
    private RelatorioService service;

//    @GET
//    @Encoded
//    @Path("/pdf")
//    @Produces("application/pdf")
//    @Consumes("application/json")
//    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CADASTRAR,
//        PermissionConstants.USUARIO_ALTERAR, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR,
//        PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
//        PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR}))
//    public Response gerarRelatorioPDF(@BeanParam UsuarioFilter usuarioFilter, @Context SecurityContext context, @Context HttpServletRequest request) {
//        LOGGER.debug("Gerando PDF por filtro");
//        byte[] file = service.gerarRelatorioPDF(usuarioFilter
//            , ClienteInfos.getDadosFilter(context)
//            , ClienteInfos.getClienteInfos(context, request
//                , TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS));
//        return Response.status(HttpServletResponse.SC_OK).type("application/pdf")
//            .header("Content-Lenght", file.length)
//            .header("Content-Version", getApplicationVersion())
//            .header("Content-Disposition",
//                "filename=usuarios.pdf")
//            .entity(file).build();
//    }

    @GET
    @Encoded
    @Path("/pdf")
    @Produces("application/pdf")
    @Consumes("application/json")
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CADASTRAR,
        PermissionConstants.USUARIO_ALTERAR, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR,
        PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
        PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR}))
    public Response gerarRelatorioPDF(@BeanParam UsuarioFilter usuarioFilter, @Context SecurityContext context, @Context HttpServletRequest request) {
        LOGGER.debug("Gerando PDF por filtro");
        byte[] file = service.gerarRelatorioPDF(usuarioFilter
            , ClienteInfos.getDadosFilter(context)
            , ClienteInfos.getClienteInfos(context, request
                , TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS));
        return Response.status(HttpServletResponse.SC_OK).type("application/pdf")
            .header("Content-Lenght", file.length)
            .header("Content-Version", getApplicationVersion())
            .header("Content-Disposition",
                "filename=usuarios.pdf")
            .entity(file).build();
    }

    @GET
    @Encoded
    @Path("/csv")
    @Produces("text/csv")
    @Consumes("application/json")
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CADASTRAR,
        PermissionConstants.USUARIO_ALTERAR, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR,
        PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
        PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR}))
    public Response gerarRelatorioCsv(@BeanParam UsuarioFilter usuarioFilter, @Context SecurityContext context, @Context HttpServletRequest request){

        LOGGER.debug("iniciando a geração do csv");

        byte[] file = service.gerarRelatorioCSV(usuarioFilter
            , ClienteInfos.getDadosFilter(context)
            , ClienteInfos.getClienteInfos(context, request
                , TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS));
        return Response.status(HttpServletResponse.SC_OK).type("text/csv")
            .header("Content-Lenght", file.length)
            .header("Content-Version", getApplicationVersion())
            .header("Content-Disposition",
                "attachment; filename=usuarios.csv")
            .entity(file).build();
    }
}
