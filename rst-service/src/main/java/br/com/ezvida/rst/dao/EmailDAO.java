package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.Email;

public class EmailDAO extends BaseRstDAO<Email, Long> {

	@Inject
	public EmailDAO(EntityManager em) {
		super(em, Email.class, "descricao");
	}
}
