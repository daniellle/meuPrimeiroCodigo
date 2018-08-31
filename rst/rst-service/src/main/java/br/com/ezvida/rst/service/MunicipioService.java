package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import br.com.ezvida.rst.dao.MunicipioDAO;
import br.com.ezvida.rst.model.Municipio;
import fw.core.service.BaseService;

@Stateless
public class MunicipioService extends BaseService {

	private static final long serialVersionUID = -8134840144430038351L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioService.class);

	@Inject
	private MunicipioDAO municipioDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listarTodos() {
		LOGGER.debug("Listando todos Municípios");
		return municipioDAO.pesquisarTodosComEstado();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando Município por id");
		return municipioDAO.pesquisarPorId(id);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> pesquisarPorIdEstado(Long idEstado) {
		LOGGER.debug("Pesquisando Município por id de Estado");
		if (idEstado == null || idEstado <= 0) {
			return Lists.newArrayList();
		}

		return municipioDAO.pesquisarPorIdEstado(idEstado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> pesquisar(String descricao) {
		LOGGER.debug("Pesquisando Município por descricao");
		return municipioDAO.pesquisar(descricao);
	}

}
