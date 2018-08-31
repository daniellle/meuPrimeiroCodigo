package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.ConselhoRegional;
import fw.core.jpa.BaseDAO;

public class ConselhoRegionalDAO extends BaseDAO<ConselhoRegional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EstadoDAO.class);

	@Inject
	public ConselhoRegionalDAO(EntityManager em) {
		super(em, ConselhoRegional.class, "nome");
	}

	@SuppressWarnings("unchecked")
	public List<ConselhoRegional> pesquisarTodos() {
		LOGGER.debug("Pesquisando todos ConselhoRegional");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select e from ConselhoRegional e");
		jpql.append(" order by e.nome ");
		return criarConsulta(jpql.toString()).getResultList();
	}
}
