package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.ProfissaoDAO;
import br.com.ezvida.rst.model.Profissao;
import fw.core.service.BaseService;

@Stateless
public class ProfissaoService extends BaseService {

	private static final long serialVersionUID = -1703037137813702200L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfissaoService.class);

	@Inject
	private ProfissaoDAO profissaoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Profissao> listarTodos() {
		LOGGER.debug("Listando todas as Profissoes");
		return profissaoDAO.pesquisarTodos();
	}
}
