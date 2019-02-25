package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatVeiculo;
import fw.core.jpa.BaseDAO;

public class UatVeiculoDAO extends BaseDAO<UatVeiculo, Long>{

  @Inject
  public UatVeiculoDAO(EntityManager em) {
      super(em, UatVeiculo.class);
  }
  
}
