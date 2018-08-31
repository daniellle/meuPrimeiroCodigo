package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.Telefone;

public class TelefoneDAO extends BaseRstDAO<Telefone, Long> {
	
	@Inject
	public TelefoneDAO(EntityManager em) {
		super(em, Telefone.class, "numero");
	}
}
