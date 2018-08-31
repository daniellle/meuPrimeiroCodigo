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

import br.com.ezvida.rst.dao.TelefoneTrabalhadorDAO;
import br.com.ezvida.rst.model.TelefoneTrabalhador;
import br.com.ezvida.rst.model.Trabalhador;
import fw.core.service.BaseService;

@Stateless
public class TelefoneTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 1395282259338694962L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneTrabalhadorService.class);

	@Inject
	private TelefoneTrabalhadorDAO telefoneTrabalhadorDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<TelefoneTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Listando todos os TelefoneTrabalhador por trabalhador");
		return telefoneTrabalhadorDAO.pesquisarPorTrabalhador(idTrabalhador);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneTrabalhador> telefonesTrabalhador, Trabalhador trabalhador) {
		LOGGER.debug("Salvando TelefoneTrabalhador");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(telefonesTrabalhador)) {
			for (TelefoneTrabalhador telefoneTrabalhador : telefonesTrabalhador) {
				telefoneTrabalhador.setTrabalhador(trabalhador);
				telefoneService.salvar(telefoneTrabalhador.getTelefone());
				telefoneTrabalhadorDAO.salvar(telefoneTrabalhador);
			}
			ids = telefonesTrabalhador.stream().map(t -> t.getId()).collect(Collectors.toList());
		}
		LOGGER.debug("Foram desativados ");
		
		telefoneService.desativarTelefone(trabalhador.getId(), ids, TelefoneTrabalhador.class, "trabalhador");
	}
}
