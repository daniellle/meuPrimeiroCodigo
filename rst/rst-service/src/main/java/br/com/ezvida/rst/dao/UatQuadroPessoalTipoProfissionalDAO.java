package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatQuadroPessoalTipoProfissional;
import fw.core.jpa.BaseDAO;

public class UatQuadroPessoalTipoProfissionalDAO extends BaseDAO<UatQuadroPessoalTipoProfissional, Long>{

  @Inject
  public UatQuadroPessoalTipoProfissionalDAO(EntityManager em) {
      super(em, UatQuadroPessoalTipoProfissional.class, "descricao");
  }
  
}
