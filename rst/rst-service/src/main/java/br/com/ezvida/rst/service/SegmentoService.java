package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.SegmentoDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SegmentoFilter;
import br.com.ezvida.rst.model.Segmento;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class SegmentoService extends BaseService {

	private static final long serialVersionUID = 790503622787186935L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentoService.class);

	@Inject
	private SegmentoDAO segmentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Segmento pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando Segmento por id");
		if (id == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		return segmentoDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Segmento> pesquisarTodos() {
		LOGGER.debug("Listando todos os segmentos");
		return segmentoDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Segmento> pesquisarPaginado(SegmentoFilter segmentoFilter) {
		LOGGER.debug("Pesquisando Segmento por filtro");
		return segmentoDAO.pesquisarPaginado(segmentoFilter);
	}	
}
