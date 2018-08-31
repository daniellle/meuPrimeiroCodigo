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

import br.com.ezvida.rst.dao.EmpresaCnaeDAO;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaCnae;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaCnaeService extends BaseService {

	private static final long serialVersionUID = -7737136028691316134L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaCnaeService.class);

	@Inject
	private EmpresaCnaeDAO empresaCnaeDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmpresaCnae> buscarPorEmpresa(Long idEmpresa) {
		LOGGER.debug("Listando todos os cnaesEmpresa por empresa");
		return empresaCnaeDAO.buscarPorEmpresa(idEmpresa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmpresaCnae empresaCnae) {
		LOGGER.debug("Salvando cnae Empresa");
		empresaCnaeDAO.salvar(empresaCnae);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Set<EmpresaCnae> empresaCnaes, Empresa empresa) {

		List<Long> ids = Lists.newArrayList();

		EmpresaCnae cnaePrincipalAtual = null;
		if (CollectionUtils.isNotEmpty(empresaCnaes)) {
			for (EmpresaCnae empresaCnae : empresaCnaes) {
				if (SimNao.SIM.equals(empresaCnae.getPrincipal())) {
					cnaePrincipalAtual = empresaCnae;
				}
				empresaCnae.setEmpresa(empresa);
				salvar(empresaCnae);
			}

			ids = empresaCnaes.stream().map(d -> d.getId()).collect(Collectors.toList());

		}

		empresaCnaeDAO.desativar(empresa.getId(), ids, "empresa");
		if (CollectionUtils.isNotEmpty(empresaCnaes)) {
		EmpresaCnae cnaePrincipalRetorno = empresaCnaeDAO.buscarPrincipal(empresa.getId());
		if (cnaePrincipalAtual == null) {
			throw new BusinessErrorException(getMensagem("app_rst_cnae_sem_principais"));
		}
		
		if (cnaePrincipalRetorno != null && !cnaePrincipalRetorno.getId().equals(cnaePrincipalAtual.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_cnae_principal_duplicado"));
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private EmpresaCnae buscarPrincipal(Long idEmpresa) {
		return empresaCnaeDAO.buscarPrincipal(idEmpresa);
	}

}
