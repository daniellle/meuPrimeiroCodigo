package br.com.ezvida.rst.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.FuncaoDAO;
import br.com.ezvida.rst.dao.filter.FuncaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Funcao;
import fw.core.service.BaseService;

@Stateless
public class FuncaoService extends BaseService {

	private static final long serialVersionUID = -6043338504469403633L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FuncaoService.class);

	@Inject
	private FuncaoDAO funcaoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Funcao> pesquisarPaginado(FuncaoFilter funcaoFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, 
				"pesquisa de Unidades de funções por filtro: ", funcaoFilter);
		return funcaoDAO.pesquisaPorPaginado(funcaoFilter);
	}
}
