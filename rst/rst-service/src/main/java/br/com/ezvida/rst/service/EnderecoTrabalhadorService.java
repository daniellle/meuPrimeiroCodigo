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

import br.com.ezvida.rst.dao.EnderecoTrabalhadorDAO;
import br.com.ezvida.rst.model.EnderecoTrabalhador;
import br.com.ezvida.rst.model.Trabalhador;
import fw.core.service.BaseService;

@Stateless
public class EnderecoTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 4449033798976368013L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoTrabalhadorService.class);

	@Inject
	private EnderecoTrabalhadorDAO enderecoTrabalhadorDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EnderecoTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Listando todos os EnderecoTrabalhador por trabalhador");
		return enderecoTrabalhadorDAO.pesquisarPorTrabalhador(idTrabalhador);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoTrabalhador> enderecosTrabalhador, Trabalhador trabalhador) {
		LOGGER.debug("Salvando EmailTrabalhador");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(enderecosTrabalhador)) {
			for (EnderecoTrabalhador enderecoTrabalhador : enderecosTrabalhador) {
				enderecoTrabalhador.setTrabalhador(trabalhador);
				enderecoService.salvar(enderecoTrabalhador.getEndereco());
				enderecoTrabalhadorDAO.salvar(enderecoTrabalhador);
			}

			ids = enderecosTrabalhador.stream().map(t -> t.getId()).collect(Collectors.toList());
		}
		LOGGER.debug("Foram desativados ");

		enderecoService.desativarEndereco(trabalhador.getId(), ids, EnderecoTrabalhador.class, "trabalhador");
	}

}
