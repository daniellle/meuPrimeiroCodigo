package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.Pais;
import fw.core.jpa.BaseDAO;

public class PaisDAO extends BaseDAO<Pais, Long> {

	
	private static final String ORDER_BY = "descricao";
	
	@Inject
	public PaisDAO(EntityManager em) {
		super(em, Pais.class, ORDER_BY);
	}

}
