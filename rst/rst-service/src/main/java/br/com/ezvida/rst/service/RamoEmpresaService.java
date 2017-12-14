package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.RamoEmpresaDAO;
import br.com.ezvida.rst.model.RamoEmpresa;
import fw.core.service.BaseService;

public class RamoEmpresaService extends BaseService {

	private static final long serialVersionUID = -1038732812688053362L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RamoEmpresaService.class);

	@Inject
	private RamoEmpresaDAO ramoEmpresaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RamoEmpresa> listarTodos() {
		LOGGER.debug("Listando todos os Ramos Empresa");
		return ramoEmpresaDAO.pesquisarTodos();
	}
}
