package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ProdutoServicoFilter;
import br.com.ezvida.rst.model.ProdutoServico;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class ProdutoServicoDAO extends BaseDAO<ProdutoServico, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoServicoDAO.class);

	private static final String ORDER_BY = "descricao";

	@Inject
	public ProdutoServicoDAO(EntityManager em) {
		super(em, ProdutoServico.class, ORDER_BY);
	}

	public ProdutoServico pesquisarPorId(Long id, DadosFilter segurancaFilter) {
		LOGGER.debug("Buscar ProdutoServico por Id");
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		jpql.append("select produtoServico from ProdutoServico produtoServico ");
		jpql.append(" inner join fetch produtoServico.linha linha ");

		jpql.append(
				" left join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos");
		jpql.append(" left join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional");

		jpql.append(" where produtoServico.id = :id");

		parametros.put("id", id);
		if (segurancaFilter != null && !segurancaFilter.isAdministrador() && segurancaFilter.temIdsDepRegional()) {
			jpql.append("  and departamentoRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}
		TypedQuery<ProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<ProdutoServico> pesquisarPaginado(ProdutoServicoFilter filter, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Produtos e Servicos por filtro");

		ListaPaginada<ProdutoServico> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, filter, false, segurancaFilter);
		TypedQuery<ProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(filter, segurancaFilter));

		if (filter != null) {
			query.setFirstResult((filter.getPagina() - 1) * filter.getQuantidadeRegistro());
			query.setMaxResults(filter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}
	
	public ListaPaginada<ProdutoServico> pesquisarPaginadoPorIdDepartamento(ProdutoServicoFilter filter, Long idDepartamento, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Produtos e Servicos por filtro");

		ListaPaginada<ProdutoServico> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginadoDepartamento(jpql, parametros, filter, idDepartamento, false, segurancaFilter);
		TypedQuery<ProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginadoDepartamento(filter, idDepartamento, segurancaFilter));

		if (filter != null) {
			query.setFirstResult((filter.getPagina() - 1) * filter.getQuantidadeRegistro());
			query.setMaxResults(filter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public ProdutoServico buscarPorNomeELinha(String nome, Long idLinha) {
		LOGGER.debug("Pesquisando Produto e Serviço por Nome");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select produtoServico from ProdutoServico produtoServico ");
		jpql.append(" inner join fetch produtoServico.linha linha ");
		jpql.append(" where ");
		jpql.append(" upper(produtoServico.nome) = :nome and ");
		jpql.append(" linha.id = :idLinha");
		TypedQuery<ProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("nome", nome.toUpperCase());
		query.setParameter("idLinha", idLinha);

		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, ProdutoServicoFilter filter,
			boolean count, DadosFilter segurancaFilter) {

		boolean nome = false;
		boolean linha = false;
		boolean filtroIdDep = segurancaFilter != null && filter.isAplicarDadosFilter() && segurancaFilter.temIdsDepRegional();
		boolean filtroIdEmp  = segurancaFilter != null && filter.isAplicarDadosFilter() &&  segurancaFilter.temIdsEmpresa();
		
		montarJoinPaginado(jpql, filter, count, segurancaFilter);
		
		if (filter != null) {
			nome = StringUtils.isNotBlank(filter.getNome());
			linha = filter.getIdLinha() != null;
		}
		
		addWhere(jpql, filter, segurancaFilter, nome, linha);
		montarFiltroNomeLinha(jpql, parametros, filter, nome, linha);
		montarSegurancaFilterIds(jpql, parametros, segurancaFilter, nome, linha, filtroIdDep, filtroIdEmp);

		if (!count) {
			jpql.append(" order by linha.descricao ");
		}
	}

	private void addWhere(StringBuilder jpql, ProdutoServicoFilter filter, DadosFilter segurancaFilter, boolean nome,
			boolean linha) {
		if (segurancaFilter != null && filter.isAplicarDadosFilter() && (!segurancaFilter.isAdministrador() && segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) || nome || linha) {
			jpql.append("  where ");
		}
	}

	private void montarFiltroNomeLinha(StringBuilder jpql, Map<String, Object> parametros, ProdutoServicoFilter filter,
			boolean nome, boolean linha) {
		if (nome) {
			jpql.append(" set_simple_name(UPPER(produtoServico.nome)) like set_simple_name(:nome) escape :sc");
			parametros.put("sc", "\\");
			parametros.put("nome", "%" + filter.getNome().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
		}
		if (linha) {
			if (nome) {
				jpql.append(" and ");
			}

			jpql.append(" linha.id = :idLinha");
			parametros.put("idLinha", filter.getIdLinha());
		}
	}
	
	private void montarSegurancaFilterIds(StringBuilder jpql, Map<String, Object> parametros,
			 DadosFilter segurancaFilter, boolean nome, boolean linha , 
			boolean filtroIdDep, boolean filtroIdEmp) {
		
		if (filtroIdDep && !segurancaFilter.isAdministrador()) {

			if (nome || linha) {
				jpql.append(" and ");
			}
			jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}
		
		if (filtroIdEmp && !segurancaFilter.isAdministrador()) {

			if (nome || linha || segurancaFilter.temIdsDepRegional()) {
				jpql.append(" and ");
			}
			jpql.append(" empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}
	}

	private void montarJoinPaginado(StringBuilder jpql, ProdutoServicoFilter filter, boolean count,
			DadosFilter segurancaFilter) {
		if (count) {
			jpql.append("select count(DISTINCT produtoServico.id) from ProdutoServico produtoServico ");
			jpql.append(" inner join produtoServico.linha linha ");
		} else {
			jpql.append("select DISTINCT produtoServico from ProdutoServico produtoServico ");
			jpql.append(" inner join fetch produtoServico.linha linha ");
		}

		if (segurancaFilter != null && filter.isAplicarDadosFilter() && (!segurancaFilter.isAdministrador() && segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa())) {
			jpql.append(" inner join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos");
			jpql.append(" inner join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional");
		}
		
		if (segurancaFilter != null && filter.isAplicarDadosFilter() && !segurancaFilter.isAdministrador() && segurancaFilter.temIdsEmpresa()) {
			jpql.append(" inner join departamentoRegional.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador");
			jpql.append(" inner join unidadeAtendimentoTrabalhador.empresaUats empresaUats");
			jpql.append(" inner join empresaUats.empresa empresa");
		}
	}
	
	private void getQueryPaginadoDepartamento(StringBuilder jpql, Map<String, Object> parametros, ProdutoServicoFilter filter, Long idDepartamento,
			boolean count, DadosFilter segurancaFilter) {

		boolean nome = false;
		boolean linha = false;
		
		montarJoinPaginadoDepartamento(jpql, idDepartamento, count);
		
		if (filter != null) {
			nome = StringUtils.isNotBlank(filter.getNome());
			linha = filter.getIdLinha() != null;
		}
		boolean temWhere = nome || linha || idDepartamento != null;
		if (temWhere) {
			jpql.append("  where ");
		}

		montarFiltroNomeLinha(jpql, parametros, filter, nome, linha);

		if (idDepartamento != null) {

			if (nome || linha) {
				jpql.append(" and ");
			}
			jpql.append(" departamentoRegional.id = (:idDepRegional) ");
			parametros.put("idDepRegional", idDepartamento);
		}
		
		jpql.append(" and ");
		jpql.append(" departamentoRegionalProdutoServicos.dataExclusao is null ");
		
		if (!count) {
			jpql.append(" order by linha.descricao ");
		}
	}

	private void montarJoinPaginadoDepartamento(StringBuilder jpql, Long idDepartamento, boolean count) {
		if (count) {
			jpql.append("select count(DISTINCT produtoServico.id) from ProdutoServico produtoServico ");
			jpql.append(" inner join produtoServico.linha linha ");
		} else {
			jpql.append("select DISTINCT produtoServico from ProdutoServico produtoServico ");
			jpql.append(" inner join fetch produtoServico.linha linha ");
		}

		if (idDepartamento != null) {
			jpql.append(" inner join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos");
			jpql.append(" inner join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional");
		}
	}

	private long getCountQueryPaginado(ProdutoServicoFilter filter, DadosFilter segurancaFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, filter, true, segurancaFilter);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}
	
	private long getCountQueryPaginadoDepartamento(ProdutoServicoFilter filter, Long idDepartamento, DadosFilter segurancaFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginadoDepartamento(jpql, parametros, filter, idDepartamento, true, segurancaFilter);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}
	
	public List<ProdutoServico> pesquisarSemPaginacao(DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Produtos e Servicos por filtro");

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		boolean temWhere = segurancaFilter != null && (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) && !segurancaFilter.isAdministrador();
		
		jpql.append("select DISTINCT produtoServico from ProdutoServico produtoServico ");
		montarJoinPesquisarSemPaginacao(segurancaFilter, jpql);
		
		if (temWhere) {
			jpql.append("  where ");
		}
		
		filtroDepEmpIds(segurancaFilter, jpql, parametros);

		jpql.append(" order by produtoServico.nome ");
		
		TypedQuery<ProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return query.getResultList();
	}

	private void filtroDepEmpIds(DadosFilter segurancaFilter, StringBuilder jpql, Map<String, Object> parametros) {
		if (segurancaFilter != null && segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
			jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}
		
		if (segurancaFilter != null &&  (segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) && !segurancaFilter.isAdministrador()) {
			if (segurancaFilter.temIdsDepRegional()) {
				jpql.append(" and ");
			}
			jpql.append(" empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}
	}

	private void montarJoinPesquisarSemPaginacao(DadosFilter segurancaFilter, StringBuilder jpql) {
		jpql.append(" inner join fetch produtoServico.linha linha ");
		if (segurancaFilter != null && (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) && !segurancaFilter.isAdministrador()) {
			jpql.append(" inner join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos");
			jpql.append(" inner join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional");
		}
		
		if (segurancaFilter != null && (segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) && !segurancaFilter.isAdministrador()) {
			jpql.append(" inner join departamentoRegional.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador");
			jpql.append(" inner join unidadeAtendimentoTrabalhador.empresaUats empresaUats");
			jpql.append(" inner join empresaUats.empresa empresa");
		}
	}
	
	public List<ProdutoServico> buscarProdutoServicoPorIdUat(String ids, DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		
		List<String> listaIdsString = Arrays.asList(ids.split(","));
		List<Long> listaIdsLong = new ArrayList<Long>();
		listaIdsString.stream().forEach(n -> listaIdsLong.add(Long.parseLong(n)));

		jpql.append("select DISTINCT produtoServico from ProdutoServico produtoServico ");
		jpql.append(" inner join fetch produtoServico.linha linha ");

		jpql.append(
				" inner join produtoServico.unidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico");
		jpql.append(" inner join uatProdutoServico.uat uat");
		
		if (segurancaFilter != null && !segurancaFilter.isAdministrador() && (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa())) {
			jpql.append(" inner join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos");
			jpql.append(" inner join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional");
		}
		
		if (segurancaFilter != null && !segurancaFilter.isAdministrador() && segurancaFilter.temIdsEmpresa()) {
			jpql.append(" inner join departamentoRegional.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador");
			jpql.append(" inner join unidadeAtendimentoTrabalhador.empresaUats empresaUats");
			jpql.append(" inner join empresaUats.empresa empresa");
		}

		jpql.append(" where uat.id IN :id ");
		parametros.put("id", listaIdsLong);
		
		jpql.append(" and ");
		jpql.append(" uatProdutoServico.dataExclusao is null ");
		
		if (segurancaFilter != null && !segurancaFilter.isAdministrador() && segurancaFilter.temIdsDepRegional()) {
			jpql.append(" and ");
			jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}
		
		if (segurancaFilter != null && !segurancaFilter.isAdministrador() &&  segurancaFilter.temIdsEmpresa()) {
			jpql.append(" and ");
			jpql.append(" empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}
		
		jpql.append(" order by linha.descricao ");

		TypedQuery<ProdutoServico> query = criarConsultaPorTipo(jpql.toString());

		DAOUtil.setParameterMap(query, parametros);

		return query.getResultList();
	}

}
