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

import br.com.ezvida.rst.dao.EnderecoProfissionalDAO;
import br.com.ezvida.rst.model.EnderecoProfissional;
import br.com.ezvida.rst.model.Profissional;
import fw.core.service.BaseService;

@Stateless
public class EnderecoProfissionalService extends BaseService {

	private static final long serialVersionUID = 5711265408371704120L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoProfissionalService.class);

	@Inject
	private EnderecoProfissionalDAO enderecoProfissionalDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EnderecoProfissional> pesquisarPorProfissional(Long idProfissional) {
		LOGGER.debug("Listando todos os EnderecoProfissional por profissional");
		return enderecoProfissionalDAO.pesquisarPorProfissional(idProfissional);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoProfissional> enderecosProfissional, Profissional profissional) {
		LOGGER.debug("Salvando EmailTrabalhador");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(enderecosProfissional)) {
			for (EnderecoProfissional enderecoProfissional : enderecosProfissional) {
				enderecoProfissional.setProfissional(profissional);
				enderecoService.salvar(enderecoProfissional.getEndereco());
				enderecoProfissionalDAO.salvar(enderecoProfissional);
			}

			ids = enderecosProfissional.stream().map(t -> t.getId()).collect(Collectors.toList());
		}
		LOGGER.debug("Foram desativados ");

		enderecoService.desativarEndereco(profissional.getId(), ids, EnderecoProfissional.class, "profissional");
	}

}
