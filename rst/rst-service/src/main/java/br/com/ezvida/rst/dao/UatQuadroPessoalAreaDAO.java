package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatQuadroPessoalArea;
import fw.core.jpa.BaseDAO;

public class UatQuadroPessoalAreaDAO extends BaseDAO<UatQuadroPessoalArea, Long>{

  @Inject
  public UatQuadroPessoalAreaDAO(EntityManager em) {
      super(em, UatQuadroPessoalArea.class, "descricao");
  }
  
}
