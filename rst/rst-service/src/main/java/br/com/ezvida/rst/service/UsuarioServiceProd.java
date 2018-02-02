package br.com.ezvida.rst.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.rst.anotacoes.Prod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import br.com.ezvida.girst.apiclient.client.UsuarioClient;
import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.girst.apiclient.model.Perfil;
import br.com.ezvida.girst.apiclient.model.UsuarioPerfilSistema;
import br.com.ezvida.girst.apiclient.model.filter.UsuarioFilter;
import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UsuarioGirstViewDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.model.UsuarioEntidade;
import br.com.ezvida.rst.model.UsuarioGirstView;
import br.com.ezvida.rst.model.dto.UsuarioDTO;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.common.util.ResourceUtil;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
@Prod
public class UsuarioServiceProd extends BaseService implements UsuarioService {

	private static final long serialVersionUID = -4839541588378608503L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CredencialService.class);

	private static final String CODIGO_PERFIL_GESTOR_EMPRESA = "GEEM";
	private static final String CODIGO_PERFIL_DIRETOR_DR = "DIDR";
	private static final String CODIGO_PERFIL_GESTOR_DR_APLICACOES = "GDRA";

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
		br.com.ezvida.girst.apiclient.model.Usuario usuarioGirst = usuarioClient.getPerfilUsuario(
				apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), login,
				apiClientService.getSistema());

		Usuario usuario = null;
		if (usuarioGirst != null) {
			LOGGER.debug("Criando usuario do rst");
			usuario = new Usuario(null, usuarioGirst.getLogin(), usuarioGirst.getNome(), null, null,
					usuarioGirst.getEmail());

			List<br.com.ezvida.girst.apiclient.model.Perfil> perfis = usuarioGirst.getPerfisSistema().stream()
					.map(ups -> ups.getPerfil()).collect(Collectors.toList());

			for (Perfil perfil : perfis) {
				usuario.getPapeis().add(perfil.getCodigo());
				usuario.getPermissoes().addAll(perfil.getPerfilPermissoes().stream()
						.map(p -> p.getPermissao().getNome()).collect(Collectors.toList()));
			}
			getEntidadesFiltradas(usuario);
		}		
		return usuario;
	}

	private void getEntidadesFiltradas(Usuario usuario) {
		List<UsuarioEntidade> usuariosEntidade = usuarioEntidadeService.pesquisarPorCPF(usuario.getLogin());

		if (usuario.getPapeis().contains(DadosFilter.TRABALHADOR)) {
			Trabalhador trabalhador = trabalhadorService.pesquisarPorCPF(usuario.getLogin());

			if (trabalhador != null) {
				usuario.getIdTrabalhadores().add(trabalhador.getId());
				usuario.setIdDepartamentos(departamentoRegionalService.buscarPorTrabalhador(trabalhador.getId())
						.stream().map(d -> d.getId()).collect(Collectors.toSet()));
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
	public ListaPaginada<br.com.ezvida.girst.apiclient.model.Usuario> pesquisarPaginado(
			br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter) {

		UsuarioFilter usuarioFilterClient = new UsuarioFilter();
		usuarioFilterClient.setLogin(usuarioFilter.getLogin());
		usuarioFilterClient.setNome(usuarioFilter.getNome());
		usuarioFilterClient.setPagina(usuarioFilter.getPagina());
		usuarioFilterClient.setQuantidadeRegistro(usuarioFilter.getQuantidadeRegistro());

		return usuarioClient.pesquisarPaginado(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), usuarioFilterClient);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public br.com.ezvida.rst.dao.filter.ListaPaginada<UsuarioGirstView> pesquisarPaginadoGirst(
			br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter, DadosFilter dados
			, ClienteAuditoria auditoria) {

		br.com.ezvida.rst.dao.filter.ListaPaginada<UsuarioGirstView> listaPagianda = usuarioGirstViewDAO
				.pesquisarPorFiltro(usuarioFilter, dados);
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de usuário por filtro: " , usuarioFilter);
		return listaPagianda;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public br.com.ezvida.girst.apiclient.model.Usuario buscarPorId(String id, ClienteAuditoria auditoria) {	
		if (auditoria != null) {
			LogAuditoria.registrar(LOGGER, auditoria, "buscar usuário por id: " + id);
		}
		try {			
			return usuarioClient.buscarUsuarioPorId(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), id);
		} catch (Exception e) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public br.com.ezvida.girst.apiclient.model.Usuario cadastrarUsuario(
			br.com.ezvida.girst.apiclient.model.Usuario usuario
			, ClienteAuditoria auditoria) {
		if (auditoria != null) {
			LogAuditoria.registrar(LOGGER, auditoria, "cadastro de usuário: ", usuario);
		}
		return usuarioClient.cadastrar(apiClientService.getURL()
				, apiClientService.getOAuthToken().getAccess_token(),
				usuario);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public br.com.ezvida.girst.apiclient.model.Usuario alterarUsuario(
			br.com.ezvida.girst.apiclient.model.Usuario usuario, ClienteAuditoria auditoria) {

		br.com.ezvida.girst.apiclient.model.Usuario usuarioAnterior = buscarPorId(usuario.getId().toString(), null);
		if (auditoria != null && usuarioAnterior != null && usuarioAnterior.getPerfisSistema() != null
				&& usuarioAnterior.getPerfisSistema().size() > 0) {
			List<UsuarioPerfilSistema> perfisRemovidos = new ArrayList<UsuarioPerfilSistema>();
			for (UsuarioPerfilSistema itemUsrAnt : usuarioAnterior.getPerfisSistema()) {
				if (!usuario.getPerfisSistema().contains(itemUsrAnt)) {
					perfisRemovidos.add(itemUsrAnt);
				}
			}

			if (perfisRemovidos.size() > 0) {
				for (UsuarioPerfilSistema perfil : perfisRemovidos) {
					if (perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_EMPRESA)) {
						List<UsuarioEntidade> listaEmpresasAssociadas = usuarioEntidadeService
								.pesquisarTodasEmpresasAssociadas(usuario.getLogin());
						if (listaEmpresasAssociadas != null && listaEmpresasAssociadas.size() > 0) {
							for (UsuarioEntidade item : listaEmpresasAssociadas) {
								usuarioEntidadeService.desativarUsuarioEntidade(item, auditoria);
							}
						}
					}
					if (perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_DIRETOR_DR)
							|| perfil.getPerfil().getCodigo().equals(CODIGO_PERFIL_GESTOR_DR_APLICACOES)) {
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
		
		if (auditoria != null) {
			LogAuditoria.registrar(LOGGER, auditoria, "Alterção do usuário: " + usuario);
		}
		return usuarioClient.alterar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(),
				usuario);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public br.com.ezvida.girst.apiclient.model.Usuario desativarUsuario(String id, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,"desativar usuário: " + id);
		return usuarioClient.remover(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), id,
				null);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public br.com.ezvida.girst.apiclient.model.Usuario buscarPorEmail(String email) {
		return usuarioClient.buscarPorEmail(apiClientService.getURL(),
				apiClientService.getOAuthToken().getAccess_token(), email);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public br.com.ezvida.girst.apiclient.model.Usuario buscarPorLogin(String login) {
		return usuarioClient.getPerfilUsuario(apiClientService.getURL(),
				apiClientService.getOAuthToken().getAccess_token(), login, null);
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
	public UsuarioDTO consultarDadosUsuario(DadosFilter dados, Usuario usuarioLogado) {
		LOGGER.debug("consultando dados do usuario");

		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setLogin(usuarioLogado.getLogin());

		if (dados.isTrabalhador()) {
			usuario.setEmpresas(empresaService.buscarEmpresasUatsDrsPorCpf(usuarioLogado.getLogin()));
			Trabalhador trabalhador = trabalhadorService.buscarPorCpf(usuarioLogado.getLogin());
			usuario.setTipoImagem(trabalhador.getTipoImagem());
			usuario.setImagem(trabalhador.getImagem());
		} else {
			usuario.setDepartamentosRegionais(departamentoRegionalService.pesquisarPorIds(dados.getIdsDepartamentoRegional()));
			usuario.setEmpresas(empresaService.buscarEmpresasUatsDrsPorIds(dados.getIdsEmpresa()));
		}

		return usuario;
	}

}