package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import fw.core.jpa.BaseDAO;

public class UatVeiculoTipoAtendimentoDAO  extends BaseDAO<UatVeiculoTipoAtendimento, Long>{

  @Inject
  public UatVeiculoTipoAtendimentoDAO(EntityManager em) {
      super(em, UatVeiculoTipoAtendimento.class, "descricao");
  }
  
}
