package br.com.ezvida.rst.web.endpoint.v1;

import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.dto.PerfilUsuarioDTO;
import br.com.ezvida.rst.service.PerfilUsuarioService;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/private/v1/perfil-usuario")
public class PerfilUsuarioEndpoint extends SegurancaEndpoint<PerfilUsuarioDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerfilUsuarioEndpoint.class);

    @Inject
    private PerfilUsuarioService service;

    @GET
    @Encoded
    @Path("/paginado")
    @Produces("application/json")
    @Consumes("application/json")
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CADASTRAR, PermissionConstants.USUARIO_ALTERAR, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR, PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR, PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR}))
    public Response buscaPagina(@BeanParam UsuarioFilter usuarioFilter, @Context SecurityContext context, @Context HttpServletRequest request){
        LOGGER.debug("Inicio da busca paginada perfil x usuário");
        return Response.status(HttpServletResponse.SC_OK).type("application/json").header("Content-Version", getApplicationVersion()).entity(serializar(service.pesquisarPaginado(usuarioFilter, ClienteInfos.getDadosFilter(context), ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS)))).build();
    }


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
    @Path("/xls")
    @Produces("application/vnd.ms-excel")
    @Consumes("application/json")
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CADASTRAR,
        PermissionConstants.USUARIO_ALTERAR, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR,
        PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
        PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR}))
    public Response gerarRelatorioCsv(@BeanParam UsuarioFilter usuarioFilter, @Context SecurityContext context, @Context HttpServletRequest request){

        LOGGER.debug("iniciando a geração do csv");

        byte[] file = service.gerarRelatorioXLS(usuarioFilter
            , ClienteInfos.getDadosFilter(context)
            , ClienteInfos.getClienteInfos(context, request
                , TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS));
        return Response.status(HttpServletResponse.SC_OK).type("application/vnd.ms-excel")
            .header("Content-Lenght", file.length)
            .header("Content-Version", getApplicationVersion())
            .header("Content-Disposition",
                "attachment; filename=usuarios.xls")
            .entity(file).build();
    }
}
