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

import br.com.ezvida.rst.dao.TelefoneSindicatoDAO;
import br.com.ezvida.rst.model.Sindicato;
import br.com.ezvida.rst.model.TelefoneSindicato;
import fw.core.service.BaseService;

@Stateless
public class TelefoneSindicatoService extends BaseService {

	private static final long serialVersionUID = -7054153118774571956L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneSindicatoService.class);

	@Inject
	private TelefoneSindicatoDAO telefoneSindicatoDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneSindicato> telefonesSindicato, Sindicato sindicato) {
		LOGGER.debug("Salvando Telefones Sindicato...");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(telefonesSindicato)) {
			for (TelefoneSindicato telefoneSindicato : telefonesSindicato) {
				telefoneService.salvar(telefoneSindicato.getTelefone());
				telefoneSindicato.setSindicato(sindicato);
				salvar(telefoneSindicato);
			}

			ids = telefonesSindicato.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		telefoneService.desativarTelefone(sindicato.getId(), ids, TelefoneSindicato.class, "sindicato");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TelefoneSindicato telefoneSindicato) {
		LOGGER.debug("Salvando Telefone Sindicato...");
		telefoneSindicatoDAO.salvar(telefoneSindicato);
	}

	public List<TelefoneSindicato> pesquisarPorIdSindicato(Long id) {
		return telefoneSindicatoDAO.pesquisarPorIdSindicato(id);
	}
}
