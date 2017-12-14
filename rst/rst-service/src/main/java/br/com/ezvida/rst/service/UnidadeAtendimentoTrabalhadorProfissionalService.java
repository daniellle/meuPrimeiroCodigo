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

import br.com.ezvida.rst.dao.UnidadeAtendimentoTrabalhadorProfissionalDAO;
import br.com.ezvida.rst.model.Profissional;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhadorProfissional;
import fw.core.service.BaseService;

@Stateless
public class UnidadeAtendimentoTrabalhadorProfissionalService extends BaseService {

	private static final long serialVersionUID = -94433468968455699L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeAtendimentoTrabalhadorProfissionalService.class);

	@Inject
	private UnidadeAtendimentoTrabalhadorProfissionalDAO unidadeAtendimentoTrabalhadorProfissionalDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<UnidadeAtendimentoTrabalhadorProfissional> pesquisarPorProfissional(Long idProfissional) {
		LOGGER.debug("Listando todos os UnidadeAtendimentoTrabalhadorProfissional por profissional");
		return unidadeAtendimentoTrabalhadorProfissionalDAO.pesquisarPorProfissional(idProfissional);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<UnidadeAtendimentoTrabalhadorProfissional> unidadesAtendimentoTrabalhadorProfissional, Profissional profissional) {
		LOGGER.debug("Salvando UnidadeAtendimentoTrabalhadorProfissional");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(unidadesAtendimentoTrabalhadorProfissional)) {
			for (UnidadeAtendimentoTrabalhadorProfissional unidadeAtendimentoTrabalhadorProfissional : unidadesAtendimentoTrabalhadorProfissional) {
				unidadeAtendimentoTrabalhadorProfissional.setProfissional(profissional);
				unidadeAtendimentoTrabalhadorProfissionalDAO.salvar(unidadeAtendimentoTrabalhadorProfissional);
			}

			ids = unidadesAtendimentoTrabalhadorProfissional.stream().map(t -> t.getId()).collect(Collectors.toList());
		}
		LOGGER.debug("Foram desativados ");

		unidadeAtendimentoTrabalhadorProfissionalDAO.desativar(profissional.getId(), ids, "profissional");
	}

}
