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

import br.com.ezvida.rst.dao.EnderecoSindicatoDAO;
import br.com.ezvida.rst.model.EnderecoSindicato;
import br.com.ezvida.rst.model.Sindicato;
import fw.core.service.BaseService;

@Stateless
public class EnderecoSindicatoService extends BaseService {

	private static final long serialVersionUID = -6610238751995113704L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoSindicatoService.class);

	@Inject
	private EnderecoSindicatoDAO enderecoSindicatoDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoSindicato> enderecosSindicato, Sindicato sindicato) {
		LOGGER.debug("Salvando Enderecos Sindicato ...");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(enderecosSindicato)) {
			for (EnderecoSindicato enderecoSindicato : enderecosSindicato) {
				enderecoService.salvar(enderecoSindicato.getEndereco());
				enderecoSindicato.setSindicato(sindicato);
				salvar(enderecoSindicato);
			}

			ids = enderecosSindicato.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		enderecoService.desativarEndereco(sindicato.getId(), ids, EnderecoSindicato.class, "sindicato");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnderecoSindicato enderecoSindicato) {
		LOGGER.debug("Salvando Endereco Sindicato ...");
		enderecoSindicatoDAO.salvar(enderecoSindicato);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoSindicato> pesquisarPorIdSindicato(Long id) {
		return enderecoSindicatoDAO.pesquisarPorIdSindicato(id);
	}

}
