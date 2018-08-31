package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaCboDAO;
import br.com.ezvida.rst.dao.filter.CboFilter;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaCbo;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaCboService extends BaseService {
	
	private static final long serialVersionUID = 3104989606366103131L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaCboService.class);

	@Inject
	private EmpresaCboDAO empresaCboDAO;
	
	@Inject
	private EmpresaService empresaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaCbo> retornarPorPaginado(CboFilter empresaFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter)  {
		LogAuditoria.registrar(LOGGER, auditoria,
				"pesquisa de Empresas Cargos por filtro: ", empresaFilter);
		
		if (empresaFilter.getIdEmpresa() == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		
		Empresa empresa = empresaService.pesquisarPorId(empresaFilter.getIdEmpresa(), auditoria, dadosFilter);
			
		if (empresa == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		return empresaCboDAO.retornarPorEmpresa(empresaFilter);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaCbo pesquisarPorId(Long id) {
		return empresaCboDAO.pesquisarPorId(id);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaCbo pesquisarPorId(Long id, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de cargo por id: " + id);
		return empresaCboDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<EmpresaCbo> salvar(Long id ,Set<EmpresaCbo> emCbos, ClienteAuditoria auditoria) {
		Set<EmpresaCbo> empresasCbosSalvas = Sets.newHashSet();
		boolean sucess = false;
		Empresa em = new Empresa(id);
		if (CollectionUtils.isNotEmpty(emCbos)) {
			for (EmpresaCbo empresaCbo : emCbos) {
				if(empresaCbo.getId() == null) {
					if(empresaCboDAO.verificandoExistenciaCbo(id,empresaCbo.getCbo().getId()) == null) {
						empresaCbo.setEmpresa(em);
						empresaCboDAO.salvar(empresaCbo);
						empresasCbosSalvas.add(empresaCbo);
						sucess = true;
					}
				}
			}

		}

		String descricaoAuditoria = "Cadastro de cargo ";

		if (TipoOperacaoAuditoria.ALTERACAO.equals(auditoria.getTipoOperacao())) {
			descricaoAuditoria = "Alteração no cadastro de cargo ";
		}

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, empresasCbosSalvas);

		if(!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		}else {
			return emCbos;
		}
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaCbo desativarEmpresaCbo(EmpresaCbo empresaCbo, ClienteAuditoria auditoria) {
		if(empresaCbo != null && empresaCbo.getId() != null) {
			empresaCbo = pesquisarPorId(empresaCbo.getId());
			if(empresaCbo.getDataExclusao() == null) {
				empresaCbo.setDataExclusao(new Date());
				empresaCboDAO.salvar(empresaCbo);
				String descricaoAuditoria = "Desativação de empresa cbo: ";
				LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, empresaCbo);
			}
		}
		return empresaCbo;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmpresaCbo empresaCbo, ClienteAuditoria auditoria) {
		String descricaoAuditoria = "Cadastro de cargo ";
		if(empresaCbo.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de cargo ";
		}
		LogAuditoria.registrar(LOGGER, auditoria,  descricaoAuditoria, empresaCbo);
		empresaCboDAO.salvar(empresaCbo);
	}
	
}
