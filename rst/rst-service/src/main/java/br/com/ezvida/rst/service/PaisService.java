package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.PaisDAO;
import br.com.ezvida.rst.model.Pais;
import fw.core.service.BaseService;

@Stateless
public class PaisService extends BaseService {

	private static final long serialVersionUID = -1505273669571931510L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaisService.class);

	@Inject
	private PaisDAO paisDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pais> listarTodos() {
		LOGGER.debug("Listando todos Países");
		return paisDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvar(Pais pais) {
		LOGGER.debug("Salvando todos País");
		paisDAO.salvar(pais);
	}

}
