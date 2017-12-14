package br.com.ezvida.rst.service;

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
import br.com.ezvida.rst.dao.ProdutoServicoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ProdutoServicoFilter;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.model.ProdutoServico;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class ProdutoServicoService extends BaseService {

	private static final long serialVersionUID = 685966050649872646L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoServicoService.class);

	private static final String PRODUTOS_SERVICOS = "Produtos e Serviços";

	@Inject
	private ProdutoServicoDAO produtoServicoDAO;
	
	@Inject
	private EmpresaTrabalhadorService empresaTrabalhadorService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoServico> listarTodos() {
		return produtoServicoDAO.pesquisarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoServico buscarPorId(Long id, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		if (id == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		ProdutoServico produtoServico = produtoServicoDAO.pesquisarPorId(id, dadosFilter);
		if (produtoServico == null) {
			throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
		}
		LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de produtos e serviços por id: " + id);

		return produtoServico;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<ProdutoServico> pesquisarPaginado(ProdutoServicoFilter filter, ClienteAuditoria auditoria,
			DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando Produtos e Serviços por filtro.", filter);
		return produtoServicoDAO.pesquisarPaginado(filter, dadosFilter);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ListaPaginada<ProdutoServico> pesquisarPaginadoPorIdDepartamento(ProdutoServicoFilter filter,
			Long idDepartamento, ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando Produtos e Serviços por filtro.", filter);
		return produtoServicoDAO.pesquisarPaginadoPorIdDepartamento(filter, idDepartamento, dadosFilter);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProdutoServico salvar(ProdutoServico produtoServico, ClienteAuditoria auditoria) {

		if (produtoServico == null) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}

		String descricaoAuditoria = "Cadastro de " + PRODUTOS_SERVICOS + ": ";
		if (produtoServico.getId() != null) {
			descricaoAuditoria = "Alteração no cadastro de " + PRODUTOS_SERVICOS + ": ";
		}

		validar(produtoServico);
		produtoServicoDAO.salvar(produtoServico);

		LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, produtoServico);
		return produtoServico;
	}

	private void validar(ProdutoServico produtoServico) {
		ProdutoServico produtoCadastrado = produtoServicoDAO.buscarPorNomeELinha(produtoServico.getNome(),
				produtoServico.getLinha().getId());
		if (produtoCadastrado != null && !produtoCadastrado.getId().equals(produtoServico.getId())) {
			throw new BusinessErrorException(getMensagem("app_rst_registro_duplicado",
					getMensagem("app_rst_label_produto_servico"), getMensagem("app_rst_label_nome")));
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoServico> pesquisarSemPaginacao(ClienteAuditoria auditoria, DadosFilter dadosFilter) {
		
		if (dadosFilter.isTrabalhador()) {
			Set<EmpresaTrabalhador> empresas = empresaTrabalhadorService.pesquisarPorTrabalhadorCpf(auditoria.getUsuario());
			
			
			if (CollectionUtils.isNotEmpty(empresas)) {
				if (CollectionUtils.isEmpty(dadosFilter.getIdsEmpresa())) {
					dadosFilter.setIdsEmpresa(Sets.newHashSet());
				}
				for (EmpresaTrabalhador empresa : empresas) {
					dadosFilter.getIdsEmpresa().add(empresa.getEmpresa().getId());
				}
			}
		}
		
		return produtoServicoDAO.pesquisarSemPaginacao(dadosFilter);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoServico> buscarProdutosPorIdUat(String ids, DadosFilter dados) {

		if (ids == null || StringUtils.isBlank(ids)) {
			throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
		}
		
		List<ProdutoServico> produtoServico = produtoServicoDAO.buscarProdutoServicoPorIdUat(ids, dados);

		return produtoServico;
	}
}
