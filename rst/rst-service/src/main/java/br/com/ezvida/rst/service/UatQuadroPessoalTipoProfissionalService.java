package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import br.com.ezvida.rst.dao.UatQuadroPessoalTipoProfissionalDAO;
import br.com.ezvida.rst.model.UatQuadroPessoalTipoProfissional;
import fw.core.service.BaseService;

@Stateless
public class UatQuadroPessoalTipoProfissionalService extends BaseService{

  private static final long serialVersionUID = 6919743145644039309L;
  
  @Inject
  private UatQuadroPessoalTipoProfissionalDAO uatQuadroPessoalTipoProfissionalDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatQuadroPessoalTipoProfissional> listarTodos() {  
      return uatQuadroPessoalTipoProfissionalDAO.pesquisarTodos();
  }
}
