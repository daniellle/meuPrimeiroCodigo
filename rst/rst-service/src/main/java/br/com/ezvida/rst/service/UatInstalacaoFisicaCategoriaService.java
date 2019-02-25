package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import br.com.ezvida.rst.dao.UatInstalacaoFisicaCategoriaDAO;
import br.com.ezvida.rst.model.UatInstalacaoFisicaCategoria;
import fw.core.service.BaseService;

@Stateless
public class UatInstalacaoFisicaCategoriaService extends BaseService{

  private static final long serialVersionUID = 3803409672517581214L;

  @Inject
  private UatInstalacaoFisicaCategoriaDAO uatInstalacaoFisicaCategoriaDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatInstalacaoFisicaCategoria> listarTodos() {  
      return uatInstalacaoFisicaCategoriaDAO.pesquisarTodos();
  }
}
