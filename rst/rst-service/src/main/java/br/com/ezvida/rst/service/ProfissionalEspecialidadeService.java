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

import br.com.ezvida.rst.dao.ProfissionalEspecialidadeDAO;
import br.com.ezvida.rst.model.Profissional;
import br.com.ezvida.rst.model.ProfissionalEspecialidade;
import fw.core.service.BaseService;

@Stateless
public class ProfissionalEspecialidadeService extends BaseService {

	private static final long serialVersionUID = -7842498666682214829L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfissionalEspecialidadeService.class);

	@Inject
	private ProfissionalEspecialidadeDAO profissionalEspecialidadeDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProfissionalEspecialidade> pesquisarPorProfissional(Long id) {
		return profissionalEspecialidadeDAO.pesquisarPorProfissional(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<ProfissionalEspecialidade> profissionalEspecialidades, Profissional profissional) {
		LOGGER.debug("Salvando ProfissionalEspecialidade");

		List<Long> ids = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(profissionalEspecialidades)) {
			for (ProfissionalEspecialidade profissionalEspecialidade : profissionalEspecialidades) {
				profissionalEspecialidade.setProfissional(profissional);
				profissionalEspecialidadeDAO.salvar(profissionalEspecialidade);
			}

			ids = profissionalEspecialidades.stream().map(t -> t.getId()).collect(Collectors.toList());
		}

		LOGGER.debug("Foram desativados ");

		profissionalEspecialidadeDAO.desativar(profissional.getId(), ids, "profissional");
	}

}
