package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import br.com.ezvida.rst.dao.UatQuadroPessoalTipoServicoDAO;
import br.com.ezvida.rst.model.UatQuadroPessoalTipoServico;
import fw.core.service.BaseService;

@Stateless
public class UatQuadroPessoalTipoServicoService extends BaseService{

  private static final long serialVersionUID = 8796981225922092479L;
  
  @Inject
  private UatQuadroPessoalTipoServicoDAO uatQuadroPessoalTipoServicoDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatQuadroPessoalTipoServico> listarTodos() {  
      return uatQuadroPessoalTipoServicoDAO.pesquisarTodos();
  }
}
