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
import br.com.ezvida.rst.dao.DepartamentoRegionalProdutoServicoDAO;
import br.com.ezvida.rst.dao.filter.DepartamentoRegionalFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.DepartamentoRegionalProdutoServico;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class DepartamentoRegionalProdutoServicoService extends BaseService {

	private static final long serialVersionUID = 3060043354781178142L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoRegionalProdutoServicoService.class);

	@Inject
	private DepartamentoRegionalProdutoServicoDAO departamentoRegionalProdutoServicoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<DepartamentoRegionalProdutoServico> retornarPorDepartamentoRegional(
			DepartamentoRegionalFilter departamentoRegionalFilter, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,
				"Pesquisando Produtos e Serviços por departamento Regional"
				,departamentoRegionalFilter);
		return departamentoRegionalProdutoServicoDAO.retornarPorDepartamentoRegional(departamentoRegionalFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<DepartamentoRegionalProdutoServico> salvar(Long id,
			Set<DepartamentoRegionalProdutoServico> departamentoRegionalProdutoServicos,
			ClienteAuditoria auditoria) {
		Set<DepartamentoRegionalProdutoServico> produtosSalvos = Sets.newHashSet();
		boolean sucess = false;
		DepartamentoRegional dep = new DepartamentoRegional();
		dep.setId(id);
		if (CollectionUtils.isNotEmpty(departamentoRegionalProdutoServicos)) {
			
			for (DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico : departamentoRegionalProdutoServicos) {
				if (departamentoRegionalProdutoServico.getId() == null) {
					if (departamentoRegionalProdutoServicoDAO.verificandoExistenciaProdutoServico(id,
							departamentoRegionalProdutoServico.getProdutoServico().getId()) == null) {
						departamentoRegionalProdutoServico.setDepartamentoRegional(dep);	
						this.salvar(departamentoRegionalProdutoServico);	
						produtosSalvos.add(departamentoRegionalProdutoServico);
						sucess = true;
					}
				}
			}

		}
		
		if (!produtosSalvos.isEmpty()) {
			auditoria.setDescricao("Cadastro de produtos/serviços para o DR " + id +": ");

			if (TipoOperacaoAuditoria.ALTERACAO.equals(auditoria.getTipoOperacao())) {
				auditoria.setDescricao("Alteração de produtos/serviços para o DR " + id + ": ");
			}

			LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), produtosSalvos);
		}

		if (!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		} else {					
			return departamentoRegionalProdutoServicos;
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico) {		
		departamentoRegionalProdutoServicoDAO.salvar(departamentoRegionalProdutoServico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DepartamentoRegionalProdutoServico desativarDepartamentoRegionalProdutoServico(
			DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico
			,ClienteAuditoria auditoria) {
		boolean sucess = false;
		if (departamentoRegionalProdutoServico.getId() != null) {
			departamentoRegionalProdutoServico = departamentoRegionalProdutoServicoDAO
					.pesquisarPorId(departamentoRegionalProdutoServico.getId());
			if (departamentoRegionalProdutoServico.getDataExclusao() == null) {
				departamentoRegionalProdutoServico.setDataExclusao(new Date());
				this.salvar(departamentoRegionalProdutoServico);
				
				auditoria.setDescricao("Desativação de produtos/serviços para o DR " 
						+ departamentoRegionalProdutoServico.getDepartamentoRegional().getId() + ": ");
				LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(),departamentoRegionalProdutoServico);
				
				sucess = true;
			}
		}
		
		if (!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		} else {
			return departamentoRegionalProdutoServico;
		}
		
		
	}

}
