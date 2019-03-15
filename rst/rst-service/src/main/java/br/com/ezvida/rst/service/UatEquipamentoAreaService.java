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
import br.com.ezvida.rst.dao.UatEquipamentoAreaDAO;
import br.com.ezvida.rst.model.UatEquipamentoArea;
import fw.core.service.BaseService;

@Stateless
public class UatEquipamentoAreaService extends BaseService {

  private static final long serialVersionUID = 3514698191441517496L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoAreaService.class);
  
  @Inject
  private UatEquipamentoAreaDAO uatEquipamentoAreaDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatEquipamentoArea> listarTodos(ClienteAuditoria auditoria) {  
	  LogAuditoria.registrar(LOGGER, auditoria, "Listando todos os UatEquipamentoArea");
      return uatEquipamentoAreaDAO.pesquisarTodos();
  }

}
