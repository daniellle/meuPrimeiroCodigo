package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import br.com.ezvida.rst.dao.UatQuadroPessoalAreaDAO;
import br.com.ezvida.rst.model.UatQuadroPessoalArea;
import fw.core.service.BaseService;

@Stateless
public class UatQuadroPessoalAreaService extends BaseService{

  private static final long serialVersionUID = -642667528948172399L;

  @Inject
  private UatQuadroPessoalAreaDAO uatQuadroPessoalAreaDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatQuadroPessoalArea> listarTodos() {  
      return uatQuadroPessoalAreaDAO.pesquisarTodos();
  }

}
