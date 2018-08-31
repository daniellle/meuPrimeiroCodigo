package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.ConselhoRegionalDAO;
import br.com.ezvida.rst.model.ConselhoRegional;
import fw.core.service.BaseService;

@Stateless
public class ConselhoRegionalService extends BaseService {

	private static final long serialVersionUID = 8154029827961787936L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConselhoRegionalService.class);

	@Inject
	private ConselhoRegionalDAO conselhoRegionalDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ConselhoRegional> listarTodos() {
		LOGGER.debug("Listando todos Conselhos Regionais");
		return conselhoRegionalDAO.pesquisarTodos();
	}
}
