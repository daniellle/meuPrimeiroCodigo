
package br.com.ezvida.rst.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaSindicatoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaSindicato;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaSindicatoService extends BaseService {

	private static final long serialVersionUID = 6248050901450102181L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaSindicatoService.class);

	@Inject
	private EmpresaSindicatoDAO empresaSindicatoDAO;
	
	@Inject
	private EmpresaService empresaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaSindicato pesquisarPorId(Long id, ClienteAuditoria auditoria) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		LogAuditoria.registrar(LOGGER, auditoria,"pesquisa de SindicatoEmpresa por id: " + id);
		return empresaSindicatoDAO.pesquisarPorId(id);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmpresaSindicato> pesquisarPorCnpj(Long idEmpresa, String cnpj,ClienteAuditoria auditoria) {
		if (cnpj == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		LogAuditoria.registrar(LOGGER, auditoria,"pesquisa de SindicatoEmpresa por cnpj: " + cnpj);
		return empresaSindicatoDAO.pesquisarAssociadosPorCNPJ(idEmpresa, cnpj);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmpresaSindicato> listarTodos() {
		return empresaSindicatoDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaSindicato> pesquisarPorEmpresa(EmpresaFilter empresaFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LOGGER.debug("Listando todos os sindicatosEmpresa por empresa");
		if (empresaFilter.getId() == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		Empresa empresa = empresaService.pesquisarPorId(empresaFilter.getId(), auditoria, dadosFilter);
		if (empresa == null) {
			throw new BusinessErrorException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de sindicatos empresa por filtro: ", empresaFilter);
		return empresaSindicatoDAO.pesquisarPorEmpresa(empresaFilter);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaSindicato salvar(Long idEmpresa, EmpresaSindicato sindicatoEmpresa, ClienteAuditoria auditoria) {
		
		String descricaoAuditoria = "Cadastro de Sindicato Empresa: ";
		if(sindicatoEmpresa.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de Sindicato Empresa: ";
		}
		validar(idEmpresa, sindicatoEmpresa);
		sindicatoEmpresa.setEmpresa(new Empresa(idEmpresa));
		empresaSindicatoDAO.salvar(sindicatoEmpresa);
		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, sindicatoEmpresa);
		return sindicatoEmpresa;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaSindicato remover(EmpresaSindicato empresaSindicato, ClienteAuditoria auditoria) {
		LOGGER.debug("Removendo Sindicato Empresa");
		EmpresaSindicato sindicatoEmpresaRetorno = null;
		if(empresaSindicato != null && empresaSindicato.getId() != null) {
			sindicatoEmpresaRetorno = empresaSindicatoDAO.pesquisarPorIdFetchAll(empresaSindicato.getId());
			if(sindicatoEmpresaRetorno.getDataExclusao() == null) {
				sindicatoEmpresaRetorno.setDataExclusao(new Date());
			}
			
			empresaSindicatoDAO.salvar(sindicatoEmpresaRetorno);
		}
		LogAuditoria.registrar(LOGGER, auditoria, "Desativação de empresa sindicato: ", sindicatoEmpresaRetorno);
		return sindicatoEmpresaRetorno;
	}
	
	private void validar(Long idEmpresa, EmpresaSindicato empresaSindicato) {
		
		if(empresaSindicato != null){
			validarSeDataFutura(empresaSindicato);
			
			validarSindicatoJaAssociado(idEmpresa, empresaSindicato);

			validarExisteSindicatoNaoDesligado(idEmpresa, empresaSindicato);

			validarExisteSindicatoAssociadoNoMesmoPeriodo(idEmpresa, empresaSindicato);

		}
		
	}

	private void validarSeDataFutura(EmpresaSindicato empresaSindicato) {
		if (DateUtils.truncate(empresaSindicato.getDataAssociacao(), Calendar.DAY_OF_MONTH)
				.after(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH))) {

			throw new BusinessErrorException(getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_associacao")));
		}

		if (empresaSindicato.getDataDesligamento() != null && DateUtils.truncate(empresaSindicato.getDataDesligamento(), Calendar.DAY_OF_MONTH)
				.after(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH))) {

			throw new BusinessErrorException(getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_desligamento")));
		}
	}

	private void validarExisteSindicatoNaoDesligado(Long idEmpresa, EmpresaSindicato empresaSindicato) {
		if (empresaSindicato.getDataDesligamento() == null) {

			EmpresaSindicato empresaSindicatoRetorno = empresaSindicatoDAO.buscarSindicatoEEmpresaNaoDesligado(idEmpresa);

			if (empresaSindicatoRetorno != null && !empresaSindicatoRetorno.getId().equals(empresaSindicato.getId())) {
				throw new BusinessErrorException(getMensagem("app_rst_existe_sindicato_ativo_na_empresa"));
			}
		}
	}

	private void validarExisteSindicatoAssociadoNoMesmoPeriodo(Long idEmpresa, EmpresaSindicato empresaSindicato) {
		// if (empresaSindicato.getDataDesligamento() != null) {

		if (CollectionUtils.isNotEmpty(empresaSindicatoDAO.buscarEmpresaSindicatoNoPeriodo(idEmpresa, empresaSindicato))) {
			throw new BusinessErrorException(getMensagem("app_rst_sindicato_ja_cadastrado_no_periodo"));
		}
		// }
	}

	private void validarSindicatoJaAssociado(Long idEmpresa, EmpresaSindicato empresaSindicato) {
		EmpresaSindicato empresaSindicatoRetorno = empresaSindicatoDAO.buscarPorSindicatoEEmpresa(empresaSindicato.getSindicato().getId(), idEmpresa);
		if (empresaSindicatoRetorno != null && !empresaSindicatoRetorno.getId().equals(empresaSindicato.getId())) {
			throw new BusinessErrorException(
					getMensagem("app_rst_registro_ja_associado", getMensagem("app_rst_label_sindicato"), getMensagem("app_rst_label_empresa")));
		}

	}
}
