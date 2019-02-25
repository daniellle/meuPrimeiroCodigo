package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import br.com.ezvida.rst.dao.UatVeiculoTipoAtendimentoDAO;
import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import fw.core.service.BaseService;

@Stateless
public class UatVeiculoTipoAtendimentoService extends BaseService{

  private static final long serialVersionUID = -642667528948172399L;

  @Inject
  private UatVeiculoTipoAtendimentoDAO uatVeiculoTipoAtendimentoDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatVeiculoTipoAtendimento> listarTodos() {  
      return uatVeiculoTipoAtendimentoDAO.pesquisarTodos();
  }
}
