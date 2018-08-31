package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.TipoEmpresa;
import fw.core.jpa.BaseDAO;

public class TipoEmpresaDAO extends BaseDAO<TipoEmpresa, Long> {

	@Inject
	public TipoEmpresaDAO(EntityManager em) {
		super(em, TipoEmpresa.class, "descricao");
	}
}
