package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.LinhaFilter;
import br.com.ezvida.rst.model.Linha;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class LinhaDAO extends BaseDAO<Linha, Long> {

	private static final String ORDER_BY = "descricao";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LinhaDAO.class);
	
	@Inject
	public LinhaDAO(EntityManager em) {
		super(em, Linha.class, ORDER_BY);
	}
	
	public Linha pesquisarPorDescricao(String descricao) {
		LOGGER.debug("Pesquisando Linha por Descricao");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select linha from Linha linha where upper(linha.descricao) = :descricao");
		jpql.append(" order by linha.descricao");
		TypedQuery<Linha> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("descricao", descricao.toUpperCase());

		return DAOUtil.getSingleResult(query);
	}

	public List<Linha> pesquisarTodos(DadosFilter segurancaFilter, LinhaFilter linhaFilter, boolean habilitaFiltro) {
		LOGGER.debug("Pesquisando Linhas");

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		
		jpql.append("select DISTINCT linha from Linha linha ");
		
		if (segurancaFilter != null && habilitaFiltro ) {
			
			if (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) {
				jpql.append(" inner join fetch linha.produtosServicos produtoServico ");
				jpql.append(" inner join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos");
				jpql.append(" inner join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional");
			}
			
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) {
				jpql.append(" inner join departamentoRegional.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador");
				jpql.append(" inner join unidadeAtendimentoTrabalhador.empresaUats empresaUats");
				jpql.append(" inner join empresaUats.empresa empresa");
			}
			
			if (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) {
				jpql.append("  where ");
			}
			
			if (segurancaFilter.temIdsDepRegional()) {
				jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
				parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
			}
			
			if (segurancaFilter.temIdsDepRegional() && (segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador())) {
				jpql.append(" and ");
			}
			
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.isTrabalhador()) {
				jpql.append(" empresa.id IN (:idsEmpresa) ");
				parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
			}
		}
		jpql.append(" order by linha.descricao ");
		
		TypedQuery<Linha> query = criarConsultaPorTipo(jpql.toString());
		
		DAOUtil.setParameterMap(query, parametros);
		
		return query.getResultList();
	}
	
	public List<Linha> buscarLinhasPorIdDepartamento(Long id) {
		LOGGER.debug("Pesquisando Linhas");

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		jpql.append("select DISTINCT linha from Linha linha ");

		jpql.append(" inner join fetch linha.produtosServicos produtoServico ");
		jpql.append(
				" inner join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos");
		jpql.append(" inner join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional");

		jpql.append(" where departamentoRegional.id = :id ");
		parametros.put("id", id);
		
		jpql.append(" and ");
		jpql.append(" departamentoRegionalProdutoServicos.dataExclusao is null ");
		
		jpql.append(" order by linha.descricao ");

		TypedQuery<Linha> query = criarConsultaPorTipo(jpql.toString());

		DAOUtil.setParameterMap(query, parametros);

		return query.getResultList();
	}
	
	public List<Linha> buscarLinhasPorIdUat(String ids) {
		LOGGER.debug("Pesquisando Linhas");

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		
		List<String> listaIdsString = Arrays.asList(ids.split(","));
		List<Long> listaIdsLong = new ArrayList<Long>();
		listaIdsString.stream().forEach(n -> listaIdsLong.add(Long.parseLong(n)));

		jpql.append("select DISTINCT linha from Linha linha ");

		jpql.append(" inner join fetch linha.produtosServicos produtoServico ");
		jpql.append(
				" inner join produtoServico.unidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico");
		jpql.append(" inner join uatProdutoServico.uat uat");

		jpql.append(" where uat.id IN :id ");
		parametros.put("id", listaIdsLong);
		
		jpql.append(" and ");
		jpql.append(" uatProdutoServico.dataExclusao is null ");
		
		jpql.append(" order by linha.descricao ");

		TypedQuery<Linha> query = criarConsultaPorTipo(jpql.toString());

		DAOUtil.setParameterMap(query, parametros);

		return query.getResultList();
	}
}
