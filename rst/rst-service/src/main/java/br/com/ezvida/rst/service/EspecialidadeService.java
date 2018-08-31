package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.EspecialidadeDAO;
import br.com.ezvida.rst.model.Especialidade;
import fw.core.service.BaseService;

@Stateless
public class EspecialidadeService extends BaseService {

	private static final long serialVersionUID = -4238909349925907076L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EspecialidadeService.class);

	@Inject
	private EspecialidadeDAO especialidadeDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Especialidade> listarTodos() {
		LOGGER.debug("Listando todos Especialidades...");
		return especialidadeDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Especialidade especialidade) {
		especialidadeDAO.salvar(especialidade);
	}

}
