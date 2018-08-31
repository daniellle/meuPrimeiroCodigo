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
import br.com.ezvida.rst.dao.EmpresaLotacaoDAO;
import br.com.ezvida.rst.dao.filter.EmpresaLotacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.EmpresaCbo;
import br.com.ezvida.rst.model.EmpresaFuncao;
import br.com.ezvida.rst.model.EmpresaJornada;
import br.com.ezvida.rst.model.EmpresaLotacao;
import br.com.ezvida.rst.model.EmpresaSetor;
import br.com.ezvida.rst.model.UnidadeObra;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaLotacaoService extends BaseService {

	private static final long serialVersionUID = -1438469853091953265L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaLotacaoService.class);

	@Inject
	private EmpresaLotacaoDAO empresaLotacaoDAO;

	@Inject
	private EmpresaSetorService empresaSetorService;

	@Inject
	private EmpresaFuncaoService empresaFuncaoService;

	@Inject
	private EmpresaCboService empresaCboService;

	@Inject
	private EmpresaJornadaService empresaJornadaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaLotacao> pesquisarEmpresaLotacaoPaginada(EmpresaLotacaoFilter empresaLotacaoFilter,
			ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de lotação por filtro: ", empresaLotacaoFilter);
		return empresaLotacaoDAO.pesquisarPorEmpresa(empresaLotacaoFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EmpresaLotacao lotacaoEmpresa) {
		LOGGER.debug("Salvando lotacoes Empresa");
		verificarEmpresaLotacaoVinculada(lotacaoEmpresa);
		validarCboAtivo(lotacaoEmpresa.getEmpresaCbo());
		validarFuncaoAtiva(lotacaoEmpresa.getEmpresaFuncao());
		validarJornadaAtiva(lotacaoEmpresa.getEmpresaJornada());
		validarSetorAtivo(lotacaoEmpresa.getEmpresaSetor());
		validarObra(lotacaoEmpresa.getUnidadeObra());
		empresaLotacaoDAO.salvar(lotacaoEmpresa);
	}

	public void verificarEmpresaLotacaoVinculada(EmpresaLotacao empresaLotacao) {
		EmpresaLotacao empresaLotacaoExistente = empresaLotacaoDAO.pesquisarEmpresaLotacaoVinculada(empresaLotacao);
		if (empresaLotacaoExistente != null) {
			throw new BusinessErrorException(getMensagem("app_rst_lotacao_cadastrada_para_esta_empresa"));
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<EmpresaLotacao> salvar(Set<EmpresaLotacao> empresaLotacoes, ClienteAuditoria auditoria) {
		Set<EmpresaLotacao> lotacoesSalvas = Sets.newHashSet();
		Long idEmpresa = null;
		if (CollectionUtils.isNotEmpty(empresaLotacoes)) {
			for (EmpresaLotacao lotacaoEmpresa : empresaLotacoes) {
				if (lotacaoEmpresa.getId() == null) {					
					salvar(lotacaoEmpresa);
					if (idEmpresa == null) {
						idEmpresa = empresaLotacaoDAO.getIdEmpresaAssociada(lotacaoEmpresa.getId());
					}
					lotacoesSalvas.add(lotacaoEmpresa);
				}
			}
		}
		
		if (!lotacoesSalvas.isEmpty()) {
			 
			String descricaoAuditoria = "Associar lotação para empresa " + idEmpresa + ": ";

			if (TipoOperacaoAuditoria.ALTERACAO.equals(auditoria.getTipoOperacao())) {
				descricaoAuditoria = "Alteração na lotação para empresa " + idEmpresa + ": ";
			}

			LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, lotacoesSalvas);
		}
		
		return CollectionUtils.isNotEmpty(empresaLotacoes) ? empresaLotacoes : Sets.newHashSet();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaLotacao desativar(EmpresaLotacao empresaLotacao, ClienteAuditoria auditoria) {
		EmpresaLotacao empresaLotacaoRetorno = null;		

		if (empresaLotacao != null) {
			empresaLotacaoRetorno = empresaLotacaoDAO.pesquisarPorId(empresaLotacao.getId());
		}

		if (empresaLotacaoRetorno != null && empresaLotacaoRetorno.getDataExclusao() == null) {
			empresaLotacaoRetorno.setDataExclusao(new Date());
			
			
			empresaLotacaoDAO.salvar(empresaLotacao);
			auditoria.setDescricao("Desativação de lotação para a empresa " + empresaLotacaoDAO.getIdEmpresaAssociada(empresaLotacao.getId()) + ": ");
			LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), empresaLotacao);
		}
		return empresaLotacao;
	}

	private void validarCboAtivo(EmpresaCbo empresaCbo) {
		if (empresaCbo != null) {
			EmpresaCbo cbo = empresaCboService.pesquisarPorId(empresaCbo.getId());
			if (cbo != null && cbo.getDataExclusao() != null) {
				throw new BusinessErrorException(getMensagem("app_rst_cbo_nao_ativo"));
			}
		}
	}

	private void validarSetorAtivo(EmpresaSetor empresaSetor) {
		if (empresaSetor != null) {
			EmpresaSetor setor = empresaSetorService.pesquisarPorId(empresaSetor.getId());
			if (setor != null && setor.getDataExclusao() != null) {
				throw new BusinessErrorException(getMensagem("app_rst_setor_nao_ativo"));
			}
		}
	}

	private void validarFuncaoAtiva(EmpresaFuncao empresaFuncao) {
		if (empresaFuncao != null) {
			EmpresaFuncao funcao = empresaFuncaoService.pesquisarPorId(empresaFuncao.getId());
			if (funcao != null && funcao.getDataExclusao() != null) {
				throw new BusinessErrorException(getMensagem("app_rst_funcao_nao_ativo"));
			}
		}
	}

	private void validarObra(UnidadeObra unidadeObra) {
		if (unidadeObra != null && unidadeObra.getDataExclusao() != null) {
			throw new BusinessErrorException(getMensagem("app_rst_funcao_nao_ativo"));
		}
	}

	private void validarJornadaAtiva(EmpresaJornada empresaJornada) {
		if (empresaJornada != null) {
			EmpresaJornada jornada = empresaJornadaService.pesquisarPorId(empresaJornada.getId());
			if (jornada != null && jornada.getDataExclusao() != null) {
				throw new BusinessErrorException(getMensagem("app_rst_jornada_nao_ativo"));
			}
		}
	}
}
