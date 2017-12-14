package br.com.ezvida.rst.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.girst.apiclient.model.Perfil;
import br.com.ezvida.girst.apiclient.model.Sistema;
import br.com.ezvida.girst.apiclient.model.Usuario;
import br.com.ezvida.girst.apiclient.model.UsuarioPerfilSistema;
import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.enums.TipoTelefone;
import br.com.ezvida.rst.model.EmailTrabalhador;
import br.com.ezvida.rst.model.PrimeiroAcesso;
import br.com.ezvida.rst.model.SolicitacaoEmail;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.TrabalhadorDependente;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class TrabalhadorService extends BaseService {

	private static final long serialVersionUID = 6186912314459313987L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorService.class);
	
	private static final String CODIGO_SISTEMA_CADASTRO = "cadastro";
	
	@Inject
	private TrabalhadorDAO trabalhadorDAO;

	@Inject
	private TelefoneTrabalhadorService telefoneTrabalhadorService;

	@Inject
	private EmailTrabalhadorService emailTrabalhadorService;

	@Inject
	private EnderecoTrabalhadorService enderecoTrabalhadorService;

	@Inject
	private TrabalhadorDependenteService trabalhadorDependenteService;

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private SolicitacaoEmailService solicitacaoEmailService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Trabalhador buscarPorId(TrabalhadorFilter trabalhadorFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter) {

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
	public Trabalhador buscarTrabalhadorPrimeiroAcesso(String cpf, String dataNascimento) {
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
		
		if (trabalhador != null) {
			trabalhador.setListaTelefoneTrabalhador(
					telefoneTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
			trabalhador.setListaEmailTrabalhador(emailTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
			trabalhador.setListaEnderecoTrabalhador(
					enderecoTrabalhadorService.pesquisarPorTrabalhador(trabalhador.getId()));
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
		String descricaoAuditoria = "Cadastro de Profissional: ";

		if (trabalhador.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de trabalhador: ";
		}
		validar(trabalhador, auditoria);
		trabalhadorDAO.salvar(trabalhador);
		emailTrabalhadorService.salvar(trabalhador.getListaEmailTrabalhador(), trabalhador);
		telefoneTrabalhadorService.salvar(trabalhador.getListaTelefoneTrabalhador(), trabalhador);
		enderecoTrabalhadorService.salvar(trabalhador.getListaEnderecoTrabalhador(), trabalhador);
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, trabalhador);
		return trabalhador;
	}

	private Usuario buscarTrabalhadorJaCadastrado(String login) {
		UsuarioFilter usuarioFilter = new UsuarioFilter(login, null, 1, 10);
		br.com.ezvida.girst.apiclient.model.ListaPaginada<br.com.ezvida.girst.apiclient.model.Usuario> lista = usuarioService
				.pesquisarPaginado(usuarioFilter);
		if (lista.getQuantidade() > 0) {
			return usuarioService.buscarPorLogin(login);
		}

		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PrimeiroAcesso salvarPrimeiroAcesso(PrimeiroAcesso primeiroAcesso) {

		Trabalhador trab = trabalhadorDAO.pesquisarPorId(primeiroAcesso.getTrabalhador().getId());

		if (trab != null) {
			if (trab.getDataFalecimento() != null) {
				throw new BusinessErrorException(getMensagem("app_rst_primeiro_acesso_erro_data_falecimento"));
			}

			definirPerfilSistema(primeiroAcesso);

			Usuario usuario = buscarTrabalhadorJaCadastrado(trab.getCpf());
			if (usuario == null) {
				usuario = usuarioService.cadastrarUsuario(primeiroAcesso.getUsuario(), null);

				if (usuario == null) {
					throw new BusinessErrorException(getMensagem("app_validacao_error"));
				}

			} else {
				if (!verificarPerfilSistemaPrimeiroAcesso(usuario)) {
					usuario.getPerfisSistema().add(primeiroAcesso.getUsuario().getPerfisSistema().iterator().next());
				}
				
				usuario.setSenha(primeiroAcesso.getUsuario().getSenha());
				usuario.setEmail(primeiroAcesso.getUsuario().getEmail());
				usuarioService.alterarUsuario(usuario, null);
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

		UsuarioPerfilSistema usuarioPerfilSistema = new UsuarioPerfilSistema();
		usuarioPerfilSistema.setPerfil(perfil);
		usuarioPerfilSistema.setSistema(sistema);

		primeiroAcesso.getUsuario().setPerfisSistema(new HashSet<UsuarioPerfilSistema>());
		primeiroAcesso.getUsuario().getPerfisSistema().add(usuarioPerfilSistema);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Trabalhador pesquisarPorCPF(String cpf) {
		List<Trabalhador> trabalhadores = trabalhadorDAO.pesquisarPorCPF(Arrays.asList(cpf));
		return !trabalhadores.isEmpty() ? trabalhadores.get(0) : null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Trabalhador buscarPorCpf(String cpf, ClienteAuditoria auditoria, DadosFilter dadosFilter) {

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
    		    Set<EmailTrabalhador> emailTrabalhadorRetorno = emailTrabalhadorService.pesquisarPorEmail(emailTrabalhador.getEmail().getDescricao());
    		    if (emailTrabalhadorRetorno != null && CollectionUtils.isNotEmpty(emailTrabalhadorRetorno)
    		    		&& !emailTrabalhadorRetorno.iterator().next().getTrabalhador().getId().equals(trabalhador.getId())) {
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

    private String adicionarMascaraCpf(String cpf) {
		StringBuilder sBuilder = new StringBuilder(cpf);
		sBuilder.insert(3, ".");
		sBuilder.insert(7, ".");
		sBuilder.insert(11, "-");
		return sBuilder.toString();
	}
    
    private boolean verificarPerfilSistemaPrimeiroAcesso(Usuario uruario) {
    	boolean ehTrabalhador = false;
    	for (UsuarioPerfilSistema item: uruario.getPerfisSistema()) {
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
    			&& solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().iterator().next().getId() == null ) {
    		solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().iterator().next().getTelefone().setTipo(TipoTelefone.RESIDENCIAL);
    		solicitacaoEmail.getTrabalhador().getListaTelefoneTrabalhador().iterator().next().getTelefone().setContato(SimNao.SIM);
    	}
    	
    	salvarPrimeiroAcesso(solicitacaoEmail);
    	solicitacaoEmail.getSolicitacaoEmail().setCpf(
    			adicionarMascaraCpf(solicitacaoEmail.getSolicitacaoEmail().getCpf()));
    	return solicitacaoEmailService.enviarEmail(solicitacaoEmail.getSolicitacaoEmail());
    }
}