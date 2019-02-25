package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatEquipamento;
import fw.core.jpa.BaseDAO;

public class UatEquipamentoDAO extends BaseDAO<UatEquipamento, Long>{

  @Inject
  public UatEquipamentoDAO(EntityManager em) {
      super(em, UatEquipamento.class);
  }
  
}
