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

import br.com.ezvida.rst.dao.EnderecoUnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.model.EnderecoUnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import fw.core.service.BaseService;

@Stateless
public class EnderecoUnidadeAtendimentoTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 8510449027638048237L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoUnidadeAtendimentoTrabalhadorService.class);

	@Inject
	private EnderecoUnidadeAtendimentoTrabalhadorDAO enderecoUnidadeAtendimentoTrabalhadorDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoUnidadeAtendimentoTrabalhador> enderecosUnidadeAtendimentoTrabalhador, UnidadeAtendimentoTrabalhador unidadeEmailTrabalhador) {
		LOGGER.debug("Salvando Enderecos Unidade Atendimento Trabalhador");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(enderecosUnidadeAtendimentoTrabalhador)) {
			for (EnderecoUnidadeAtendimentoTrabalhador enderecoUnidadeAtendimentoTrabalhador : enderecosUnidadeAtendimentoTrabalhador) {
				enderecoUnidadeAtendimentoTrabalhador.setUnidadeAtendimentoTrabalhador(unidadeEmailTrabalhador);
				enderecoService.salvar(enderecoUnidadeAtendimentoTrabalhador.getEndereco());
				salvar(enderecoUnidadeAtendimentoTrabalhador);
			}

			ids = enderecosUnidadeAtendimentoTrabalhador.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		enderecoService.desativarEndereco(unidadeEmailTrabalhador.getId(), ids, EnderecoUnidadeAtendimentoTrabalhador.class,
				"unidadeAtendimentoTrabalhador");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnderecoUnidadeAtendimentoTrabalhador enderecoUat) {
		LOGGER.debug("Salvando Endereco Unidade Atendimento Trabalhador");
		enderecoUnidadeAtendimentoTrabalhadorDAO.salvar(enderecoUat);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoUnidadeAtendimentoTrabalhador> pesquisarPorIdUat(Long id) {
		return enderecoUnidadeAtendimentoTrabalhadorDAO.pesquisarPorIdUat(id);
	}
}
