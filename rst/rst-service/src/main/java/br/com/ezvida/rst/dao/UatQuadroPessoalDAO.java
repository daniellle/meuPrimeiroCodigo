package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatQuadroPessoal;
import fw.core.jpa.BaseDAO;

public class UatQuadroPessoalDAO extends BaseDAO<UatQuadroPessoal, Long>{

  @Inject
  public UatQuadroPessoalDAO(EntityManager em) {
      super(em, UatQuadroPessoal.class);
  }

}
