package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ProdutoServicoFilter;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhadorProdutoServico;
import fw.core.jpa.DAOUtil;

public class UnidadeAtendimentoTrabalhadorProdutoServicoDAO
		extends BaseRstDAO<UnidadeAtendimentoTrabalhadorProdutoServico, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeAtendimentoTrabalhadorProdutoServicoDAO.class);

	@Inject
	public UnidadeAtendimentoTrabalhadorProdutoServicoDAO(EntityManager em) {
		super(em, UnidadeAtendimentoTrabalhadorProdutoServico.class);
	}

	public ListaPaginada<UnidadeAtendimentoTrabalhadorProdutoServico> retornarPorUat(
			ProdutoServicoFilter unidAtendTrabalhadorFilter) {
		LOGGER.debug("Pesquisando Unidade Sesi Produtos e Serviços por Unidades Sesi");

		ListaPaginada<UnidadeAtendimentoTrabalhadorProdutoServico> listaPaginada = new ListaPaginada<>(0L,
				new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		queryPorUat(jpql, parametros, unidAtendTrabalhadorFilter.getId(), false);
		TypedQuery<UnidadeAtendimentoTrabalhadorProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(unidAtendTrabalhadorFilter.getId()));

		query.setFirstResult(
				(unidAtendTrabalhadorFilter.getPagina() - 1) * unidAtendTrabalhadorFilter.getQuantidadeRegistro());
		query.setMaxResults(unidAtendTrabalhadorFilter.getQuantidadeRegistro());

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}
	
	public List<UnidadeAtendimentoTrabalhadorProdutoServico> retornarTodosPorUat(
			ProdutoServicoFilter unidAtendTrabalhadorFilter) {
		LOGGER.debug("Pesquisando Todos Unidade Sesi Produtos e Serviços por Unidades Sesi");

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		queryPorUat(jpql, parametros, unidAtendTrabalhadorFilter.getId(), false);
		TypedQuery<UnidadeAtendimentoTrabalhadorProdutoServico> query = criarConsultaPorTipo(jpql.toString());

		DAOUtil.setParameterMap(query, parametros);

		return query.getResultList();
	}

	private void queryPorUat(StringBuilder jpql, Map<String, Object> parametros, Long id, boolean count) {

		if (!count) {
			jpql.append("select uatProdutoServico from UnidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico ");
			jpql.append(" left join fetch uatProdutoServico.uat uat ");
			jpql.append(" left join fetch uatProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join fetch produtoServico.linha linha ");
		} else {
			
			jpql.append("select count(uatProdutoServico.id) from UnidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico ");
			jpql.append(" left join uatProdutoServico.uat uat ");
			jpql.append(" left join uatProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join produtoServico.linha linha ");
			
		}
		
		jpql.append(" where uatProdutoServico.dataExclusao is null ");
		
		
		if(id != null) {
			jpql.append(" and uat.id = :id ");
			parametros.put("id", id);
		}
		
		if(!count) {
			jpql.append(" Order By linha.descricao ");
		}

	}

	public UnidadeAtendimentoTrabalhadorProdutoServico verificandoExistenciaProdutoServico(Long idUat, Long idProdutosServico) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select uatProdutoServico from UnidadeAtendimentoTrabalhadorProdutoServico uatProdutoServico ");
		jpql.append(" left join fetch uatProdutoServico.uat uat ");
		jpql.append(" left join fetch uatProdutoServico.produtoServico produtoServico ");
		jpql.append(" left join fetch  produtoServico.linha linha ");
		
		jpql.append(" where uatProdutoServico.dataExclusao is null ");
		jpql.append(" and produtoServico.id = :idProdutosServico  ");
		jpql.append(" and uat.id = :id  ");
		
		TypedQuery<UnidadeAtendimentoTrabalhadorProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idProdutosServico" ,idProdutosServico);
		query.setParameter("id", idUat);
		
		return DAOUtil.getSingleResult(query);
	}

	private Long getCountQueryPaginado(Long idUat) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		queryPorUat(jpql, parametros, idUat, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

}
