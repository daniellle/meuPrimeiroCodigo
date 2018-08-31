package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.EstadoDAO;
import br.com.ezvida.rst.model.Estado;
import fw.core.service.BaseService;

@Stateless
public class EstadoService extends BaseService {

	private static final long serialVersionUID = 8154029827961787936L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EstadoService.class);

	@Inject
	private EstadoDAO estadoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Estado> listarTodos() {
		LOGGER.debug("Listando todos Estados");
		return estadoDAO.pesquisarTodosComPais();
	}

}
