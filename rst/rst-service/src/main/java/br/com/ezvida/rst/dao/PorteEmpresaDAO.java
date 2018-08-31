package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.PorteEmpresa;
import fw.core.jpa.BaseDAO;

public class PorteEmpresaDAO extends BaseDAO<PorteEmpresa, Long> {

	@Inject
	public PorteEmpresaDAO(EntityManager em) {
		super(em, PorteEmpresa.class, "descricao");
	}
}
