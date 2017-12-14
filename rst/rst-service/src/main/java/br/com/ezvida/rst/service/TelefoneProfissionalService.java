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

import br.com.ezvida.rst.dao.TelefoneProfissionalDAO;
import br.com.ezvida.rst.model.Profissional;
import br.com.ezvida.rst.model.TelefoneProfissional;
import fw.core.service.BaseService;

@Stateless
public class TelefoneProfissionalService extends BaseService {

	private static final long serialVersionUID = -6610303496156519177L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneProfissionalService.class);

	@Inject
	private TelefoneProfissionalDAO telefoneProfissionalDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TelefoneProfissional> pesquisarPorProfissional(Long id) {
		return telefoneProfissionalDAO.pesquisarPorProfissional(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneProfissional> emailsProfissional, Profissional profissional) {
		LOGGER.debug("Salvando TelefoneProfissional");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(emailsProfissional)) {
			for (TelefoneProfissional telefoneProfissional : emailsProfissional) {
				telefoneProfissional.setProfissional(profissional);
				telefoneService.salvar(telefoneProfissional.getTelefone());
				telefoneProfissionalDAO.salvar(telefoneProfissional);
			}

			ids = emailsProfissional.stream().map(t -> t.getId()).collect(Collectors.toList());
		}

		LOGGER.debug("Foram desativados ");

		telefoneService.desativarTelefone(profissional.getId(), ids, TelefoneProfissional.class, "profissional");
	}

}
