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

import br.com.ezvida.rst.dao.TelefoneDepartamentoRegionalDAO;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.TelefoneDepartamentoRegional;
import fw.core.service.BaseService;

@Stateless
public class TelefoneDepartamentoRegionalService extends BaseService {

	private static final long serialVersionUID = 6497591884414563630L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneDepartamentoRegionalService.class);

	@Inject
	private TelefoneDepartamentoRegionalDAO telefoneDepartamentoRegionalDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneDepartamentoRegional> telefonesDepartamentoRegional,
			DepartamentoRegional departamentoRegional) {
		LOGGER.debug("Salvando TelefoneDepartamentoRegional");
		
		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(telefonesDepartamentoRegional)) {
			for (TelefoneDepartamentoRegional telefoneDepartamentoRegional : telefonesDepartamentoRegional) {
				telefoneDepartamentoRegional.setDepartamentoRegional(departamentoRegional);
				telefoneService.salvar(telefoneDepartamentoRegional.getTelefone());
				salvar(telefoneDepartamentoRegional);
			}

			ids = telefonesDepartamentoRegional.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		telefoneService.desativarTelefone(departamentoRegional.getId(), ids, TelefoneDepartamentoRegional.class, "departamentoRegional");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TelefoneDepartamentoRegional telefoneDepartamentoRegional) {
		LOGGER.debug("Salvando TelefoneDepartamentoRegional");
		telefoneDepartamentoRegionalDAO.salvar(telefoneDepartamentoRegional);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<TelefoneDepartamentoRegional> pesquisarPorDepartamentoRegional(Long idDepartamentoRegional) {
		LOGGER.debug("Listando todos os TelefoneDepartamentoRegional por departamento regional");
		return telefoneDepartamentoRegionalDAO.pesquisarPorDepartamentoRegional(idDepartamentoRegional);
	}

}
