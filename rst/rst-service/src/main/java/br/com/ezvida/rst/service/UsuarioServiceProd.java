package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.client.UsuarioClient;
import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.girst.apiclient.model.Perfil;
import br.com.ezvida.girst.apiclient.model.UsuarioPerfilSistema;
import br.com.ezvida.girst.apiclient.model.filter.UsuarioFilter;
import br.com.ezvida.rst.anotacoes.Prod;
import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UsuarioGirstViewDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.*;
import br.com.ezvida.rst.model.dto.UsuarioDTO;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fw.core.common.util.ResourceUtil;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Stateless
@Prod
public class UsuarioServiceProd extends BaseService implements UsuarioService {

    private static final long serialVersionUID = -4839541588378608503L;

    private static final Logger LOGGER = LoggerFactory.getLogger(CredencialService.class);

    private static final String CODIGO_PERFIL_GESTOR_EMPRESA = "GEEM";
    private static final String CODIGO_PERFIL_GESTOR_EMPRESA_MASTER = "GEEMM";
    private static final String CODIGO_PERFIL_DIRETOR_DR = "DIDR";
    private static final String CODIGO_PERFIL_GESTOR_DR_APLICACOES = "GDRA";
    private static final String CODIGO_PERFIL_GESTOR_DR_APLICACOES_MASTER = "GDRM";
    private static final String CODIGO_PERFIL_SUPERITENDENTE_DR= "SUDR";
    private static final String CODIGO_PERFIL_MEDICO_TRABALHADOR_DR = "MTSDR";
    private static final String CODIGO_PERFIL_GESTOR_COMERCIAL_DR = "GCDR";

    @Inject
    private UsuarioClient usuarioClient;

    @Inject
    private APIClientService apiClientService;

    @Inject
    private UsuarioEntidadeService usuarioEntidadeService;

    @Inject
    private TrabalhadorService trabalhadorService;

    @Inject
    private DepartamentoRegionalService departamentoRegionalService;

    @Inject
    private UsuarioGirstViewDAO usuarioGirstViewDAO;

    @Inject
    private EmpresaService empresaService;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Usuario getUsuario(String login) {
        LOGGER.debug("Obtendo usuario do girst");

        br.com.ezvida.girst.apiclient.model.Usuario usuarioGirst;

        try {
            usuarioGirst = usuarioClient.getPerfilUsuario(
                    apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), login,
                    apiClientService.getSistema());
        } catch (Exception e) {
            LOGGER.error("Erro ao obter usuário do girst. Login = " + login + ". Erro: " + e.getMessage(), e.getCause());
            throw e;
        }

        Usuario usuario = null;
        if (usuarioGirst != null) {
            LOGGER.debug("Criando usuario do rst");
            Integer hieraquia = getHieraquia(usuarioGirst);

            usuario = new Usuario(null, usuarioGirst.getLogin(), usuarioGirst.getNome(), null, null,
                    usuarioGirst.getEmail(), hieraquia);

            List<br.com.ezvida.girst.apiclient.model.Perfil> perfis = usuarioGirst.getPerfisSistema().stream()
                    .map(UsuarioPerfilSistema::getPerfil).collect(Collectors.toList());

            for (Perfil perfil : perfis) {
                usuario.getPapeis().add(perfil.getCodigo());
                if (perfil.getPerfilPermissoes() != null) {
                    usuario.getPermissoes().addAll(perfil.getPerfilPermissoes().stream()
                            .map(p -> p.getPermissao().getNome()).collect(Collectors.toList()));
                }
            }
            getEntidadesFiltradas(usuario);
        } else {
            LOGGER.info("O usuário do girst para o login = " + login + ", é null.");
        }

        return usuario;
    }

    private Integer getHieraquia(br.com.ezvida.girst.apiclient.model.Usuario usuarioGirst) {
        Integer hieraquia = null;
        for (UsuarioPerfilSistema ups: usuarioGirst.getPerfisSistema()) {
            if(ups.getPerfil() != null && ups.getPerfil().getHierarquia() != null
                    && (hieraquia == null || ups.getPerfil().getHierarquia() < hieraquia)){
                hieraquia = ups.getPerfil().getHierarquia();
            }
        }
        return hieraquia;
    }

    private void getEntidadesFiltradas(Usuario usuario) {
        List<UsuarioEntidade> usuariosEntidade = usuarioEntidadeService.pesquisarPorCPF(usuario.getLogin());

        if (usuario.getPapeis().contains(DadosFilter.TRABALHADOR)) {
            Trabalhador trabalhador = trabalhadorService.pesquisarPorCPF(usuario.getLogin());

            if (trabalhador != null) {
                usuario.getIdTrabalhadores().add(trabalhador.getId());
                usuario.setIdDepartamentos(departamentoRegionalService.buscarPorTrabalhador(trabalhador.getId())
                        .stream().map(DepartamentoRegional::getId).collect(Collectors.toSet()));
            }
        }

        for (UsuarioEntidade usuarioEntidade : usuariosEntidade) {
            if (usuarioEntidade.getDepartamentoRegional() != null) {
                usuario.getIdDepartamentos().add(usuarioEntidade.getDepartamentoRegional().getId());
            }

            if (usuarioEntidade.getEmpresa() != null) {
                usuario.getIdEmpresas().add(usuarioEntidade.getEmpresa().getId());
            }

            if (usuarioEntidade.getParceiro() != null) {
                usuario.getIdParceiros().add(usuarioEntidade.getParceiro().getId());
            }

            if (usuarioEntidade.getRedeCredenciada() != null) {
                usuario.getIdRedesCredenciadas().add(usuarioEntidade.getRedeCredenciada().getId());
            }

            if (usuarioEntidade.getSindicato() != null) {
                usuario.getIdSindicatos().add(usuarioEntidade.getSindicato().getId());
            }

            if (usuarioEntidade.getUnidadeAtendimentoTrabalhador() != null) {
                usuario.getIdUnidadesSESI().add(usuarioEntidade.getUnidadeAtendimentoTrabalhador().getId());
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, String> getConfiguracao(Ambiente ambiente) {

        Map<Ambiente, Map<String, String>> configuracoes;

        try (InputStream stream = this.getClass().getResourceAsStream("/ldap/configuracao.yaml")) {

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            configuracoes = mapper.readValue(stream,
                    mapper.getTypeFactory().constructMapType(Map.class, Ambiente.class, Map.class));

        } catch (Exception e) {
            throw new BusinessErrorException(ResourceUtil.getMensagem("app_dashboard_validacao_error"), e);
        }

        return configuracoes.get(ambiente);

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<br.com.ezvida.girst.apiclient.model.Usuario> pesquisarPaginado(br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter) {
        UsuarioFilter usuarioFilterClient = new UsuarioFilter();
        usuarioFilterClient.setLogin(usuarioFilter.getLogin());
        usuarioFilterClient.setNome(usuarioFilter.getNome());
        usuarioFilterClient.setPagina(usuarioFilter.getPagina());
        usuarioFilterClient.setQuantidadeRegistro(usuarioFilter.getQuantidadeRegistro());

        ListaPaginada<br.com.ezvida.girst.apiclient.model.Usuario> listaRetorno;

        try {
            listaRetorno = this.usuarioClient.pesquisarPaginado(apiClientService.getURL(),
                    apiClientService.getOAuthToken().getAccess_token(),
                    usuarioFilterClient);
        } catch (Exception e) {
            LOGGER.error("Erro ao pesquisar usuários paginado por filtro. Erro: " + e.getMessage(), e.getCause());
            throw e;
        }

        return listaRetorno;

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public br.com.ezvida.rst.dao.filter.ListaPaginada<UsuarioGirstView> pesquisarPaginadoGirst(
            br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria) {

        br.com.ezvida.rst.dao.filter.ListaPaginada<UsuarioGirstView> listaPagianda = usuarioGirstViewDAO
                .pesquisarPorFiltro(usuarioFilter, dados);
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de usuário por filtro: ", usuarioFilter);
        return listaPagianda;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public br.com.ezvida.girst.apiclient.model.Usuario buscarPorId(String id, ClienteAuditoria auditoria) {
        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "buscar usuário por id: " + id);
        }
        try {
            return this.usuarioClient.buscarUsuarioPorId(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), id);
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar usuário por id. Id = " + id + ". Erro: " + e.getMessage(), e.getCause());
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public br.com.ezvida.girst.apiclient.model.Usuario cadastrarUsuario(
            br.com.ezvida.girst.apiclient.model.Usuario usuario
            , Usuario usuarioLogado, ClienteAuditoria auditoria) {
        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "cadastro de usuário: ", usuario);
        }

        validarHierarquia(usuarioLogado, usuario);

        br.com.ezvida.girst.apiclient.model.Usuario u;

        try {
            u = this.usuarioClient.cadastrar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), usuario);
        } catch (Exception e) {
            LOGGER.error("Erro ao cadastrar usuário. Erro: " + e.getMessage(), e.getCause());
            throw e;
        }

        return u;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public br.com.ezvida.girst.apiclient.model.Usuario alterarUsuario(
            br.com.ezvida.girst.apiclient.model.Usuario usuario, Usuario usuarioLogado, ClienteAuditoria auditoria) {

        validarHierarquia(usuarioLogado, usuario);
        br.com.ezvida.girst.apiclient.model.Usuario usuarioAnterior = buscarPorId(usuario.getId().toString(), null);
        if (auditoria != null && usuarioAnterior != null && usuarioAnterior.getPerfisSistema() != null
                && usuarioAnterior.getPerfisSistema().size() > 0) {
            List<UsuarioPerfilSistema> perfisRemovidos = new ArrayList<>();
            for (UsuarioPerfilSistema itemUsrAnt : usuarioAnterior.getPerfisSistema()) {
                if (!usuario.getPerfisSistema().contains(itemUsrAnt)) {
                    perfisRemovidos.add(itemUsrAnt);
                }
            }
            auditoria.getTipoOperacao();

            if (auditoria.getTipoOperacao().getCodigo() == "2") {
                if (perfisRemovidos.size() > 0) {
                    for (UsuarioPerfilSistema perfil : perfisRemovidos) {
                        if (perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_EMPRESA)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_EMPRESA_MASTER)) {
                            List<UsuarioEntidade> listaEmpresasAssociadas = usuarioEntidadeService
                                    .pesquisarTodasEmpresasAssociadas(usuario.getLogin());
                            if (listaEmpresasAssociadas != null && listaEmpresasAssociadas.size() > 0) {
                                for (UsuarioEntidade item : listaEmpresasAssociadas) {
                                    usuarioEntidadeService.alterarUsuarioEntidade(item, auditoria);
                                }
                            }
                        }
                        if (perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_DIRETOR_DR)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_DR_APLICACOES)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_DR_APLICACOES_MASTER)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_SUPERITENDENTE_DR)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_MEDICO_TRABALHADOR_DR)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_COMERCIAL_DR)) {
                            List<UsuarioEntidade> listaDepartamentosAssociados = usuarioEntidadeService
                                    .pesquisarTodosDepartamentosAssociadas(usuario.getLogin());
                            if (listaDepartamentosAssociados != null && listaDepartamentosAssociados.size() > 0) {
                                for (UsuarioEntidade item : listaDepartamentosAssociados) {
                                    usuarioEntidadeService.alterarUsuarioEntidade(item, auditoria);
                                }
                            }
                        }
                    }
                }
            } else {
                if (perfisRemovidos.size() > 0) {
                    for (UsuarioPerfilSistema perfil : perfisRemovidos) {
                        if (perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_EMPRESA)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_EMPRESA_MASTER)) {
                            List<UsuarioEntidade> listaEmpresasAssociadas = usuarioEntidadeService
                                    .pesquisarTodasEmpresasAssociadas(usuario.getLogin());
                            if (listaEmpresasAssociadas != null && listaEmpresasAssociadas.size() > 0) {
                                for (UsuarioEntidade item : listaEmpresasAssociadas) {
                                    usuarioEntidadeService.desativarUsuarioEntidade(item, auditoria);
                                }
                            }
                        }
                        if (perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_DIRETOR_DR)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_DR_APLICACOES)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_DR_APLICACOES_MASTER)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_SUPERITENDENTE_DR)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_MEDICO_TRABALHADOR_DR)
                                || perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_COMERCIAL_DR)) {
                            List<UsuarioEntidade> listaDepartamentosAssociados = usuarioEntidadeService
                                    .pesquisarTodosDepartamentosAssociadas(usuario.getLogin());
                            if (listaDepartamentosAssociados != null && listaDepartamentosAssociados.size() > 0) {
                                for (UsuarioEntidade item : listaDepartamentosAssociados) {
                                    usuarioEntidadeService.desativarUsuarioEntidade(item, auditoria);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "Alteração do usuário: " + usuario);
        }

        br.com.ezvida.girst.apiclient.model.Usuario u;

        try {
            u = this.usuarioClient.alterar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), usuario);
            AtomicReference<Boolean> isTrabalhador = new AtomicReference<>(false);
            usuarioAnterior.getPerfisSistema().parallelStream().forEach(ups -> {
                if (ups.getPerfil().getCodigo().equalsIgnoreCase("TRA")) {
                    isTrabalhador.set(true);
                }
            });
            if (isTrabalhador.get()) {
                trabalhadorService.sicronizarUsuarioTrabalhador(usuario, auditoria);
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao alterar o usuário. Id = " + String.valueOf(usuario.getId()) + ". Erro: " + e.getMessage(), e.getCause());
            throw e;
        }

        return u;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public br.com.ezvida.girst.apiclient.model.Usuario sicronizarTrabalhadorUsuario(Trabalhador trabalhador, ClienteAuditoria auditoria) {
        br.com.ezvida.girst.apiclient.model.Usuario user = null;
        try {
            user = buscarPorLogin(trabalhador.getCpf());
        } catch (Exception e) {
            LOGGER.debug("Usuario não encontrado no GIRST", e);
        }

        if (user != null) {
            user.setNome(trabalhador.getNome());
            Set<EmailTrabalhador> listaEmail = trabalhador.getListaEmailTrabalhador();
            if (listaEmail != null) {
                Optional<EmailTrabalhador> opt = listaEmail.parallelStream().filter(
                        e -> e.getEmail().getNotificacao().equals(SimNao.SIM)).findFirst();
                if (opt.isPresent()) {
                    user.setEmail(opt.get().getEmail().getDescricao());
                }
                LogAuditoria.registrar(LOGGER, auditoria, "Sicronizando o usuário: " + user);
                try {
                    user = this.usuarioClient.alterar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), user);
                } catch (Exception e) {
                    LOGGER.error("Erro ao alterar o usuário. Id = " + String.valueOf(user.getId()) + ". Erro: " + e.getMessage(), e.getCause());
                    throw e;
                }
            }
        }
        return user;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public br.com.ezvida.girst.apiclient.model.Usuario alterarPerfilSenha(br.com.ezvida.girst.apiclient.model.UsuarioCredencial usuarioCredencial, ClienteAuditoria auditoria) {
        br.com.ezvida.girst.apiclient.model.Usuario u = new br.com.ezvida.girst.apiclient.model.Usuario();

        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "Alteração do perfil e senha do usuário: {}", usuarioCredencial);
            try {
                u = this.usuarioClient.alterarPerfilSenha(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), usuarioCredencial);
            } catch (Exception e) {
                LOGGER.error("Erro ao alterar o perfil do usuário. Id = " + String.valueOf(usuarioCredencial.getId()) + ". Erro: " + e.getMessage(), e.getCause());
                throw e;
            }
        }

        return u;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public br.com.ezvida.girst.apiclient.model.Usuario desativarUsuario(String id, Usuario usuarioLogado, ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "desativar usuário: " + id);

        br.com.ezvida.girst.apiclient.model.Usuario userRemov = buscarPorId(id, auditoria);
        validarHierarquia(usuarioLogado, userRemov);

        br.com.ezvida.girst.apiclient.model.Usuario u;
        try {
            u = this.usuarioClient.remover(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), id, null);
        } catch (Exception e) {
            LOGGER.error("Erro ao remover o usuário. Id = " + id + ". Erro: " + e.getMessage(), e.getCause());
            throw e;
        }

        return u;
    }

    private void validarHierarquia(Usuario usuarioLogado, br.com.ezvida.girst.apiclient.model.Usuario usuarioModicar) {
        if (usuarioLogado != null && usuarioModicar != null) {
            Integer hierarquiaMod = getHieraquia(usuarioModicar);

            if (usuarioLogado.getHierarquia() != null && hierarquiaMod != null
                    && usuarioLogado.getHierarquia() != 0 && hierarquiaMod < usuarioLogado.getHierarquia()) {
                throw new BusinessErrorException("Não é permitido modificar um usuario superior ao logado");
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public br.com.ezvida.girst.apiclient.model.Usuario buscarPorEmail(String email) {
        br.com.ezvida.girst.apiclient.model.Usuario u;
        try {
            u = this.usuarioClient.buscarPorEmail(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), email);
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar usuário por email. Email = " + email + ". Erro: " + e.getMessage(), e.getCause());
            throw e;
        }

        return u;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public br.com.ezvida.girst.apiclient.model.Usuario buscarPorLogin(String login) {
        br.com.ezvida.girst.apiclient.model.Usuario u;

        try {
            u = this.usuarioClient.getPerfilUsuario(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), login, null);
        } catch (Exception e) {
            LOGGER.error("Erro ao obter o perfil do usuário por login. Login = " + login + ". Erro: " + e.getMessage(), e.getCause());
            throw e;
        }


        return u;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean isAdm(String cpf) {
        br.com.ezvida.girst.apiclient.model.Usuario usuario = buscarPorLogin(cpf);

        if (usuario != null) {
            for (UsuarioPerfilSistema perfilSistema : usuario.getPerfisSistema()) {
                if (perfilSistema.getPerfil() != null
                        && perfilSistema.getPerfil().getCodigo().equals(DadosFilter.ADMINISTRADOR)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public UsuarioDTO consultarDadosUsuario(String login) {
        LOGGER.debug("consultando dados do usuario");
        Usuario usuarioAConsultar = getUsuario(login);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setLogin(login);

        if (usuarioAConsultar.getPapeis().contains(DadosFilter.TRABALHADOR)) {
            usuario.setEmpresas(empresaService.buscarEmpresasUatsDrsPorCpf(login));
            Trabalhador trabalhador = trabalhadorService.buscarPorCpf(login);
            usuario.setTipoImagem(trabalhador.getTipoImagem());
            usuario.setImagem(trabalhador.getImagem());
        } else {
            if(usuarioAConsultar.getPapeis().contains(DadosFilter.DIRETOR_DN) || usuarioAConsultar.getPapeis().contains(DadosFilter.GESTOR_DN)
            || usuarioAConsultar.getPapeis().contains(DadosFilter.GESTOR_CONTEUDO_DN) || usuarioAConsultar.getPapeis().contains(DadosFilter.MEDICO_TRABALHO_DN)){
                usuario.setDepartamentosRegionais(departamentoRegionalService.buscarDNPorSigla());
            }
            else {
                usuario.setDepartamentosRegionais(departamentoRegionalService.pesquisarPorIds(usuarioAConsultar.getIdDepartamentos()));
                usuario.setEmpresas(empresaService.buscarEmpresasUatsDrsPorIds(usuarioAConsultar.getIdEmpresas()));
            }
        }

        br.com.ezvida.girst.apiclient.model.Usuario usuarioGirst = buscarPorLogin(login);
        if (usuarioGirst != null) {
            usuario.setExibirApelido(usuarioGirst.getExibirApelido());
            usuario.setApelido(usuarioGirst.getApelido());
            usuario.setFotoPerfil(usuarioGirst.getFoto());
        }

        return usuario;
    }
}