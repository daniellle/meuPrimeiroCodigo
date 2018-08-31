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

import br.com.ezvida.rst.dao.EnderecoParceiroDAO;
import br.com.ezvida.rst.model.EnderecoParceiro;
import br.com.ezvida.rst.model.Parceiro;
import fw.core.service.BaseService;

@Stateless
public class EnderecoParceiroService extends BaseService {

	private static final long serialVersionUID = -4716769568162333977L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoParceiroService.class);
	
	@Inject
	private EnderecoParceiroDAO enderecoParceiroDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoParceiro> enderecosParceiro, Parceiro parceiro) {
		LOGGER.debug("Salvando EnderecoParceiro ");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(enderecosParceiro)) {
			for (EnderecoParceiro enderecoParceiro : enderecosParceiro) {
				enderecoParceiro.setParceiro(parceiro);
				enderecoService.salvar(enderecoParceiro.getEndereco());
				salvar(enderecoParceiro);
			}

			ids = enderecosParceiro.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		enderecoService.desativarEndereco(parceiro.getId(), ids, EnderecoParceiro.class, "parceiro");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnderecoParceiro enderecoRedeCredenciada) {
		LOGGER.debug("Salvando EnderecoParceiro");
		enderecoParceiroDAO.salvar(enderecoRedeCredenciada);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoParceiro> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Listando todos os EnderecoParceiro por parceiro");
		return enderecoParceiroDAO.pesquisarPorParceiro(idParceiro);
	}

}
