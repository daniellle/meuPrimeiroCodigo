package br.com.ezvida.rst.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import br.com.ezvida.rst.dao.EmpresaTrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EmpresaTrabalhadorFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class EmpresaTrabalhadorService extends BaseService {

	private static final long serialVersionUID = -2798635519642140307L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorService.class);

	@Inject
	private EmpresaTrabalhadorDAO empresaTrabalhadorDAO;
	
	@Inject
	private EmpresaTrabalhadorLotacaoService empresaTrabalhadorLotacaoService;
	
	@Inject
	private EmpresaService empresaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaTrabalhador pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando EmpresaTrabalhador por id");
		if (id == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		
		return empresaTrabalhadorDAO.pesquisarPorId(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmpresaTrabalhador> listarTodos() {
		LOGGER.debug("Listando todos os emailEmpresa");
		return empresaTrabalhadorDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<EmpresaTrabalhador> pesquisarPaginado(EmpresaTrabalhadorFilter empresaTrabalhadorFilter, 
			ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LOGGER.debug("Pesquisando Trabalhador por filtro paginado");
		
		Empresa empresa = empresaService.pesquisarPorId(empresaTrabalhadorFilter.getIdEmpresa(), dadosFilter);
		
		if (empresa == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		return empresaTrabalhadorDAO.pesquisarPaginado(empresaTrabalhadorFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EmpresaTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Listando todos os EmpresaTrabalhador por trabalhador");
		return empresaTrabalhadorDAO.pesquisarPorTrabalhador(idTrabalhador);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Set<EmpresaTrabalhador> pesquisarPorTrabalhadorCpf(String cpf) {
		LOGGER.debug("Listando todos os EmpresaTrabalhador por cpf");
		return empresaTrabalhadorDAO.pesquisarPorTrabalhador(cpf);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaTrabalhador salvar(EmpresaTrabalhador empresaTrabalhador, ClienteAuditoria clienteAuditoria) {
		String descricaoAuditoria = "Cadastro de empresa trabalhador: ";
		if (empresaTrabalhador.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de empresa trabalhador: ";
		}

		validar(empresaTrabalhador);
		setaSituacao(empresaTrabalhador);
		empresaTrabalhadorDAO.salvar(empresaTrabalhador);

		LogAuditoria.registrar(LOGGER, clienteAuditoria, descricaoAuditoria, empresaTrabalhador);
		return empresaTrabalhador;
	}

	private void setaSituacao(EmpresaTrabalhador empresaTrabalhador) {
		if(empresaTrabalhador != null && empresaTrabalhador.getSituacao() == null) {
			empresaTrabalhador.setSituacao(Situacao.ATIVO);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmpresaTrabalhador remover(EmpresaTrabalhador empresaTrabalhador, ClienteAuditoria clienteAuditoria) {
		LOGGER.debug("Removendo Empresa Trabalhador");
		EmpresaTrabalhador empresaTrabalhadorRetorno = null;
		if (empresaTrabalhador != null && empresaTrabalhador.getId() != null) {
			empresaTrabalhadorRetorno = pesquisarPorId(empresaTrabalhador.getId());
			if (empresaTrabalhadorRetorno.getDataExclusao() == null) {
				empresaTrabalhadorRetorno.setDataExclusao(new Date());
			}

			empresaTrabalhadorDAO.salvar(empresaTrabalhadorRetorno);

			String descricaoAuditoria = "Desativação de empresa trabalhador: ";
			LogAuditoria.registrar(LOGGER, clienteAuditoria, descricaoAuditoria, empresaTrabalhadorRetorno);
		}
		return empresaTrabalhadorRetorno;
	}

	private void validar(EmpresaTrabalhador empresaTrabalhador) {

		if (empresaTrabalhador != null) {
			validarData(empresaTrabalhador);
		}
	}

	private void validarData(EmpresaTrabalhador empresaTrabalhador) {
		if (DateUtils.truncate(empresaTrabalhador.getDataAdmissao(), Calendar.DAY_OF_MONTH)
				.after(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH))) {

			throw new BusinessErrorException(
					getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_admissao")));
		}

		if (empresaTrabalhador.getDataDemissao() != null
				&& DateUtils.truncate(empresaTrabalhador.getDataDemissao(), Calendar.DAY_OF_MONTH)
						.after(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH))) {

			throw new BusinessErrorException(
					getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_demissao")));
		}

		if (empresaTrabalhador.getDataDemissao() != null
				&& DateUtils.truncate(empresaTrabalhador.getDataDemissao(), Calendar.DAY_OF_MONTH)
						.before(DateUtils.truncate(empresaTrabalhador.getDataAdmissao(), Calendar.DAY_OF_MONTH))) {

			throw new BusinessErrorException(getMensagem("app_rst_data_demissao_menor_que_data_admissao",
					getMensagem("app_rst_data_demissao"), getMensagem("app_rst_data_admissao")));
		}

		if (empresaTrabalhador.getId() == null &&CollectionUtils.isNotEmpty(empresaTrabalhadorDAO.buscarEmpresaTrabalhadorNoPeriodo(empresaTrabalhador))) {
			throw new BusinessErrorException(getMensagem("app_rst_emp_tra_ja_cadastrado_no_periodo"));
		}
		
		if(empresaTrabalhador.getId() != null && empresaTrabalhador.getDataDemissao() != null && empresaTrabalhadorLotacaoService.verificarDataEmpresaTrabalhadorLotacaoSuperiorDemissao(empresaTrabalhador)) {
			throw new BusinessErrorException(getMensagem("app_rst_emp_tra_Lotacao_superior_demissao"));
		}
		
		if( empresaTrabalhador.getId() == null && empresaTrabalhadorDAO.buscarEmpresaTrabalhadorAtivo(empresaTrabalhador) != null) {
			
			throw new BusinessErrorException(getMensagem("app_rst_emp_tra_cadastrado"));
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpresaTrabalhador pesquisarPorIdEIdEmpresa(Long idEmpresaTrabalhador, Long idEmpresa) {
		return empresaTrabalhadorDAO.pesquisarPorIdEIdEmpresa(idEmpresaTrabalhador, idEmpresa);
	}
}
