package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.CnaeDAO;
import br.com.ezvida.rst.dao.filter.CnaeFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Cnae;
import fw.core.service.BaseService;

@Stateless
public class CnaeService extends BaseService {

	private static final long serialVersionUID = 1739823855349346640L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CnaeService.class);

	@Inject
	private CnaeDAO cnaeDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Cnae> pesquisarPaginado(CnaeFilter cnaeFilter) {
		LOGGER.debug(" Buscando cnaes ... ");
		return cnaeDAO.pesquisaPorPaginado(cnaeFilter);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> buscarVersoes() {
		LOGGER.debug(" Buscando versoes ... ");
		return cnaeDAO.buscarVersoes();
	}
}
