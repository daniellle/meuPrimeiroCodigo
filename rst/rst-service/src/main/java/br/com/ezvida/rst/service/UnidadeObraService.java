package br.com.ezvida.rst.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.rst.utils.ValidadorUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import br.com.ezvida.rst.dao.UnidadeObraDAO;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.UnidadeObra;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class UnidadeObraService extends BaseService {

	private static final long serialVersionUID = -8735307869641252189L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeObraService.class);

	@Inject
	private UnidadeObraDAO unidadeObraDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UnidadeObra pesquisarPorId(Long id) {
		LOGGER.debug("Buscar trabalhador por Id");

		if (id == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		return unidadeObraDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeObra> buscarPorEmpresaPorNome(Long id, String nome){
		LOGGER.debug("Buscar unidade obra por nome na empresa");

		if(id == null){
			throw new BusinessErrorException("Id de consulta está nulo.");
		}

		return unidadeObraDAO.buscarPorNome(nome, id);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeObra> listarTodos() {
		LOGGER.debug("Listando todos os unidade obra");
		return unidadeObraDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<UnidadeObra> unidadeObras, Empresa empresa) {
		LOGGER.debug("Salvando Telefones Empresa");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(unidadeObras)) {
			for (UnidadeObra unidadeObra : unidadeObras) {
				unidadeObra.setEmpresa(empresa);
				salvar(unidadeObra);
			}

			ids = unidadeObras.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		desativarUnidadeObra(empresa.getId(), ids);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desativarUnidadeObra(Long id, List<Long> ids) {
		int quantidade = unidadeObraDAO.desativar(id, ids, "empresa");
		LOGGER.debug("Foi desativado um total de {}", quantidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(UnidadeObra unidadeObra) {
		LOGGER.debug("Salvando Unidade Obra...");
		validar(unidadeObra);
		unidadeObraDAO.salvar(unidadeObra);
	}
	
	private void validar(UnidadeObra unidadeObra) {
		LOGGER.debug("Validando Unidade Obra...");

		UnidadeObra unidadeObraRetorno = unidadeObraDAO.buscarPorCei(unidadeObra.getCei());
		if (unidadeObraRetorno != null && !unidadeObraRetorno.getId().equals(unidadeObra.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
					getMensagem("app_rst_label_unidade_obra"), getMensagem("app_rst_label_cei")));
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeObra> buscarPorEmpresa(Long id) {
		return unidadeObraDAO.buscarPorEmpresa(id);
	}

}
