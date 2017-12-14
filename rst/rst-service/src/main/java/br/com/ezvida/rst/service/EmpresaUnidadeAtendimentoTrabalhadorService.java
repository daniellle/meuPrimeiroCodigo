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

import br.com.ezvida.rst.dao.EmpresaUnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaUnidadeAtendimentoTrabalhador;
import fw.core.service.BaseService;

@Stateless
public class EmpresaUnidadeAtendimentoTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 8846560596637314867L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaUnidadeAtendimentoTrabalhadorService.class);

	@Inject
	private EmpresaUnidadeAtendimentoTrabalhadorDAO empresaUatDAO;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmpresaUnidadeAtendimentoTrabalhador> empresaUats, Empresa empresa) {
		LOGGER.debug("Salvando Unidades de Atendimento ao Trabalhador em Empresa ");

		List<Long> ids = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(empresaUats)) {
			for (EmpresaUnidadeAtendimentoTrabalhador empresaUat : empresaUats) {
				empresaUat.setEmpresa(empresa);
				salvar(empresaUat);
			}

			ids = empresaUats.stream().map(d -> d.getId()).collect(Collectors.toList());
		}

		empresaUatDAO.desativar(empresa.getId(), ids, "empresa");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmpresaUnidadeAtendimentoTrabalhador empresaUat) {
		LOGGER.debug("Salvando unidade de atendimento ao trabalhador ...");
		empresaUatDAO.salvar(empresaUat);
	}

	public List<EmpresaUnidadeAtendimentoTrabalhador> buscarPorEmpresa(Long id) {
		return empresaUatDAO.buscarPorEmpresa(id);
	}
}
