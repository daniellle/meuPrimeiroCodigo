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
import br.com.ezvida.rst.dao.EmpresaSetorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SetorFilter;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaSetor;
import fw.core.exception.BusinessErrorException;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaSetorService extends BaseService {

	private static final long serialVersionUID = -4781750631885661955L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaSetorService.class);

	@Inject
	private EmpresaSetorDAO empresaSetorDAO;
	
	@Inject
	private EmpresaService empresaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaSetor pesquisarPorId(Long id) {
		return empresaSetorDAO.pesquisarPorId(id);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaSetor pesquisarPorId(Long id, ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria,  
				"pesquisa de setor por id: " + id);
		return empresaSetorDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<EmpresaSetor> salvar(Long idEmpresa, Set<EmpresaSetor> empresaSetores, ClienteAuditoria auditoria) {
		Set<EmpresaSetor> setoresSalvos = Sets.newHashSet();
		boolean sucess = false;
		Empresa empresa = new Empresa(idEmpresa);
		if (CollectionUtils.isNotEmpty(empresaSetores)) {
			for (EmpresaSetor setorEmpresa : empresaSetores) {
				if (setorEmpresa.getId() == null) {
					if(empresaSetorDAO.verificandoExistenciaSetor(idEmpresa,setorEmpresa.getSetor().getId()) == null) {
						setorEmpresa.setEmpresa(empresa);
						empresaSetorDAO.salvar(setorEmpresa);
						setoresSalvos.add(setorEmpresa);
						sucess = true;
					}
				}
			}
		}
		
		if (!setoresSalvos.isEmpty()) {
			String descricaoAuditoria = "Cadastro de empresa setor: ";

			if (TipoOperacaoAuditoria.ALTERACAO.equals(auditoria.getTipoOperacao())) {
				descricaoAuditoria = "Alteração no cadastro de empresa setor: ";
			}

			LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, setoresSalvos);
		}

		if(!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		}else {
			return CollectionUtils.isNotEmpty(empresaSetores) ? empresaSetores : Sets.newHashSet();
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaSetor desativar(EmpresaSetor empresaSetor, ClienteAuditoria auditoria) {
		EmpresaSetor empresaSetorRetorno = null;

		if (empresaSetor != null) {
			empresaSetorRetorno = empresaSetorDAO.pesquisarPorId(empresaSetor.getId());
		}

		if (empresaSetorRetorno != null && empresaSetorRetorno.getDataExclusao() == null) {
			empresaSetorRetorno.setDataExclusao(new Date());
		}
		
		empresaSetorDAO.salvar(empresaSetorRetorno);
		LogAuditoria.registrar(LOGGER, auditoria ,  "Desativação de empresa setor: ", empresaSetor);

		return empresaSetorRetorno;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaSetor> pesquisarPorPaginado(SetorFilter setorFilter, ClienteAuditoria auditoria
			, DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria, 
				"pesquisa de Empresas setores  por filtro: ", setorFilter);
		if (setorFilter.getIdEmpresa() == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		Empresa empresa = empresaService.pesquisarPorId(setorFilter.getIdEmpresa(), auditoria, dadosFilter);
		if (empresa == null) {
			throw new BusinessErrorException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		return empresaSetorDAO.pesquisarPorPaginado(setorFilter);
	}
}
