package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Regiao;
import fw.core.jpa.BaseDAO;

public class RegiaoDAO extends BaseDAO<Regiao, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegiaoDAO.class);

	@Inject
	public RegiaoDAO(EntityManager em) {
		super(em, Regiao.class);
	}

	@SuppressWarnings("unchecked")
	public List<Regiao> pesquisarTodosComPais() {
		LOGGER.debug("Pesquisando todas regiões com país");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select r from Regiao r left join fetch r.pais p");

		return criarConsulta(jpql.toString()).getResultList();
	}

}
