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

import br.com.ezvida.rst.dao.EnderecoDepartamentoRegionalDAO;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.EnderecoDepartamentoRegional;
import fw.core.service.BaseService;

@Stateless
public class EnderecoDepartamentoRegionalService extends BaseService {

	private static final long serialVersionUID = 5561700525646055575L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoDepartamentoRegionalService.class);

	@Inject
	private EnderecoDepartamentoRegionalDAO enderecoDepartamentoRegionalDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoDepartamentoRegional> enderecosDepartamentoRegional,
			DepartamentoRegional departamentoRegional) {
		LOGGER.debug("Salvando EndDepRegional");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(enderecosDepartamentoRegional)) {
			for (EnderecoDepartamentoRegional enderecoDepartamentoRegional : enderecosDepartamentoRegional) {
				enderecoDepartamentoRegional.setDepartamentoRegional(departamentoRegional);
				enderecoService.salvar(enderecoDepartamentoRegional.getEndereco());
				enderecoDepartamentoRegionalDAO.salvar(enderecoDepartamentoRegional);
			}

			ids = enderecosDepartamentoRegional.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		enderecoService.desativarEndereco(departamentoRegional.getId(), ids, EnderecoDepartamentoRegional.class, "departamentoRegional");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnderecoDepartamentoRegional enderecoDepartamentoRegional) {
		LOGGER.debug("Salvando EndDepRegional");
		enderecoDepartamentoRegionalDAO.salvar(enderecoDepartamentoRegional);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoDepartamentoRegional> pesquisarPorDepartamentoRegional(Long idDepartamentoRegional) {
		LOGGER.debug("Listando todos os EnderecoDepartamentoRegional por departamento regional");
		return enderecoDepartamentoRegionalDAO.pesquisarPorDepartamentoRegional(idDepartamentoRegional);
	}

}
