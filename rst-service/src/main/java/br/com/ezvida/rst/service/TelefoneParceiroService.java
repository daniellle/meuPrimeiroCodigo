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

import br.com.ezvida.rst.dao.TelefoneParceiroDAO;
import br.com.ezvida.rst.model.Parceiro;
import br.com.ezvida.rst.model.TelefoneParceiro;
import fw.core.service.BaseService;

@Stateless
public class TelefoneParceiroService extends BaseService {

	private static final long serialVersionUID = -6662032980765895622L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneParceiroService.class);

	@Inject
	private TelefoneParceiroDAO telefoneParceiroDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneParceiro> telefonesParceiro, Parceiro parceiro) {
		LOGGER.debug("Salvando TelefoneParceiro ");
		
		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(telefonesParceiro)) {
			for (TelefoneParceiro telefoneParceiro : telefonesParceiro) {
				telefoneParceiro.setParceiro(parceiro);
				telefoneService.salvar(telefoneParceiro.getTelefone());
				salvar(telefoneParceiro);
			}

			ids = telefonesParceiro.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		telefoneService.desativarTelefone(parceiro.getId(), ids, TelefoneParceiro.class, "parceiro");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TelefoneParceiro telefoneParceiro) {
		LOGGER.debug("Salvando TelefoneParceiro");
		telefoneParceiroDAO.salvar(telefoneParceiro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TelefoneParceiro> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Listando todos os TelefoneParceiro por parceiro ");
		return telefoneParceiroDAO.pesquisarPorParceiro(idParceiro);
	}

}
