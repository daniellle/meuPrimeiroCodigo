package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.model.Perfil;
import br.com.ezvida.girst.apiclient.model.Sistema;
import br.com.ezvida.girst.apiclient.model.Usuario;
import br.com.ezvida.girst.apiclient.model.UsuarioPerfilSistema;
import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaTrabalhadorLotacaoDAO;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.enums.*;
import br.com.ezvida.rst.model.*;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Stateless
public class TrabalhadorService extends BaseService {

    private static final long serialVersionUID = 6186912314459313987L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorService.class);

    private static final String CODIGO_SISTEMA_CADASTRO = "cadastro";

    private static final String CODIGO_SISTEMA_SESI_VIVA_MAIS_MOBILE = "vivamaismobile";

    @Inject
    private TrabalhadorDAO trabalhadorDAO;

    @Inject
    private EmpresaTrabalhadorLotacaoDAO empresaTrabalhadorLotacaoDAO;

    @Inject
    private TelefoneTrabalhadorService telefoneTrabalhadorService;

    @Inject
    private EmailTrabalhadorService emailTrabalhadorService;

    @Inject
    private EnderecoTrabalhadorService enderecoTrabalhadorService;

    @Inject
    private TrabalhadorDependenteService trabalhadorDependenteService;

    @Inject
    private UsuarioEntidadeService usuarioEntidadeService;

    @Inject
    private EmpresaTrabalhadorLotacaoService empresaTrabalhadorLotacaoService;

    @Inject
    @Preferencial
    private UsuarioService usuarioService;

    @Inject
    private SolicitacaoEmailService solicitacaoEmailService;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Trabalhador buscarPorId(TrabalhadorFilter trabalhadorFilter, ClienteAuditoria auditoria,
                                   DadosFilter dadosFilter) {

        if (trabalhadorFilter == null || trabalhadorFilter.getId() == null) {
            throw new BusinessErrorException("Id de consulta está nulo.");
        }

        Trabalhador trabalhador = trabalhadorDAO.pesquisarPorId(trabalhadorFilter, dadosFilter);

        if (trabalhador == null) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

        if (!trabalhadorFilter.isAplicarDadosFilter() && !auditoria.getUsuario().equals(trabalhador.getCpf())) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

        if (trabalhador != null) {
            trabalhador.setListaTelefoneTrabalhador(
                    telefoneTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
            trabalhador.setListaEmailTrabalhador(emailTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
            trabalhador.setListaEnderecoTrabalhador(
                    enderecoTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
        }
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de trabalhador por id: " + trabalhadorFilter.getId());
        return trabalhador;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean buscarTrabalhadorVidaAtiva(String cpf) {
        return empresaTrabalhadorLotacaoDAO.validarTrabalhador(cpf);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Trabalhador buscarTrabalhadorPrimeiroAcesso(String cpf, String dataNascimento) {
        Usuario u = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = (Date) formatter.parse(dataNascimento);
        } catch (ParseException e) {
            throw new BusinessErrorException(getMensagem("app_rst_validacao_error"));
        }

        Trabalhador trabalhador = trabalhadorDAO.pesquisarPorCpfDataNascimento(cpf, date);

        if (trabalhador == null) {
            throw new BusinessErrorException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

        if (trabalhador.getId() != null && trabalhador.getTermo() != null
                && trabalhador.getTermo().equals(SimNao.SIM)) {
            throw new BusinessErrorException(getMensagem("app_rst_trabalhador_ja_rezlizou_primeiro_acesso"));
        }

        try{
            empresaTrabalhadorLotacaoService.validarTrabalhadorPrimeiroAcesso(trabalhador.getCpf());
        }
        catch (Exception e){
            throw new BusinessErrorException(getMensagem("app_rst_primeiro_acesso_erro_vida_inativa"));
        }

        try{
           u = usuarioService.buscarPorLogin(trabalhador.getCpf());
        }
        catch (Exception e){
            if (trabalhador != null) {
                trabalhador.setListaTelefoneTrabalhador(
                        telefoneTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
                trabalhador.setListaEmailTrabalhador(emailTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
                trabalhador.setListaEnderecoTrabalhador(
                        enderecoTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
            }
        }

        if(trabalhador.getId() != null && u != null){
            throw new BusinessErrorException(getMensagem("app_rst_autenticacao_ja_possui_usuario"));
        }



        return trabalhador;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Trabalhador> listarTodos() {
        return trabalhadorDAO.listarTodos();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<Trabalhador> pesquisarPaginado(TrabalhadorFilter trabalhadorFilter, ClienteAuditoria auditoria,
                                                        DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de trabalhador por filtro: ", trabalhadorFilter);
        return pesquisarPaginado(trabalhadorFilter, dados);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<Trabalhador> pesquisarPaginado(TrabalhadorFilter trabalhadorFilter, DadosFilter dados) {
        return trabalhadorDAO.pesquisarPaginado(trabalhadorFilter, dados);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Trabalhador salvar(Trabalhador trabalhador, ClienteAuditoria auditoria) {
        Boolean hasNewTrabalhador = true;
        if (trabalhador.getId() != null) {
            hasNewTrabalhador = false;
        }

        trabalhador.setDescricaoAlergias(StringUtils.trimToNull(trabalhador.getDescricaoAlergias()));
        trabalhador.setDescricaoVacinas(StringUtils.trimToNull(trabalhador.getDescricaoVacinas()));
        trabalhador.setDescricaoMedicamentos(StringUtils.trimToNull(trabalhador.getDescricaoMedicamentos()));

        validar(trabalhador, auditoria);
        trabalhadorDAO.salvar(trabalhador);
        emailTrabalhadorService.salvar(trabalhador.getListaEmailTrabalhador(), trabalhador);
        telefoneTrabalhadorService.salvar(trabalhador.getListaTelefoneTrabalhador(), trabalhador);
        enderecoTrabalhadorService.salvar(trabalhador.getListaEnderecoTrabalhador(), trabalhador);

        if (!hasNewTrabalhador) {
            usuarioService.sicronizarTrabalhadorUsuario(trabalhador, auditoria);
            LogAuditoria.registrar(LOGGER, auditoria, "Alteração no cadastro de trabalhador: ", trabalhador);
        } else {
            LogAuditoria.registrar(LOGGER, auditoria, "Cadastro de Profissional: ", trabalhador);
        }

        return trabalhador;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Trabalhador sicronizarUsuarioTrabalhador(br.com.ezvida.girst.apiclient.model.Usuario usuario, ClienteAuditoria auditoria) {
        Trabalhador trabalhador = pesquisarPorCPF(usuario.getLogin());
        trabalhador.setNome(usuario.getNome());
        trabalhadorDAO.salvar(trabalhador);
        emailTrabalhadorService.salvar(incluirEmailTrabalhador(usuario, trabalhador), trabalhador);
        return trabalhador;
    }

    private Set<EmailTrabalhador> incluirEmailTrabalhador(Usuario usuario, Trabalhador trabalhador) {
        Set<EmailTrabalhador> listaEmail = trabalhador.getListaEmailTrabalhador();
        listaEmail.parallelStream().forEach(e -> e.getEmail().setNotificacao(SimNao.NAO));

        Optional<EmailTrabalhador> optEmail = listaEmail.parallelStream().filter(e -> e.getEmail().getDescricao().equalsIgnoreCase(usuario.getEmail())).findFirst();
        if (optEmail.isPresent()) {
            listaEmail.remove(optEmail.get());
            optEmail.get().getEmail().setDescricao(usuario.getEmail());
            optEmail.get().getEmail().setNotificacao(SimNao.SIM);
            listaEmail.add(optEmail.get());
        } else {
            Email email = new Email();
            email.setNotificacao(SimNao.SIM);
            email.setDescricao(usuario.getEmail());
            email.setTipo(TipoEmail.TRABALHO);
            EmailTrabalhador emailTrabalhador = new EmailTrabalhador();
            emailTrabalhador.setTrabalhador(trabalhador);
            emailTrabalhador.setEmail(email);

            listaEmail.add(emailTrabalhador);
        }

        return listaEmail;
    }

    private Usuario buscarTrabalhadorJaCadastrado(String login) {
        try {
            return usuarioService.buscarPorLogin(login);
        } catch (Exception e) {
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PrimeiroAcesso salvarPrimeiroAcesso(PrimeiroAcesso primeiroAcesso) {

        Trabalhador trab = trabalhadorDAO.pesquisarPorId(primeiroAcesso.getTrabalhador().getId());

        if (trab != null) {
            if (trab.getDataFalecimento() != null) {
                throw new BusinessErrorException(getMensagem("app_rst_primeiro_acesso_erro_data_falecimento"));
            }
            String id = trab.getId().toString();

            if (buscarTrabalhadorVidaAtiva(trab.getCpf())) {
                throw new BusinessErrorException(getMensagem("app_rst_primeiro_acesso_erro_vida_inativa"));
            }

            definirPerfilSistema(primeiroAcesso);

            Usuario usuario = buscarTrabalhadorJaCadastrado(trab.getCpf());
            if (usuario == null) {
                usuario = usuarioService.cadastrarUsuario(primeiroAcesso.getUsuario(), null, null);

                if (usuario == null) {
                    throw new BusinessErrorException(getMensagem("app_validacao_error"));
                }

            } else {
                if (!verificarPerfilSistemaPrimeiroAcesso(usuario)) {
                    usuario.getPerfisSistema().add(primeiroAcesso.getUsuario().getPerfisSistema().iterator().next());
                }

                validarEmailPrimeiroAcesso(primeiroAcesso, usuario);
                usuario.setSenha(primeiroAcesso.getUsuario().getSenha());
                usuario.setEmail(primeiroAcesso.getUsuario().getEmail());

                usuarioService.alterarUsuario(usuario, null, getAuditoriaNovoUsuario(usuario));
            }

            trab.setTermo(SimNao.SIM);
            trabalhadorDAO.salvar(trab);
            emailTrabalhadorService.salvar(primeiroAcesso.getTrabalhador().getListaEmailTrabalhador(), trab);
            telefoneTrabalhadorService.salvar(primeiroAcesso.getTrabalhador().getListaTelefoneTrabalhador(), trab);
        }
        return primeiroAcesso;
    }

    private void definirPerfilSistema(PrimeiroAcesso primeiroAcesso) {
        Perfil perfil = new Perfil();
        perfil.setCodigo(DadosFilter.TRABALHADOR);

        Sistema sistema = new Sistema();
        sistema.setCodigo(CODIGO_SISTEMA_CADASTRO);

        Sistema sistemaMobile = new Sistema();
        sistemaMobile.setCodigo(CODIGO_SISTEMA_SESI_VIVA_MAIS_MOBILE);

        UsuarioPerfilSistema usuarioPerfilSistema = new UsuarioPerfilSistema();
        usuarioPerfilSistema.setPerfil(perfil);
        usuarioPerfilSistema.setSistema(sistema);

        primeiroAcesso.getUsuario().setPerfisSistema(new HashSet<UsuarioPerfilSistema>());
        primeiroAcesso.getUsuario().getPerfisSistema().add(usuarioPerfilSistema);

        UsuarioPerfilSistema usuarioPerfilSistemaMobile = new UsuarioPerfilSistema();
        usuarioPerfilSistemaMobile.setPerfil(perfil);
        usuarioPerfilSistemaMobile.setSistema(sistemaMobile);
        primeiroAcesso.getUsuario().getPerfisSistema().add(usuarioPerfilSistemaMobile);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Trabalhador pesquisarPorCPF(String cpf) {
        List<Trabalhador> trabalhadores = trabalhadorDAO.pesquisarPorCPF(Arrays.asList(cpf));
        return !trabalhadores.isEmpty() ? trabalhadores.get(0) : null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Trabalhador buscarPorCpf(String cpf) {
        LOGGER.debug("buscando trabalhador por cpf");

        Trabalhador trabalhador = trabalhadorDAO.pesquisarPorCpf(cpf);

        if (trabalhador == null) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

        return trabalhador;
    }

    private void validar(Trabalhador trabalhador, ClienteAuditoria auditoria) {
        Trabalhador trabalhadorRetorno = pesquisarPorCPF(trabalhador.getCpf());
        if (trabalhadorRetorno != null && !trabalhadorRetorno.getId().equals(trabalhador.getId())) {
            throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
                    getMensagem("app_rst_label_trabalhador"), getMensagem("app_rst_label_cpf")));
        }

        if (CollectionUtils.isNotEmpty(trabalhador.getListaEmailTrabalhador())) {
            for (EmailTrabalhador emailTrabalhador : trabalhador.getListaEmailTrabalhador()) {
                Set<EmailTrabalhador> emailTrabalhadorRetorno = emailTrabalhadorService
                        .pesquisarPorEmail(emailTrabalhador.getEmail().getDescricao());
                if (emailTrabalhadorRetorno != null && CollectionUtils.isNotEmpty(emailTrabalhadorRetorno)
                        && !emailTrabalhadorRetorno.iterator().next().getTrabalhador().getId()
                        .equals(trabalhador.getId())) {
                    throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
                            getMensagem("app_rst_label_trabalhador"), getMensagem("app_rst_label_email")));
                }
            }
        }

        if (trabalhador.getCpf() != null) {
            TrabalhadorDependente trabalhadorDependente = trabalhadorDependenteService
                    .pesquisarDependentePorCPF(trabalhador.getCpf(), auditoria);
            if (trabalhadorDependente != null && SimNao.NAO.equals(trabalhadorDependente.getInativo())) {
                throw new BusinessErrorException(getMensagem("app_rst_cpf_dependente_do_trabalhador",
                        adicionarMascaraCpf(trabalhadorDependente.getTrabalhador().getCpf()),
                        trabalhadorDependente.getTrabalhador().getNome()));

            }
        }

        if (StringUtils.isNotEmpty(trabalhador.getCpf()) && !ValidadorUtils.isValidCPF(trabalhador.getCpf())) {
            throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cpf")));
        }

        if (StringUtils.isNotEmpty(trabalhador.getNit()) && !ValidadorUtils.validarNit(trabalhador.getNit())) {
            throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit")));
        }

        if (trabalhador.getDataNascimento() != null && trabalhador.getDataNascimento().after(new Date())) {
            throw new BusinessErrorException(
                    getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_label_data_nascimento")));
        }
    }

    private void validarEmailPrimeiroAcesso(PrimeiroAcesso primeiroAcesso, Usuario usuario) {
        String emailInformado = primeiroAcesso.getUsuario().getEmail();
        String emailCadastrado = usuario.getEmail();
        if (emailInformado == null || emailCadastrado == null) {
            throw new BusinessErrorException(getMensagem("app_rst_validacao_error", emailCadastrado, emailInformado));
        }
        if (!emailInformado.equalsIgnoreCase(emailCadastrado)) {
            throw new BusinessErrorException(
                    getMensagem("app_rst_email_invalido", getMensagem("app_rst_label_email")));
        }
    }

    private String adicionarMascaraCpf(String cpf) {
        StringBuilder sBuilder = new StringBuilder(cpf);
        sBuilder.insert(3, ".");
        sBuilder.insert(7, ".");
        sBuilder.insert(11, "-");
        return sBuilder.toString();
    }

    private boolean verificarPerfilSistemaPrimeiroAcesso(Usuario uruario) {
        boolean ehTrabalhador = false;
        for (UsuarioPerfilSistema item : uruario.getPerfisSistema()) {
            if (item.getSistema().getCodigo().equals(CODIGO_SISTEMA_CADASTRO)
                    && item.getPerfil().getCodigo().equals(DadosFilter.TRABALHADOR)) {
                ehTrabalhador = true;
            }
        }
        return ehTrabalhador;
    }

    public SolicitacaoEmail solicitarEmailSesi(PrimeiroAcesso solicitacaoEmail) {
        if (solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador() != null
                && solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().size() == 1
                && solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().iterator().next() != null
                && solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().iterator().next().getId() == null) {
            solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().iterator().next().getTelefone()
                    .setTipo(TipoTelefone.RESIDENCIAL);
            solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().iterator().next().getTelefone()
                    .setContato(SimNao.SIM);
        }

        salvarPrimeiroAcesso(solicitacaoEmail);
        solicitacaoEmail.getSolicitacaoEmail()
                .setCpf(adicionarMascaraCpf(solicitacaoEmail.getSolicitacaoEmail().getCpf()));
        return solicitacaoEmailService.enviarEmail(solicitacaoEmail.getSolicitacaoEmail());
    }

    public Trabalhador buscarVacinasAlergiasMedicamentosAutoDeclarados(String cpf) {
        Trabalhador trabalhador = trabalhadorDAO.buscarVacinasAlergiasMedicamentosAutoDeclarados(cpf);

        if (trabalhador == null) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

        return trabalhador;
    }

    private ClienteAuditoria getAuditoriaNovoUsuario(Usuario usuario) {
        ClienteAuditoria cliente = new ClienteAuditoria();
        cliente.setUsuario(usuario.getLogin());
        cliente.setDescricao("Salvando um NOVO usuario");
        cliente.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
        cliente.setNavegador("PRIMEIRO_ACESSO");
        cliente.setFuncionalidade(Funcionalidade.USUARIOS);
        return cliente;
    }

    public Map<String, List<Object>> buscarTrabalhadorByUsuario(String login, String nome, String cpf, String page) {
        br.com.ezvida.girst.apiclient.model.Usuario usuarioGirst = usuarioService.buscarPorLogin(login);
        List<Long> empresas = new ArrayList<>();

        Optional<UsuarioPerfilSistema> optional = usuarioGirst.getPerfisSistema().stream().filter(
                u -> u.getSistema().getCodigo().equals("resonline") &&
                        u.getPerfil().getCodigo().equals(DadosFilter.ADMINISTRADOR)).findFirst();

        if (optional == null || !optional.isPresent()) {
            List<UsuarioEntidade> usuarioEntidades = usuarioEntidadeService.pesquisarPorCPF(login);
            for (UsuarioEntidade usuarioEntidade : usuarioEntidades) {
                if (usuarioEntidade.getEmpresa() != null) {
                    empresas.add(usuarioEntidade.getEmpresa().getId());
                }
            }

            if (empresas.isEmpty()) {
                return new HashMap<>();
            }
        }

        String str = null;

        if (StringUtils.isNotBlank(nome)) {
            try {
                str = URLDecoder.decode(nome, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("erro ao converter nome em utf8", e);
            }
        }

        return trabalhadorDAO.buscarTrabalhadoresByEmpresasDoUsuario(empresas, str, cpf, page);
    }

}