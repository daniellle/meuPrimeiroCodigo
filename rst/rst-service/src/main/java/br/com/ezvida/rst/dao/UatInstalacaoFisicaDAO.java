package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatInstalacaoFisica;
import fw.core.jpa.BaseDAO;

public class UatInstalacaoFisicaDAO extends BaseDAO<UatInstalacaoFisica, Long>{

  @Inject
  public UatInstalacaoFisicaDAO(EntityManager em) {
      super(em, UatInstalacaoFisica.class);
  }
  
}
