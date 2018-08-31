package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.List;
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
import br.com.ezvida.rst.dao.UnidadeAtendimentoTrabalhadorProdutoServicoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ProdutoServicoFilter;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.ProdutoServico;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhadorProdutoServico;
import fw.core.exception.BusinessErrorException;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class UnidadeAtendimentoTrabalhadorProdutoServicoService extends BaseService {

	private static final long serialVersionUID = 7073813496542848699L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UnidadeAtendimentoTrabalhadorProdutoServicoService.class);

	@Inject
	private UnidadeAtendimentoTrabalhadorProdutoServicoDAO uatProdutoServicoDAO;
	@Inject
	private ProdutoServicoService produtoServicoService;
	@Inject
	private UnidadeAtendimentoTrabalhadorService uatService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<ProdutoServico> pesquisarProdutoServicoPorDepartamento(ProdutoServicoFilter filter,
			ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando Produtos e Serviços por filtro.", filter);

		if (filter == null || filter.getId() == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}

		UnidadeAtendimentoTrabalhador uat = uatService.pesquisarPorId(filter.getId(), null, dadosFilter);

		return produtoServicoService.pesquisarPaginadoPorIdDepartamento(filter, uat.getDepartamentoRegional().getId(),
				auditoria, dadosFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<UnidadeAtendimentoTrabalhadorProdutoServico> retornarPorUat(
			ProdutoServicoFilter unidAtendTrabalhadorFilter, ClienteAuditoria auditoria) {
			LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando Produtos e Serviços por Unidade Sesi",
					unidAtendTrabalhadorFilter);
		return uatProdutoServicoDAO.retornarPorUat(unidAtendTrabalhadorFilter);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeAtendimentoTrabalhadorProdutoServico> retornarTodosPorUat(
			ProdutoServicoFilter unidAtendTrabalhadorFilter) {
		return uatProdutoServicoDAO.retornarTodosPorUat(unidAtendTrabalhadorFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<UnidadeAtendimentoTrabalhadorProdutoServico> salvar(Long id,
			Set<UnidadeAtendimentoTrabalhadorProdutoServico> uatProdutoServicos, ClienteAuditoria auditoria) {
		Set<UnidadeAtendimentoTrabalhadorProdutoServico> produtosSalvos = Sets.newHashSet();
		boolean sucess = false;
		UnidadeAtendimentoTrabalhador uat = new UnidadeAtendimentoTrabalhador();
		uat.setId(id);
		if (CollectionUtils.isNotEmpty(uatProdutoServicos)) {

			for (UnidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico : uatProdutoServicos) {
				if (uatProdutoServico.getId() == null) {
					if (uatProdutoServicoDAO.verificandoExistenciaProdutoServico(id,
							uatProdutoServico.getProdutoServico().getId()) == null) {
						uatProdutoServico.setUat(uat);
						this.salvar(uatProdutoServico);
						produtosSalvos.add(uatProdutoServico);
						sucess = true;
					}
				}
			}

		}

		if (!produtosSalvos.isEmpty()) {
			auditoria.setDescricao("Cadastro de produtos/serviços para a Unidade Sesi " + id + ": ");

			if (TipoOperacaoAuditoria.ALTERACAO.equals(auditoria.getTipoOperacao())) {
				auditoria.setDescricao("Alteração de produtos/serviços para a Unidade Sesi " + id + ": ");
			}

			LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), produtosSalvos);
		}

		if (!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		} else {
			return uatProdutoServicos;
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(UnidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico) {
		uatProdutoServicoDAO.salvar(uatProdutoServico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UnidadeAtendimentoTrabalhadorProdutoServico desativarUatProdutoServico(
			UnidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico, ClienteAuditoria auditoria) {
		boolean sucess = false;
		if (uatProdutoServico.getId() != null) {
			uatProdutoServico = uatProdutoServicoDAO.pesquisarPorId(uatProdutoServico.getId());
			if (uatProdutoServico.getDataExclusao() == null) {
				uatProdutoServico.setDataExclusao(new Date());
				this.salvar(uatProdutoServico);

				auditoria.setDescricao("Desativação de produtos/serviços para A Unidade Sesi "
						+ uatProdutoServico.getUat().getId() + ": ");
				LogAuditoria.registrar(LOGGER, auditoria, auditoria.getDescricao(), uatProdutoServico);

				sucess = true;
			}
		}

		if (!sucess) {
			throw new BusinessException(getMensagem("app_rst_generic_itens_not_add"));
		} else {
			return uatProdutoServico;
		}

	}
}
