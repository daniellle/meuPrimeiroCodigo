package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatEquipamentoTipo;
import fw.core.jpa.BaseDAO;

public class UatEquipamentoTipoDAO extends BaseDAO<UatEquipamentoTipo, Long>{

  @Inject
  public UatEquipamentoTipoDAO(EntityManager em) {
      super(em, UatEquipamentoTipo.class, "descricao");
  }
  
}
