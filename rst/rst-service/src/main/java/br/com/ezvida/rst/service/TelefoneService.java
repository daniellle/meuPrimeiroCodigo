package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.TelefoneDAO;
import br.com.ezvida.rst.model.Telefone;
import fw.core.service.BaseService;

@Stateless
public class TelefoneService extends BaseService {

	private static final long serialVersionUID = 4706580488659715699L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneService.class);

	@Inject
	private TelefoneDAO telefoneDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Telefone telefone) {
		LOGGER.debug("Salvando Telefone...");
		telefoneDAO.salvar(telefone);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desativarTelefone(Long id, List<Long> ids, Class<?> type, String fieldClass) {
		String[] fields = { fieldClass, "telefone" };
		int quantidade = telefoneDAO.desativar(id, ids, type, fields);
		LOGGER.debug("Foi desativado um total de {} em {}", quantidade, type.getSimpleName());
	}
}
