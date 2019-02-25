package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatVeiculoTipo;
import fw.core.jpa.BaseDAO;

public class UatVeiculoTipoDAO extends BaseDAO<UatVeiculoTipo, Long>{

  @Inject
  public UatVeiculoTipoDAO(EntityManager em) {
      super(em, UatVeiculoTipo.class, "descricao");
  }
  
}
