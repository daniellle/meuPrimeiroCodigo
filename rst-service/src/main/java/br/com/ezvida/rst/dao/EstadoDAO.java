package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Estado;
import fw.core.jpa.BaseDAO;

public class EstadoDAO extends BaseDAO<Estado, Long> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EstadoDAO.class);

	@Inject
	public EstadoDAO(EntityManager em) {
		super(em, Estado.class, "descricao");
	}
	
	@SuppressWarnings("unchecked")
	public List<Estado> pesquisarTodosComPais(){
		LOGGER.debug("Pesquisando todos estados com país");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select e from Estado e join fetch e.pais p");
		jpql.append(" order by e.descricao ");
		return criarConsulta(jpql.toString()).getResultList();
	}

}
