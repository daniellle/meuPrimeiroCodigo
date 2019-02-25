package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatEquipamentoArea;
import fw.core.jpa.BaseDAO;

public class UatEquipamentoAreaDAO extends BaseDAO<UatEquipamentoArea, Long> {

	@Inject
	public UatEquipamentoAreaDAO(EntityManager em) {
		super(em, UatEquipamentoArea.class, "descricao");
	}

}
