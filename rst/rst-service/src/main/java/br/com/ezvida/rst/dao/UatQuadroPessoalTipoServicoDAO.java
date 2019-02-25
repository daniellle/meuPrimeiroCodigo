package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.ezvida.rst.model.UatQuadroPessoalTipoServico;
import fw.core.jpa.BaseDAO;

public class UatQuadroPessoalTipoServicoDAO extends BaseDAO<UatQuadroPessoalTipoServico, Long>{

  @Inject
  public UatQuadroPessoalTipoServicoDAO(EntityManager em) {
      super(em, UatQuadroPessoalTipoServico.class, "descricao");
  }
  
}
