package br.com.ezvida.rst.web.endpoint.v1;

import java.nio.charset.StandardCharsets;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.constants.PermissionConstants;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.service.TrabalhadorService;
import br.com.ezvida.rst.service.UsuarioService;
import br.com.ezvida.rst.web.auditoria.ClienteInfos;
import fw.security.binding.Autorizacao;
import fw.security.binding.Permissao;
import fw.web.endpoint.SegurancaEndpoint;

@RequestScoped
@Path("/private/v1/usuarios")
public class UsuarioEndpoint extends SegurancaEndpoint<Usuario> {

    private static final String CONTENT_VERSION = "Content-Version";

	private static final long serialVersionUID = -3618279439764362012L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioEndpoint.class);

    @Inject
    @Preferencial
    private UsuarioService usuarioService;

    @Inject
    private TrabalhadorService trabalhadorService;

    //@formatter:off
    @GET
    @Encoded
    @Path("login/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO,
        PermissionConstants.USUARIO_CADASTRAR, PermissionConstants.USUARIO_ALTERAR,
        PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR}))
    //@formatter:on
    public Response getUsuario(@Context HttpServletRequest request, @Encoded @PathParam("login") String login) {

        LOGGER.debug("Carregando usuário {}", login);

        getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        return Response.status(HttpServletResponse.SC_OK)
            .type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(usuarioService.getUsuario(login))).build();
    }

    @GET
    @Encoded
    @Path("/paginado")
    @Produces("application/json")
    @Consumes("application/json")
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CADASTRAR,
        PermissionConstants.USUARIO_ALTERAR, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR,
        PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR, PermissionConstants.USUARIO_ENTIDADE_ALTERAR,
        PermissionConstants.USUARIO_ENTIDADE_CONSULTAR, PermissionConstants.USUARIO_ENTIDADE_DESATIVAR}))
    public Response pesquisarPaginado(@BeanParam UsuarioFilter usuarioFilter, @Context SecurityContext context, @Context HttpServletRequest request) {
        LOGGER.debug("Buscando Usuários por filtro e paginado");
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(usuarioService.pesquisarPaginadoGirst(usuarioFilter
                , ClienteInfos.getDadosFilter(context)
                , ClienteInfos.getUsuario(context)
                , ClienteInfos.getClienteInfos(context, request
                    , TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS)))).build();
    }

    @GET
    @Encoded
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_ALTERAR,
        PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_DESATIVAR,
        PermissionConstants.USUARIO_ENTIDADE, PermissionConstants.USUARIO_ENTIDADE_CADASTRAR,
        PermissionConstants.USUARIO_ENTIDADE_ALTERAR, PermissionConstants.USUARIO_ENTIDADE_CONSULTAR,
        PermissionConstants.USUARIO_ENTIDADE_DESATIVAR}))
    public Response buscarPorId(@QueryParam("id") String id, @Context SecurityContext context
        , @Context HttpServletRequest request) {
		br.com.ezvida.girst.apiclient.model.Usuario usuario = usuarioService.buscarPorId(id,
				ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA, Funcionalidade.USUARIOS));
        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
				.entity(serializar(usuario)).build();
    }

    @POST
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CADASTRAR}))
    public Response criar(@Encoded br.com.ezvida.girst.apiclient.model.Usuario usuario, @Context SecurityContext context,
                          @Context HttpServletRequest request) {
        Usuario usuarioLogado = ClienteInfos.getUsuario(context);
        trabalhadorService.incluirPerfilTrabalhadorUsuario(usuario);
        LOGGER.debug("Criando usuário {}", usuarioLogado.getLogin());
        return Response.status(HttpServletResponse.SC_CREATED).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(usuarioService.cadastrarUsuario(usuario
                , usuarioLogado, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.INCLUSAO
                    , Funcionalidade.USUARIOS)))).build();
    }

    @PUT
    @Encoded
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_ALTERAR}))
    public Response alterar(@Encoded br.com.ezvida.girst.apiclient.model.Usuario usuario, @Context SecurityContext context,
                            @Context HttpServletRequest request) {
        Usuario usuarioLogado = ClienteInfos.getUsuario(context);
        LOGGER.debug("Alterando usuário {}", usuarioLogado.getLogin());
        return Response.status(HttpServletResponse.SC_OK).entity(serializar(usuarioService.alterarUsuario(usuario,
            usuarioLogado, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.USUARIOS))))
            .header(CONTENT_VERSION, getApplicationVersion())
            .type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Encoded
    @Path("/perfil")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.USUARIO_ALTERAR, PermissionConstants.TRABALHADOR, PermissionConstants.TRABALHADOR_CONSULTAR,
        PermissionConstants.PERFIL, PermissionConstants.PERFIL_CONSULTAR}))
    public Response alterarPerfil(@Context SecurityContext context,
                                  @Context HttpServletRequest request, @Encoded br.com.ezvida.girst.apiclient.model.UsuarioCredencial usuarioCredencial) {
        LOGGER.debug("Atualizando o perfil e senha do usuario");
        getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        return Response.status(HttpServletResponse.SC_OK).entity(serializar(usuarioService.alterarPerfilSenha(usuarioCredencial, ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.ALTERACAO, Funcionalidade.USUARIOS))))
            .header(CONTENT_VERSION, getApplicationVersion())
            .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/buscarperfil")
    @Produces(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_CONSULTAR, PermissionConstants.TRABALHADOR, PermissionConstants.TRABALHADOR_CONSULTAR,
        PermissionConstants.PERFIL, PermissionConstants.PERFIL_CONSULTAR}))
    public Response getPerfilByLogin(@Context HttpServletRequest request, @Context SecurityContext context) {

        Usuario usuarioLogado = ClienteInfos.getUsuario(context);
        LOGGER.debug("Carregando usuário {}", usuarioLogado.getLogin());

        getResponse().setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        br.com.ezvida.girst.apiclient.model.Usuario usuarioGirst = usuarioService.buscarPorLogin(usuarioLogado.getLogin());

        return Response.status(HttpServletResponse.SC_OK).type(MediaType.APPLICATION_JSON)
            .header(CONTENT_VERSION, getApplicationVersion())
            .entity(serializar(usuarioService.buscarPorId(usuarioGirst.getId().toString()
                , ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.CONSULTA
                    , Funcionalidade.USUARIOS)))).build();
    }

    @DELETE
    @Encoded
    @Path("/desativar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Autorizacao(permissoes = @Permissao(value = {PermissionConstants.USUARIO, PermissionConstants.USUARIO_DESATIVAR}))
    public Response remover(@Context HttpServletRequest request, @Encoded @QueryParam("id") String id, @Context SecurityContext context) {
        Usuario usuarioLogado = ClienteInfos.getUsuario(context);
        LOGGER.debug("Removendo usuário {}{}", usuarioLogado.getLogin(), id);
        return Response.status(HttpServletResponse.SC_OK)
            .entity(serializar(usuarioService.desativarUsuario(id, usuarioLogado
                , ClienteInfos.getClienteInfos(context, request, TipoOperacaoAuditoria.DESATIVACAO, Funcionalidade.USUARIOS))))
            .header(CONTENT_VERSION, getApplicationVersion())
            .type(MediaType.APPLICATION_JSON).build();
    }

}
