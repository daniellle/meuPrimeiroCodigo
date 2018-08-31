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

import br.com.ezvida.rst.dao.EnderecoRedeCredenciadaDAO;
import br.com.ezvida.rst.model.EnderecoRedeCredenciada;
import br.com.ezvida.rst.model.RedeCredenciada;
import fw.core.service.BaseService;

@Stateless
public class EnderecoRedeCredenciadaService extends BaseService {

	private static final long serialVersionUID = 4264195125029893006L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoRedeCredenciadaService.class);
	
	@Inject
	private EnderecoRedeCredenciadaDAO enderecoRedeCredenciadaDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoRedeCredenciada> enderecosRedeCredenciada, RedeCredenciada redeCredenciada) {
		LOGGER.debug("Salvando EnderecoRedeCredenciada ");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(enderecosRedeCredenciada)) {
			for (EnderecoRedeCredenciada enderecoRedeCredenciada : enderecosRedeCredenciada) {
				enderecoRedeCredenciada.setRedeCredenciada(redeCredenciada);
				enderecoService.salvar(enderecoRedeCredenciada.getEndereco());
				salvar(enderecoRedeCredenciada);
			}

			ids = enderecosRedeCredenciada.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		enderecoService.desativarEndereco(redeCredenciada.getId(), ids, EnderecoRedeCredenciada.class, "redeCredenciada");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnderecoRedeCredenciada enderecoRedeCredenciada) {
		LOGGER.debug("Salvando EnderecoRedeCredenciada");
		enderecoRedeCredenciadaDAO.salvar(enderecoRedeCredenciada);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoRedeCredenciada> pesquisarPorRedeCredenciada(Long idRedeCredenciada) {
		LOGGER.debug("Listando todos os EnderecoRedeCredenciada por rede credenciada");
		return enderecoRedeCredenciadaDAO.pesquisarPorRedeCredenciada(idRedeCredenciada);
	}

}
