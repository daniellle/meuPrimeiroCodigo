package br.com.ezvida.rst.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.SetorDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SetorFilter;
import br.com.ezvida.rst.model.Setor;
import fw.core.service.BaseService;

@Stateless
public class SetorService extends BaseService {

	private static final long serialVersionUID = -5830713314109547821L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SetorService.class);

	@Inject
	private SetorDAO setorDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Setor> pesquisarPaginado(SetorFilter setorFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,"pesquisa de Unidades de Setores por filtro: ", setorFilter);
		return setorDAO.pesquisaPorPaginado(setorFilter);
	}
	
}
