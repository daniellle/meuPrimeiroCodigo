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

import br.com.ezvida.rst.dao.ParceiroEspecialidadeDAO;
import br.com.ezvida.rst.model.Parceiro;
import br.com.ezvida.rst.model.ParceiroEspecialidade;
import fw.core.service.BaseService;

@Stateless
public class ParceiroEspecialidadeService extends BaseService {

	private static final long serialVersionUID = -127428641010886427L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroEspecialidadeService.class);
	
	@Inject
	private ParceiroEspecialidadeDAO parceiroEspecialidadeDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<ParceiroEspecialidade> parceiroEspecialidades, Parceiro parceiro) {
		LOGGER.debug("Salvando ParceiroEspecialidade ");
		
		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(parceiroEspecialidades)) {
			for (ParceiroEspecialidade parceiroEspecialidade : parceiroEspecialidades) {
				parceiroEspecialidade.setParceiro(parceiro);
				salvar(parceiroEspecialidade);
			}

			ids = parceiroEspecialidades.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		parceiroEspecialidadeDAO.desativar(parceiro.getId(), ids, "parceiro");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ParceiroEspecialidade parceiroEspecialidade) {
		LOGGER.debug("Salvando ParceiroEspecialidade...");
		parceiroEspecialidadeDAO.salvar(parceiroEspecialidade);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ParceiroEspecialidade> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Listando todos os ParceiroEspecialidade por parceiro ");
		return parceiroEspecialidadeDAO.pesquisarPorParceiro(idParceiro);
	}

}
