package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaUnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.dao.UnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EnderecoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UnidAtendTrabalhadorFilter;
import br.com.ezvida.rst.model.EmpresaUnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class UnidadeAtendimentoTrabalhadorService extends BaseService {

	private static final long serialVersionUID = 891651132366509681L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeAtendimentoTrabalhadorService.class);

	@Inject
	private UnidadeAtendimentoTrabalhadorDAO unidadeAtendimentoTrabalhadorDAO;

	@Inject
	private EmailUnidadeAtendimentoTrabalhadorService emailUnidadeAtendimentoTrabalhadorService;

	@Inject
	private EnderecoUnidadeAtendimentoTrabalhadorService enderecoUnidadeAtendimentoTrabalhadorService;

	@Inject
	private TelefoneUnidadeAtendimentoTrabalhadorService telefoneUnidadeAtendimentoTrabalhadorService;

	@Inject
	private EmpresaUnidadeAtendimentoTrabalhadorDAO empresaUnidadeAtendimentoTrabalhadorDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeAtendimentoTrabalhador> pesquisarTodos(ClienteAuditoria auditoria) {
		LogAuditoria.registrar(LOGGER, auditoria, "Listando todas as Unidades de Atendimento ao Trabalhador.");
		return unidadeAtendimentoTrabalhadorDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<UnidadeAtendimentoTrabalhador> pesquisaPaginada(UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter,
			ClienteAuditoria auditoria, DadosFilter dados) {
		if (StringUtils.isNotBlank(unidAtendTrabalhadorFilter.getCnpj())
				&& unidAtendTrabalhadorFilter.getCnpj().length() != 14) {
			throw new BusinessErrorException(getMensagem("app_rst_cnpj_nao_esta_completo"));
		}

		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Unidades de Atendimento ao Trabalhador por filtro: ",
				unidAtendTrabalhadorFilter);

		return unidadeAtendimentoTrabalhadorDAO.pesquisarPaginado(unidAtendTrabalhadorFilter, dados);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UnidadeAtendimentoTrabalhador pesquisarPorId(Long id, ClienteAuditoria auditoria, DadosFilter dados) {

		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_id_consulta_nulo"));
		}

		UnidadeAtendimentoTrabalhador uat = unidadeAtendimentoTrabalhadorDAO.pesquisarPorId(id, dados);
		
		if (uat == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		
		if (uat != null) {
			uat.setTelefone(
					Sets.newHashSet(telefoneUnidadeAtendimentoTrabalhadorService.pesquisarPorIdUat(uat.getId())));
			uat.setEmail(Sets.newHashSet(emailUnidadeAtendimentoTrabalhadorService.pesquisarPorIdUat(uat.getId())));
			uat.setEndereco(
					Sets.newHashSet(enderecoUnidadeAtendimentoTrabalhadorService.pesquisarPorIdUat(uat.getId())));
		}

		if (auditoria != null) {
			LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Unidade de Atendimento ao Trabalhador por id: " + id);
		}
		return uat;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UnidadeAtendimentoTrabalhador salvar(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador,
			ClienteAuditoria auditoria, DadosFilter dados) {

		String descricaoAuditoria = "Cadastro de Unidade de Atendimento ao Trabalhador ";
		if (unidadeAtendimentoTrabalhador.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de Unidade de Atendimento ao Trabalhador: ";
		}

		validar(unidadeAtendimentoTrabalhador);

		if (!dados.isGestorDn() && !dados.isGestorDr()) {
			unidadeAtendimentoTrabalhadorDAO.salvar(unidadeAtendimentoTrabalhador);
			enderecoUnidadeAtendimentoTrabalhadorService.salvar(unidadeAtendimentoTrabalhador.getEndereco(), unidadeAtendimentoTrabalhador);
		}

		emailUnidadeAtendimentoTrabalhadorService.salvar(unidadeAtendimentoTrabalhador.getEmail(),
				unidadeAtendimentoTrabalhador);
		telefoneUnidadeAtendimentoTrabalhadorService.salvar(unidadeAtendimentoTrabalhador.getTelefone(),
				unidadeAtendimentoTrabalhador);

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, unidadeAtendimentoTrabalhador);
		return unidadeAtendimentoTrabalhador;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void validar(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {

		if (StringUtils.isNotEmpty(unidadeAtendimentoTrabalhador.getCnpj())
				&& !ValidadorUtils.isValidCNPJ(unidadeAtendimentoTrabalhador.getCnpj())) {
			throw new BusinessErrorException(getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
		}

		if (unidadeAtendimentoTrabalhador.getDataDesativacao() != null) {
			List<EmpresaUnidadeAtendimentoTrabalhador> EmpresasUat = empresaUnidadeAtendimentoTrabalhadorDAO
					.buscarPorUat(unidadeAtendimentoTrabalhador.getId());
			if (EmpresasUat.size() > 0) {
				throw new BusinessErrorException(getMensagem("app_rst_desativacao_cat"));
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeAtendimentoTrabalhador> pesquisarPorEndereco(EnderecoFilter enderecoFilter,
			ClienteAuditoria auditoria, DadosFilter dados) {
		return unidadeAtendimentoTrabalhadorDAO.pesquisarPorEndereco(enderecoFilter, dados);
	}

	public List<UnidadeAtendimentoTrabalhador> buscarPorEmpresaEAtivas(Long id) {
		return unidadeAtendimentoTrabalhadorDAO.buscarPorEmpresaEAtivas(id);
	}

}
