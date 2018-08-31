package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.TelefoneDepartamentoRegional;
import fw.core.jpa.BaseDAO;

public class TelefoneDepartamentoRegionalDAO extends BaseDAO<TelefoneDepartamentoRegional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneDepartamentoRegionalDAO.class);

	@Inject
	public TelefoneDepartamentoRegionalDAO(EntityManager em) {
		super(em, TelefoneDepartamentoRegional.class);
	}

	public Set<TelefoneDepartamentoRegional> pesquisarPorDepartamentoRegional(Long idDepartamentoRegional) {
		LOGGER.debug("Pesquisando TelefoneDepartamentoRegional por idDepartamentoRegional");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where departamentoRegional.id = :idDepartamentoRegional and telefone.dataExclusao is null ");
		jpql.append(" order by telefone.numero ");
		TypedQuery<TelefoneDepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idDepartamentoRegional", idDepartamentoRegional);
		return Sets.newHashSet(query.getResultList());

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select telefoneDepartamentoRegional from TelefoneDepartamentoRegional telefoneDepartamentoRegional ");
		jpql.append("left join fetch telefoneDepartamentoRegional.telefone telefone ");
		jpql.append("left join fetch telefoneDepartamentoRegional.departamentoRegional departamentoRegional ");
	}

}
