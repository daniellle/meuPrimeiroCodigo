package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.Profissao;
import fw.core.jpa.BaseDAO;

public class ProfissaoDAO extends BaseDAO<Profissao, Long> {

	private static final String ORDER_BY = "descricao";

	@Inject
	public ProfissaoDAO(EntityManager em) {
		super(em, Profissao.class, ORDER_BY);
	}
}
