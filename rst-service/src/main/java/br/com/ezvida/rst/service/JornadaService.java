package br.com.ezvida.rst.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.JornadaDAO;
import br.com.ezvida.rst.dao.filter.JornadaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Jornada;
import fw.core.service.BaseService;

@Stateless
public class JornadaService extends BaseService {

	private static final long serialVersionUID = 7697657816716210406L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JornadaService.class);

	private static final String JORNADA = "Jornada";

	@Inject
	private JornadaDAO jornadaDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Jornada> pesquisaPorDescricaoHoras(JornadaFilter jornadaFilter){
		return jornadaDAO.pesquisaPorDescricaoHoras(jornadaFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Jornada pesquisarPorId(Long l, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de "+JORNADA+" por id: " + l);
		return jornadaDAO.pesquisarPorId(l);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<Jornada> pesquisarPaginado(JornadaFilter jornadaFilter,  ClienteAuditoria auditoria) {
		ListaPaginada<Jornada> listaPaginada = pesquisaPorDescricaoHoras(jornadaFilter);
		LogAuditoria.registrar(LOGGER, auditoria, 
				"pesquisa de Unidades de Jornadas por filtro: ", jornadaFilter);
		return listaPaginada;
	}

}
