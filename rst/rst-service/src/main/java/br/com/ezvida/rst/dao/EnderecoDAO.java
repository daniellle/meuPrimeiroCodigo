package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.Endereco;

public class EnderecoDAO extends BaseRstDAO<Endereco, Long> {

	@Inject
	public EnderecoDAO(EntityManager em) {
		super(em, Endereco.class, "descricao");
	}
}
