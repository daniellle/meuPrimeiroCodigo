package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.TelefoneTrabalhador;
import fw.core.jpa.BaseDAO;

public class TelefoneTrabalhadorDAO extends BaseDAO<TelefoneTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneTrabalhadorDAO.class);

	@Inject
	public TelefoneTrabalhadorDAO(EntityManager em) {
		super(em, TelefoneTrabalhador.class);
	}

	public Set<TelefoneTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Pesquisando TelefoneTrabalhador por idTrabalhador");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where telefoneTrabalhador.trabalhador.id = :idTrabalhador and telefone.dataExclusao is null");
		jpql.append(" order by telefone.numero ");
		TypedQuery<TelefoneTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);
		return Sets.newHashSet(query.getResultList());

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select telefoneTrabalhador from TelefoneTrabalhador telefoneTrabalhador ");
		jpql.append("left join fetch telefoneTrabalhador.telefone telefone ");
		jpql.append("left join fetch telefoneTrabalhador.trabalhador trabalhador ");
	}

}
