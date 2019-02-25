package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatInstalacaoFisicaCategoria;
import fw.core.jpa.BaseDAO;

public class UatInstalacaoFisicaCategoriaDAO extends BaseDAO<UatInstalacaoFisicaCategoria, Long> {
  
  @Inject
  public UatInstalacaoFisicaCategoriaDAO(EntityManager em) {
      super(em, UatInstalacaoFisicaCategoria.class, "descricao");
  }
  
}
