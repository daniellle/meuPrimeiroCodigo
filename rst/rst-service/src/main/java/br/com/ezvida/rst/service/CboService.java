package br.com.ezvida.rst.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.CboDAO;
import br.com.ezvida.rst.dao.filter.CboFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Cbo;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class CboService extends BaseService {

	private static final long serialVersionUID = 7669561553863123054L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CboService.class);

	@Inject
	private CboDAO cboDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Cbo> pesquisaPaginada(CboFilter cboFilter) {
		LOGGER.debug("Pesquisando paginada CBO...");		
		return cboDAO.pesquisaPaginada(cboFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cbo pesquisarPorId(Long l, ClienteAuditoria auditoria) {
		if (l == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}		
		LogAuditoria.registrar(LOGGER, auditoria,"pesquisa de " + getMensagem("app_rst_auditoria_funcionalidade_CARGO") + " por id: " + l);
		return cboDAO.pesquisarPorId(l);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Cbo> pesquisarPaginado(CboFilter cboFilter, ClienteAuditoria auditoria) {
		ListaPaginada<Cbo> listaPaginada = pesquisaPaginada(cboFilter);
		LogAuditoria.registrar(LOGGER, auditoria
				, "pesquisa de Unidades de Cargos por filtro: ", cboFilter);
		return listaPaginada;
	}

}
