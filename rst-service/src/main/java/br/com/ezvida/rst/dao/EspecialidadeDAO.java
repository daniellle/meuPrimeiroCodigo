package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.Especialidade;
import fw.core.jpa.BaseDAO;

public class EspecialidadeDAO extends BaseDAO<Especialidade, Long> {
	
	@Inject
	public EspecialidadeDAO(EntityManager em) {
		super(em, Especialidade.class, "descricao");
	}
}
