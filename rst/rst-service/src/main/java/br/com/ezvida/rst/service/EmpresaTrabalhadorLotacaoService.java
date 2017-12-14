package br.com.ezvida.rst.service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaTrabalhadorLotacaoDAO;
import br.com.ezvida.rst.dao.filter.EmpresaTrabalhadorLotacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.model.EmpresaTrabalhadorLotacao;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaTrabalhadorLotacaoService extends BaseService {

	private static final long serialVersionUID = 4356739461801828538L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorLotacaoService.class);

	@Inject
	private EmpresaTrabalhadorLotacaoDAO empresaTrabalhadorLotacaoDAO;

	@Inject
	private EmpresaTrabalhadorService empresaTrabalhadorService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaTrabalhadorLotacao> pesquisarPaginado(
			EmpresaTrabalhadorLotacaoFilter empresaTrabalhadorLotacaoFilter, ClienteAuditoria auditoria) {
		LOGGER.debug("Pesquisando Trabalhador por filtro paginado");


		if (empresaTrabalhadorService.pesquisarPorIdEIdEmpresa(empresaTrabalhadorLotacaoFilter.getIdEmpresaTrabalhador(),
				empresaTrabalhadorLotacaoFilter.getIdEmpresa()) == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de empresa trabalhador lotação por filtro: ",
				empresaTrabalhadorLotacaoFilter);
		return empresaTrabalhadorLotacaoDAO.pesquisarPaginado(empresaTrabalhadorLotacaoFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaTrabalhadorLotacao pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando EmpresaTrabalhadorLotacao por id");
		if (id == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		return empresaTrabalhadorLotacaoDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaTrabalhadorLotacao salvar(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao,
			ClienteAuditoria auditoria) {
		String descricaoAuditoria = "Cadastro de empresa trabalhador lotação: ";
		if (empresaTrabalhadorLotacao.getId() != null) {
			descricaoAuditoria = "Alteração de empresa trabalhador lotação: ";
		}
		validar(empresaTrabalhadorLotacao);
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, empresaTrabalhadorLotacao);
		empresaTrabalhadorLotacaoDAO.salvar(empresaTrabalhadorLotacao);
		return empresaTrabalhadorLotacao;
	}

	private void validar(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao) {

		if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataAdmissao() != null) {
			if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataAdmissao()
					.after(empresaTrabalhadorLotacao.getDataAssociacao())) {
				throw new BusinessErrorException(getMensagem("app_rst_emp_tra_lot_fora_periodo"));
			}
		}

		 if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataDemissao() != null) {
		
			 if (empresaTrabalhadorLotacao.getDataDesligamento() != null) {
//				 throw new BusinessErrorException(getMensagem("app_rst_data_desligamento_obrigatoria"));				 
				 if (empresaTrabalhadorLotacao.getEmpresaTrabalhador().getDataDemissao()
							.before(empresaTrabalhadorLotacao.getDataDesligamento())) {
						throw new BusinessErrorException(getMensagem("app_rst_emp_tra_lot_fora_periodo"));					
				}
			 }
		 }
			 
		
		//
		// }
		// if (CollectionUtils.isNotEmpty(
		// empresaTrabalhadorLotacaoDAO.buscarEmpresaTrabalhadorLotacaoAtivo(empresaTrabalhadorLotacao)))
		// {
		// throw new
		// BusinessErrorException(getMensagem("app_rst_emp_tra_lot_ja_cadastrado_no_periodo"));
		// }
		//
		// if (CollectionUtils.isNotEmpty(
		// empresaTrabalhadorLotacaoDAO.buscarEmpresaTrabalhadorLotacaoNoPeriodo(empresaTrabalhadorLotacao)))
		// {
		// throw new
		// BusinessErrorException(getMensagem("app_rst_emp_tra_lot_ja_cadastrado_no_periodo"));
		// }

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaTrabalhadorLotacao remover(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao,
			ClienteAuditoria auditoria) {
		String descricaoAuditoria = "Removendo Empresa Trabalhador Lotação: ";
		EmpresaTrabalhadorLotacao empresaTrabalhadorRetorno = null;
		if (empresaTrabalhadorLotacao != null && empresaTrabalhadorLotacao.getId() != null) {
			empresaTrabalhadorRetorno = pesquisarPorId(empresaTrabalhadorLotacao.getId());
			if (empresaTrabalhadorRetorno.getDataExclusao() == null) {
				empresaTrabalhadorRetorno.setDataExclusao(new Date());
			}
			LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, empresaTrabalhadorLotacao);
			empresaTrabalhadorLotacaoDAO.salvar(empresaTrabalhadorRetorno);
		}
		return empresaTrabalhadorRetorno;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarDataEmpresaTrabalhadorLotacaoSuperiorDemissao(EmpresaTrabalhador empresaTrabalhador) {
		return CollectionUtils.isNotEmpty(empresaTrabalhadorLotacaoDAO
				.verificarDataEmpresaTrabalhadorLotacaoSuperiorDemissao(empresaTrabalhador));
	}
}
