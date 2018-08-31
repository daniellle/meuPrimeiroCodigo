package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.GrupoPergunta;
import fw.core.jpa.BaseDAO;

public class GrupoPerguntaQuestionarioDAO extends BaseDAO<GrupoPergunta, Long> {

	@Inject
	public GrupoPerguntaQuestionarioDAO(EntityManager em) {
		super(em, GrupoPergunta.class);
	}

}
