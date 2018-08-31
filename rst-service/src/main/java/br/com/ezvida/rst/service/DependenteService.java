package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.DependenteDAO;
import br.com.ezvida.rst.model.Dependente;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class DependenteService extends BaseService {

	private static final long serialVersionUID = -2382642989436948815L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DependenteService.class);

	@Inject
	private DependenteDAO dependenteDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<Dependente> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Listando todos os Dependente por trabalhador");
		return dependenteDAO.pesquisarPorTrabalhador(idTrabalhador);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Dependente pesquisarPorCPF(String cpf) {
		LOGGER.debug("Pesquisando Dependente por CPF");
		return dependenteDAO.pesquisarPorCPF(cpf);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Dependente> pesquisarPorCPF(Long idTrabalhador, List<String> cpfs) {
		LOGGER.debug("Pesquisando Dependente por CPF");
		return dependenteDAO.pesquisarPorCPF(idTrabalhador, cpfs);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Dependente dependente) {
		LOGGER.debug("Salvando Dependentes");
		validar(dependente);
		dependenteDAO.salvar(dependente);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desativarDependentes(Long id, List<Long> ids, Class<?> type, String fieldClass) {
		int quantidade = dependenteDAO.desativar(id, ids, type, fieldClass);
		
		LOGGER.debug("Foi desativado um total de {} em {}", quantidade, type.getSimpleName());
	}

	private void validar(Dependente dependente) {
		LOGGER.debug("Validando Dependente...");

		if (StringUtils.isNotEmpty(dependente.getCpf()) && !ValidadorUtils.isValidCPF(dependente.getCpf())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cpf")));
		}

		if (dependente.getDataNascimento() != null && dependente.getDataNascimento().after(new Date())) {
			throw new BusinessErrorException(getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_label_data_nascimento")));
		}
	}
}
