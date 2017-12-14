package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.PorteEmpresaDAO;
import br.com.ezvida.rst.model.PorteEmpresa;
import fw.core.service.BaseService;

@Stateless
public class PorteEmpresaService extends BaseService {
	private static final long serialVersionUID = 507587647044729514L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PorteEmpresaService.class);

	@Inject
	private PorteEmpresaDAO porteEmpresaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PorteEmpresa> listarTodos() {
		LOGGER.debug("Listando todos os Portes Empresas");
		return porteEmpresaDAO.pesquisarTodos();
	}
}
