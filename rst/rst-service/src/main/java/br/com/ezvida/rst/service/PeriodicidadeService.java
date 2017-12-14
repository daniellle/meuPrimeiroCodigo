package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.PeriodicidadeDAO;
import br.com.ezvida.rst.model.Periodicidade;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

public class PeriodicidadeService extends BaseService {

	private static final long serialVersionUID = 3285648470961523243L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PeriodicidadeService.class);

	@Inject
	private PeriodicidadeDAO periodicidadeDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Periodicidade pesquisarPorId(Long id, ClienteAuditoria auditoria) {

		if (id == null) {
			throw new UnauthorizedException(getMensagem("app_rst_id_consulta_nulo"));
		}

		Periodicidade periodicidade = periodicidadeDAO.pesquisarPorId(id);

		if (periodicidade == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Periodicidade por id: " + id);
		return periodicidade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Periodicidade> listarTodos(ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Listar todas Periodicidade");
		return periodicidadeDAO.listarTodos();
	}

}
