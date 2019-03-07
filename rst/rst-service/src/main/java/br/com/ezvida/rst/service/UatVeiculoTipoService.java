package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatVeiculoTipoDAO;
import br.com.ezvida.rst.model.UatVeiculoTipo;
import fw.core.service.BaseService;

@Stateless
public class UatVeiculoTipoService extends BaseService{

  private static final long serialVersionUID = -642667528948172399L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoTipoService.class);

  @Inject
  private UatVeiculoTipoDAO uatVeiculoTipoDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatVeiculoTipo> listarTodos(ClienteAuditoria auditoria) {
	  LogAuditoria.registrar(LOGGER, auditoria, "Listando todos os UatVeiculoTipo");
      return uatVeiculoTipoDAO.pesquisarTodos();
  }
  
}