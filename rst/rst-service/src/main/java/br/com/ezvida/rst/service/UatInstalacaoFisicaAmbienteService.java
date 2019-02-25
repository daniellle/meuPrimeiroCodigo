package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import br.com.ezvida.rst.dao.UatInstalacaoFisicaAmbienteDAO;
import br.com.ezvida.rst.model.UatInstalacaoFisicaAmbiente;
import fw.core.service.BaseService;

@Stateless
public class UatInstalacaoFisicaAmbienteService extends BaseService{


  private static final long serialVersionUID = 8692359712822744235L;

  @Inject
  private UatInstalacaoFisicaAmbienteDAO uatInstalacaoFisicaAmbienteDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatInstalacaoFisicaAmbiente> listarTodos() {  
      return uatInstalacaoFisicaAmbienteDAO.pesquisarTodos();
  }
  
}
