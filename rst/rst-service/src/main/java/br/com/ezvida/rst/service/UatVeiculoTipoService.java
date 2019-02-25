package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import br.com.ezvida.rst.dao.UatVeiculoTipoDAO;
import br.com.ezvida.rst.model.UatVeiculoTipo;
import fw.core.service.BaseService;

@Stateless
public class UatVeiculoTipoService extends BaseService{

  private static final long serialVersionUID = -642667528948172399L;

  @Inject
  private UatVeiculoTipoDAO uatVeiculoTipoDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatVeiculoTipo> listarTodos() {  
      return uatVeiculoTipoDAO.pesquisarTodos();
  }
  

}