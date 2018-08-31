package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.dao.TrabalhadorDependenteDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.Dependente;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.TrabalhadorDependente;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class TrabalhadorDependenteService extends BaseService {

	private static final long serialVersionUID = 5968045804831589592L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorDependenteService.class);

	@Inject
	private TrabalhadorDependenteDAO trabalhadorDependenteDAO;

	@Inject
	private TrabalhadorDAO trabalhadorDAO;

	@Inject
	private DependenteService dependenteService;
	
	@Inject
	private TrabalhadorService trabalhadorService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<TrabalhadorDependente> pesquisarPorTrabalhador(Long idTrabalhador, ClienteAuditoria auditoria
			, DadosFilter dadosFilter) {		
		LogAuditoria.registrar(LOGGER, auditoria, "Pequisa de dependetes por trabalhador: " + idTrabalhador);
		
		TrabalhadorFilter filtro = new TrabalhadorFilter();
		filtro.setId(idTrabalhador);
		filtro.setAplicarDadosFilter(true);
		if (idTrabalhador != null &&
				this.trabalhadorService.buscarPorId(filtro, auditoria, dadosFilter) == null) {
				throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhuRm_registro_encontrado"));
		}
		
		return trabalhadorDependenteDAO.pesquisarPorTrabalhador(idTrabalhador);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<TrabalhadorDependente> pesquisarPorTrabalhador(TrabalhadorFilter trabalhadorFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pequisa de dependetes por trabalhador: ", trabalhadorFilter);
		return trabalhadorDependenteDAO.pesquisarPorTrabalhador(trabalhadorFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TrabalhadorDependente> pesquisarPorCPF(Long idTrabalhador, List<String> cpfs) {		
		return trabalhadorDependenteDAO.pesquisarPorCPFAtivos(idTrabalhador, cpfs);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TrabalhadorDependente pesquisarDependentePorCPF(String cpf, ClienteAuditoria auditoria) {	
		LogAuditoria.registrar(LOGGER, auditoria
				,"Pesquisa de dependente por cpf: " + cpf);
		return trabalhadorDependenteDAO.pesquisarDependentePorCPF(cpf);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TrabalhadorDependente> pesquisarPorCPFLista(String cpf) {		
		return trabalhadorDependenteDAO.pesquisarPorCPFLista(cpf);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TrabalhadorDependente salvar(TrabalhadorDependente trabalhadoresDependente, Long idTrabalhador, ClienteAuditoria auditoria) {
		if (trabalhadoresDependente != null) {
			List<TrabalhadorDependente> listaTrabalhadorDependente = pesquisarPorCPFLista(trabalhadoresDependente.getDependente().getCpf());
			trabalhadoresDependente.setTrabalhador(new Trabalhador(idTrabalhador));
			validarDependente(trabalhadoresDependente);
			for (TrabalhadorDependente trabalhadorDependenteRetorno : listaTrabalhadorDependente) {
				if (trabalhadorDependenteRetorno.getTrabalhador().getId().equals(idTrabalhador)) {
					trabalhadoresDependente.setId(trabalhadorDependenteRetorno.getId());
				}
			}
			Dependente dependente = obterDependente(trabalhadoresDependente);
			dependenteService.salvar(dependente);
			trabalhadorDependenteDAO.salvar(trabalhadoresDependente);
			LogAuditoria.registrar(LOGGER, auditoria
					,"Cadastro de Dependente para o trabalhador " + trabalhadoresDependente.getTrabalhador().getId()
					,dependente);
		}
		return trabalhadoresDependente;
	}

	private void validarDependente(TrabalhadorDependente trabalhadorDependente) {
		LOGGER.debug("Validando Dependentes");

		List<TrabalhadorDependente> listaTrabalhadorDependente = pesquisarPorCPFLista(trabalhadorDependente.getDependente().getCpf());
		if (CollectionUtils.isNotEmpty(listaTrabalhadorDependente)) {
			for (TrabalhadorDependente trabalhadorDependenteRetorno : listaTrabalhadorDependente) {
				if (trabalhadorDependenteRetorno != null
						&& !trabalhadorDependenteRetorno.getTrabalhador().getId().equals(trabalhadorDependente.getTrabalhador().getId())
						&& trabalhadorDependenteRetorno.getDataExclusao() == null && SimNao.NAO.equals(trabalhadorDependenteRetorno.getInativo())) {
					throw new BusinessErrorException(getMensagem("app_rst_dependente_associado_ao_trabalhador",
							trabalhadorDependenteRetorno.getTrabalhador().getCpf(), trabalhadorDependenteRetorno.getTrabalhador().getNome()));
				}
			}
		}

		Trabalhador trabalhadorCadastrado = trabalhadorDAO.pesquisarPorCpf(trabalhadorDependente.getDependente().getCpf());
		if (trabalhadorCadastrado != null && trabalhadorCadastrado.getDataExclusao() == null) {
			throw new BusinessErrorException(
					getMensagem("app_rst_dependente_cadastrado_como_tabalhador", trabalhadorCadastrado.getCpf(), trabalhadorCadastrado.getNome()));
		}
	}

	private Dependente obterDependente(TrabalhadorDependente trabalhadorDependente) {
		Dependente dependente = dependenteService.pesquisarPorCPF(trabalhadorDependente.getDependente().getCpf());
		if (dependente != null) {
			trabalhadorDependente.getDependente().setId(dependente.getId());
		}
		return trabalhadorDependente.getDependente();
	}

	public TrabalhadorDependente desativar(TrabalhadorDependente trabalhadorDependente, ClienteAuditoria auditoria) {
		
		TrabalhadorDependente trabalhadorDependenteRetorno = null;
		if (trabalhadorDependente != null) {
			trabalhadorDependenteRetorno = trabalhadorDependenteDAO.pesquisarPorId(trabalhadorDependente.getId());
		}

		if (trabalhadorDependenteRetorno != null && trabalhadorDependenteRetorno.getDataExclusao() == null) {
			trabalhadorDependenteRetorno.setDataExclusao(new Date());
		}

		trabalhadorDependenteDAO.salvar(trabalhadorDependenteRetorno);
		LogAuditoria.registrar(LOGGER, auditoria, "Desativar dependente: ", trabalhadorDependenteRetorno);

		return trabalhadorDependenteRetorno;
	}

}
