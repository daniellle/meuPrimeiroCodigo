package br.com.ezvida.rst.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.DepartamentoRegionalDAO;
import br.com.ezvida.rst.dao.UnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.DepartamentoRegionalFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class DepartamentoRegionalService extends BaseService {

	private static final long serialVersionUID = -459125870958649771L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoRegionalService.class);

	@Inject
	private DepartamentoRegionalDAO departamentoRegionalDAO;
	
	@Inject
	private UnidadeAtendimentoTrabalhadorDAO unidadeAtendimentoTrabalhadorDAO;

	@Inject
	private TelefoneDepartamentoRegionalService telefoneDepartamentoRegionalService;

	@Inject
	private EmailDepartamentoRegionalService emailDepartamentoRegionalService;

	@Inject
	private EnderecoDepartamentoRegionalService enderecoDepartamentoRegionalService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DepartamentoRegional pesquisarPorId(Long id, ClienteAuditoria auditoria) {
		
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}
		
		DepartamentoRegional departamentoRegional = departamentoRegionalDAO.pesquisarPorId(id);
		
		if (departamentoRegional == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}

		if (departamentoRegional != null) {
			departamentoRegional
			.setListaTelDepRegional(telefoneDepartamentoRegionalService.pesquisarPorDepartamentoRegional(id));
			
			departamentoRegional
			.setListaEmailDepRegional(emailDepartamentoRegionalService.pesquisarPorDepartamentoRegional(id));
			
			departamentoRegional.setListaEndDepRegional(
					Sets.newHashSet(enderecoDepartamentoRegionalService.pesquisarPorDepartamentoRegional(id)));
		}
		LogAuditoria.registrar(LOGGER, auditoria
				,"Pesquisar departamento regional por id: " + id);
		return departamentoRegional;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DepartamentoRegional> listarTodos(Situacao situacao, DadosFilter dados, DepartamentoRegionalFilter departamentoRegionalFilter) {		
		return departamentoRegionalDAO.listarTodos(situacao, dados, departamentoRegionalFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<DepartamentoRegional> pesquisarPaginado(
			DepartamentoRegionalFilter departamentoRegionalFilter, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		
		if (!departamentoRegionalFilter.getCnpj().isEmpty() && departamentoRegionalFilter.getCnpj().length() != 14) {
			throw new BusinessErrorException("CNPJ não está completo");
		}
		LogAuditoria.registrar(LOGGER, auditoria
				, "Pesquisar departamento regional por filtro: "
				, departamentoRegionalFilter);
		return departamentoRegionalDAO.pesquisarPaginado(departamentoRegionalFilter, dadosFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DepartamentoRegional salvar(DepartamentoRegional departamentoRegional, ClienteAuditoria auditoria, DadosFilter dados) {
		String descricaoAuditoria = "Cadastro de DR: ";
		if(departamentoRegional.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de DR: ";
		}

		if (departamentoRegional.getId() == null) {
			departamentoRegional.setDataCriacao(new Date());
		} else {
			departamentoRegional.setDataAlteracao(new Date());
		}
		
		validar(departamentoRegional);
		
		if (departamentoRegional.getId() != null && departamentoRegional.getDataDesativacao() != null) {
			validarUatAtivaAssociada(departamentoRegional.getId());
		}

		if (!dados.isGestorDr()) {
			departamentoRegionalDAO.salvar(departamentoRegional);
			telefoneDepartamentoRegionalService.salvar(departamentoRegional.getListaTelDepRegional(), departamentoRegional);
			emailDepartamentoRegionalService.salvar(departamentoRegional.getListaEmailDepRegional(), departamentoRegional);
			enderecoDepartamentoRegionalService.salvar(departamentoRegional.getListaEndDepRegional(), departamentoRegional);

		} else if (departamentoRegional.getId() != null) {
			DepartamentoRegional depto = departamentoRegionalDAO.pesquisarPorId(departamentoRegional.getId());
			depto.setNomeResponsavel(departamentoRegional.getNomeResponsavel());
			departamentoRegionalDAO.salvar(depto);
			telefoneDepartamentoRegionalService.salvar(departamentoRegional.getListaTelDepRegional(), departamentoRegional);
			emailDepartamentoRegionalService.salvar(departamentoRegional.getListaEmailDepRegional(), departamentoRegional);
		}

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, departamentoRegional);
		return departamentoRegional;
	}

	private void validar(DepartamentoRegional departamentoRegional) {		
		if (StringUtils.isNotEmpty(departamentoRegional.getCnpj())
				&& !ValidadorUtils.isValidCNPJ(departamentoRegional.getCnpj())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
		}

		DepartamentoRegional DRRetorno = departamentoRegionalDAO.pesquisarPorCNPJ(departamentoRegional.getCnpj());
		if (DRRetorno != null && !DRRetorno.getId().equals(departamentoRegional.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
					getMensagem("app_rst_label_departamento_regional"), getMensagem("app_rst_label_cnpj")));
		}
	}
	
	private void validarUatAtivaAssociada(Long departamentoRegionalId) {		
		List<UnidadeAtendimentoTrabalhador> drsRetorno = unidadeAtendimentoTrabalhadorDAO.pesquisarPorDepartamento(departamentoRegionalId);
		if (drsRetorno != null && !drsRetorno.isEmpty()) {
			throw new BusinessErrorException(getMensagem("app_rst_nao_foi_possivel_desativar_dr_dn"));
		}
	}
	
	public List<DepartamentoRegional> buscarPorTrabalhador(Long idTrabalhador) {
		return departamentoRegionalDAO.buscarPorTrabalhador(idTrabalhador);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DepartamentoRegional> pesquisarPorIds(Set<Long> ids) {
		LOGGER.debug("pesquisando departamentos por ids");

		if (CollectionUtils.isNotEmpty(ids)) {
			return departamentoRegionalDAO.pesquisarPorIds(ids);
		}

		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DepartamentoRegional> buscarDNPorSigla(){
		LOGGER.debug("pesquisando departamento nacional");

		return departamentoRegionalDAO.pesquisarDNPorSigla();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigInteger countByIdsAndCNPJ(Collection<Long> listId, String cnpj) {
		return departamentoRegionalDAO.countByIdsAndCNPJ(listId, cnpj);
	}
}
