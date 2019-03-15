package br.com.ezvida.rst.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatVeiculoTipoAtendimentoDAO;
import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import br.com.ezvida.rst.model.dto.UatVeiculoTipoAtendimentoDTO;
import fw.core.service.BaseService;

@Stateless
public class UatVeiculoTipoAtendimentoService extends BaseService{

  private static final long serialVersionUID = -642667528948172399L;

  private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoTipoAtendimentoService.class);
  
  @Inject
  private UatVeiculoTipoAtendimentoDAO uatVeiculoTipoAtendimentoDAO;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public List<UatVeiculoTipoAtendimentoDTO> listarTodos(ClienteAuditoria auditoria) {
	  LogAuditoria.registrar(LOGGER, auditoria, "Listando todos os UatVeiculoTipo");
      return parseToDTO(uatVeiculoTipoAtendimentoDAO.pesquisarTodos());
  }
  
  private List<UatVeiculoTipoAtendimentoDTO> parseToDTO(List<UatVeiculoTipoAtendimento> list) {
		return list.stream().map(temp -> new UatVeiculoTipoAtendimentoDTO(temp)).collect(Collectors.toList());
  }
}
