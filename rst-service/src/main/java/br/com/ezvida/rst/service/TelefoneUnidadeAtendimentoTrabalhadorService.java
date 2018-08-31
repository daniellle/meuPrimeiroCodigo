package br.com.ezvida.rst.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import br.com.ezvida.rst.dao.TelefoneUnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.model.TelefoneUnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import fw.core.service.BaseService;

@Stateless
public class TelefoneUnidadeAtendimentoTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 1751707269785346762L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneUnidadeAtendimentoTrabalhadorService.class);

	@Inject
	private TelefoneUnidadeAtendimentoTrabalhadorDAO telefoneUnidadeAtendimentoTrabalhadorDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneUnidadeAtendimentoTrabalhador> telefonesUnidadeAtendimentoTrabalhador, UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		LOGGER.debug("Salvando TelefoneDepartamentoRegional");
		
		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(telefonesUnidadeAtendimentoTrabalhador)) {
			for (TelefoneUnidadeAtendimentoTrabalhador telefoneUnidadeAtendimentoTrabalhador : telefonesUnidadeAtendimentoTrabalhador) {
				telefoneUnidadeAtendimentoTrabalhador.setUnidadeAtendimentoTrabalhador(unidadeAtendimentoTrabalhador);
				telefoneService.salvar(telefoneUnidadeAtendimentoTrabalhador.getTelefone());
				salvar(telefoneUnidadeAtendimentoTrabalhador);
			}

			ids = telefonesUnidadeAtendimentoTrabalhador.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		telefoneService.desativarTelefone(unidadeAtendimentoTrabalhador.getId(), ids, TelefoneUnidadeAtendimentoTrabalhador.class,
				"unidadeAtendimentoTrabalhador");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TelefoneUnidadeAtendimentoTrabalhador telefoneUat) {
		LOGGER.debug("Salvando TelefoneDepartamentoRegional");
		telefoneUnidadeAtendimentoTrabalhadorDAO.salvar(telefoneUat);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TelefoneUnidadeAtendimentoTrabalhador> pesquisarPorIdUat(Long id) {
		return telefoneUnidadeAtendimentoTrabalhadorDAO.pesquisarPorIdUat(id);
	}
}
