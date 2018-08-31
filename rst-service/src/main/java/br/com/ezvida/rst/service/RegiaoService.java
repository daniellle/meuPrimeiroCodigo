package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.RegiaoDAO;
import br.com.ezvida.rst.model.Regiao;
import fw.core.service.BaseService;

@Stateless
public class RegiaoService extends BaseService {

	private static final long serialVersionUID = -5405768745962510187L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RegiaoService.class);

	@Inject
	private RegiaoDAO regiaoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Regiao> listarTodos() {
		LOGGER.debug("Listando todas Regiões");
		return regiaoDAO.pesquisarTodosComPais();
	}

}
