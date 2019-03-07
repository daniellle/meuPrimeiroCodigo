package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.UatEquipamentoTipoDAO;
import br.com.ezvida.rst.model.UatEquipamentoTipo;
import fw.core.service.BaseService;

@Stateless
public class UatEquipamentoTipoService extends BaseService{
  
  private static final long serialVersionUID = 2858427240479738621L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoTipoService.class);
  
  @Inject
  private UatEquipamentoTipoDAO uatEquipamentoTipoDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatEquipamentoTipo> listarTodos() {
	  LOGGER.info("Listando todos os Equipamentos Tipo");
      return uatEquipamentoTipoDAO.pesquisarTodos();
  }

}
