package br.com.ezvida.rst.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import br.com.ezvida.rst.dao.TelefoneEmpresaDAO;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.TelefoneEmpresa;
import fw.core.service.BaseService;

@Stateless
public class TelefoneEmpresaService extends BaseService {

	private static final long serialVersionUID = 1190922456453806644L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneEmpresaService.class);

	@Inject
	private TelefoneEmpresaDAO telefoneEmpresaDAO;

	@Inject
	private TelefoneService telefoneService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<TelefoneEmpresa> telefonesEmpresa, Empresa empresa) {
		LOGGER.debug("Salvando Telefones Empresa");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(telefonesEmpresa)) {
			for (TelefoneEmpresa telefoneEmpresa : telefonesEmpresa) {
				telefoneEmpresa.setEmpresa(empresa);
				telefoneService.salvar(telefoneEmpresa.getTelefone());
				salvar(telefoneEmpresa);
			}

			ids = telefonesEmpresa.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		telefoneService.desativarTelefone(empresa.getId(), ids, TelefoneEmpresa.class, "empresa");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TelefoneEmpresa telefoneEmpresa) {
		LOGGER.debug("Salvando Telefone Endereco...");
		telefoneEmpresaDAO.salvar(telefoneEmpresa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TelefoneEmpresa> buscarPorEmpresa(Long id) {
		return telefoneEmpresaDAO.buscarPorEmpresa(id);
	}
}
