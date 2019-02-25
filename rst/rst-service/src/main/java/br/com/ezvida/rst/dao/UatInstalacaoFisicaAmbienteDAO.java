package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatInstalacaoFisicaAmbiente;
import fw.core.jpa.BaseDAO;

public class UatInstalacaoFisicaAmbienteDAO extends BaseDAO<UatInstalacaoFisicaAmbiente, Long> {

  @Inject
  public UatInstalacaoFisicaAmbienteDAO(EntityManager em) {
      super(em, UatInstalacaoFisicaAmbiente.class, "descricao");
  }
  
}
