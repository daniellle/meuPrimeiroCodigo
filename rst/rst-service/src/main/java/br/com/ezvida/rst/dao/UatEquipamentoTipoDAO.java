package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.UatEquipamentoTipo;
import fw.core.jpa.BaseDAO;

public class UatEquipamentoTipoDAO extends BaseDAO<UatEquipamentoTipo, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoTipoDAO.class);
	
	@Inject
	public UatEquipamentoTipoDAO(EntityManager em) {
		super(em, UatEquipamentoTipo.class, "descricao");
	}

	public List<UatEquipamentoTipo> pesquisarPorIdArea(Long idArea) {
		LOGGER.debug("Listando todos os EquipamentoTipo da area de ID {}", idArea);

		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT uet FROM UatEquipamentoTipo uet ");
		jpql.append(" JOIN uet.uatEquipamentoArea uea ");
		jpql.append(" WHERE uea.id = :idArea ");
		TypedQuery<UatEquipamentoTipo> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idArea", idArea);
		return query.getResultList();
	}

}
