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
import br.com.ezvida.rst.dao.EmpresaFuncaoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.FuncaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaFuncao;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaFuncaoService extends BaseService {

	private static final long serialVersionUID = -4958307804888370338L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaFuncaoService.class);

	@Inject
	private EmpresaFuncaoDAO empresaFuncaoDAO;
	
	@Inject
	private EmpresaService empresaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaFuncao> pesquisarPorPaginado(FuncaoFilter funcaoFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		
		LogAuditoria.registrar(LOGGER, auditoria,
				"pesquisa de Empresa Funções por filtro: ", funcaoFilter);
		
		Empresa empresa = empresaService.pesquisarPorId(funcaoFilter.getIdEmpresa(), auditoria, dadosFilter);
		
		if (empresa == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		return empresaFuncaoDAO.pesquisarPorPaginado(funcaoFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmpresaFuncao funcaoEmpresa) {
		LOGGER.debug("Salvando funcao Empresa");		
		empresaFuncaoDAO.salvar(funcaoEmpresa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<EmpresaFuncao> salvar(Long idEmpresa, Set<EmpresaFuncao> empresaFuncoes, ClienteAuditoria auditoria) {
		Set<EmpresaFuncao> funcoesSalvas = Sets.newHashSet();
		boolean sucess = false;
		Empresa empresa = new Empresa(idEmpresa);
		if (CollectionUtils.isNotEmpty(empresaFuncoes)) {
			for (EmpresaFuncao funcaoEmpresa : empresaFuncoes) {
				if (funcaoEmpresa.getId() == null) {
					if(empresaFuncaoDAO.verificandoExistenciaFuncao(idEmpresa,funcaoEmpresa.getFuncao().getId()) == null) {
						funcaoEmpresa.setEmpresa(empresa);
						funcoesSalvas.add(funcaoEmpresa);
						salvar(funcaoEmpresa);
						sucess = true;
					}
				}
			}
		}
		
		if (!funcoesSalvas.isEmpty()) {
			String descricaoAuditoria = "Associar função a empresa :" + idEmpresa;

			if (TipoOperacaoAuditoria.ALTERACAO.equals(auditoria.getTipoOperacao())) {
				descricaoAuditoria = "Alteração no cadastro de empresa setor: ";
			}

			LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, funcoesSalvas);
		}
		
		if(!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		}else {
			return CollectionUtils.isNotEmpty(empresaFuncoes) ? empresaFuncoes : Sets.newHashSet();
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaFuncao desativar(EmpresaFuncao empresaFuncao, ClienteAuditoria auditoria) {
		EmpresaFuncao empresaFuncaoRetorno = null;

		if (empresaFuncao != null) {
			empresaFuncaoRetorno = empresaFuncaoDAO.pesquisarPorId(empresaFuncao.getId());
		}
		
		if (empresaFuncaoRetorno != null && empresaFuncaoRetorno.getDataExclusao() == null) {
			empresaFuncaoRetorno.setDataExclusao(new Date());
			empresaFuncaoRetorno.getEmpresa().setId(empresaFuncaoDAO.getIdEmpresaAssociada(empresaFuncao.getId()));
			empresaFuncaoRetorno.getFuncao().setId(empresaFuncaoDAO.getIdFuncaoAssociada(empresaFuncao.getId()));
			empresaFuncaoDAO.salvar(empresaFuncaoRetorno);
			auditoria.setDescricao("Desativação de empresa função para a empresa: " +  empresaFuncaoRetorno.getEmpresa().getId());
			LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), empresaFuncaoRetorno);
		}
		
		return empresaFuncaoRetorno;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaFuncao pesquisarPorId(Long id) {
		return empresaFuncaoDAO.pesquisarPorId(id);
	}
}
