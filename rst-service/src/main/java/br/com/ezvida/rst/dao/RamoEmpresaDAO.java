package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.RamoEmpresa;
import fw.core.jpa.BaseDAO;

public class RamoEmpresaDAO extends BaseDAO<RamoEmpresa, Long> {

	@Inject
	public RamoEmpresaDAO(EntityManager em) {
		super(em, RamoEmpresa.class, "descricao");
	}
}
