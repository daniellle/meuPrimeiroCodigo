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

import br.com.ezvida.rst.dao.EnderecoEmpresaDAO;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EnderecoEmpresa;
import fw.core.service.BaseService;

@Stateless
public class EnderecoEmpresaService extends BaseService {

	private static final long serialVersionUID = -5130597528390059473L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoEmpresaService.class);

	@Inject
	private EnderecoEmpresaDAO enderecoEmpresaDAO;

	@Inject
	private EnderecoService enderecoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EnderecoEmpresa> enderecosEmpresa, Empresa empresa) {
		LOGGER.debug("Salvando Enderecos Empresa");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(enderecosEmpresa)) {
			for (EnderecoEmpresa enderecoEmpresa : enderecosEmpresa) {
				enderecoEmpresa.setEmpresa(empresa);
				enderecoService.salvar(enderecoEmpresa.getEndereco());
				salvar(enderecoEmpresa);
			}

			ids = enderecosEmpresa.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		enderecoService.desativarEndereco(empresa.getId(), ids, EnderecoEmpresa.class, "empresa");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnderecoEmpresa enderecoEmpresa) {
		LOGGER.debug("Salvando Endereco Empresa...");
		enderecoEmpresaDAO.salvar(enderecoEmpresa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoEmpresa> buscarPorEmpresa(Long id) {
		return enderecoEmpresaDAO.buscarPorEmpresa(id);
	}

}
