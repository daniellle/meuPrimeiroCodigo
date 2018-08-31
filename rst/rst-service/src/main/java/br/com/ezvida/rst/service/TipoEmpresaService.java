package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.TipoEmpresaDAO;
import br.com.ezvida.rst.model.TipoEmpresa;
import fw.core.service.BaseService;

@Stateless
public class TipoEmpresaService extends BaseService {

	private static final long serialVersionUID = 2353492468937463450L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TipoEmpresaService.class);

	@Inject
	private TipoEmpresaDAO tipoEmpresaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoEmpresa> listarTodos() {
		LOGGER.debug("Listando todos os Tipos Empresas");
		return tipoEmpresaDAO.pesquisarTodos();
	}
}
