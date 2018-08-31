package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.EnderecoDAO;
import br.com.ezvida.rst.model.Endereco;
import fw.core.service.BaseService;

@Stateless
public class EnderecoService extends BaseService {

	private static final long serialVersionUID = -1356809675117724107L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoService.class);

	@Inject
	private EnderecoDAO enderecoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Endereco endereco) {
		LOGGER.debug("Salvando Endereco...");
		enderecoDAO.salvar(endereco);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int desativarEndereco(Long id, List<Long> ids, Class<?> type, String fieldClass) {
		String[] fields = { fieldClass, "endereco" };
		return enderecoDAO.desativar(id, ids, type, fields);
	}
}
