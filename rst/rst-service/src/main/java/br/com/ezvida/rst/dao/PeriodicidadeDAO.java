package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.Periodicidade;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class PeriodicidadeDAO extends BaseDAO<Periodicidade, Long> {

	@Inject
	public PeriodicidadeDAO(EntityManager em) {
		super(em, Periodicidade.class);
	}
	
	public Periodicidade pesquisarPorId(Long id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select periodicidade from Periodicidade periodicidade ");
		jpql.append(" where periodicidade.id = :id");
		TypedQuery<Periodicidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return DAOUtil.getSingleResult(query);
	}
	
	public List<Periodicidade> listarTodos() {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select periodicidade from Periodicidade periodicidade ");
		TypedQuery<Periodicidade> query = criarConsultaPorTipo(jpql.toString());
		return query.getResultList();
	}

}
