package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.IndicadorQuestionarioFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.IndicadorQuestionario;
import fw.core.jpa.DAOUtil;

public class IndicadorQuestionarioDAO extends BaseRstDAO<IndicadorQuestionario, Long> {

	private static final String ORDER_BY = "descricao";

	private static final Logger LOGGER = LoggerFactory.getLogger(IndicadorQuestionarioDAO.class);

	@Inject
	public IndicadorQuestionarioDAO(EntityManager em) {
		super(em, IndicadorQuestionario.class,ORDER_BY);
	}

	public ListaPaginada<IndicadorQuestionario> pesquisarPaginado(
			IndicadorQuestionarioFilter indicadorQuestionarioFilter) {

		ListaPaginada<IndicadorQuestionario> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		StringBuilder jpql = new StringBuilder();

		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, indicadorQuestionarioFilter, false);
		TypedQuery<IndicadorQuestionario> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(indicadorQuestionarioFilter));

		if (indicadorQuestionarioFilter != null && indicadorQuestionarioFilter.getPagina() != null
				&& indicadorQuestionarioFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((indicadorQuestionarioFilter.getPagina() - 1)
					* indicadorQuestionarioFilter.getQuantidadeRegistro());
			query.setMaxResults(indicadorQuestionarioFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;

	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			IndicadorQuestionarioFilter indicadorQuestionarioFilter, boolean count) {

		if (count) {
			jpql.append(
					"select count(DISTINCT indicadorQuestionario.id) from IndicadorQuestionario indicadorQuestionario ");
		} else {
			jpql.append("select DISTINCT indicadorQuestionario from IndicadorQuestionario indicadorQuestionario ");
		}

		jpql.append("where indicadorQuestionario.dataExclusao is null ");
		
		if (indicadorQuestionarioFilter != null && indicadorQuestionarioFilter.getDescricao() != null) {
			jpql.append("and UPPER(indicadorQuestionario.descricao) like :descricao escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("descricao",
					"%" + indicadorQuestionarioFilter.getDescricao().replaceAll("%", "\\%").toUpperCase() + "%");
		}

		if (!count) {
			jpql.append(" order by indicadorQuestionario.descricao ");
		}
	}

	public long getCountQueryPaginado(IndicadorQuestionarioFilter indicadorQuestionarioFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, indicadorQuestionarioFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public IndicadorQuestionario pesquisarPorDescricao(String descricao) {
		LOGGER.debug("Pesquisando IndicadorQuestionario por Descricao");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select indicadorQuestionario from IndicadorQuestionario indicadorQuestionario ");
		jpql.append("where upper(indicadorQuestionario.descricao) = :descricao ");
		jpql.append("and indicadorQuestionario.dataExclusao is null ");
		TypedQuery<IndicadorQuestionario> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("descricao", descricao.toUpperCase());

		return DAOUtil.getSingleResult(query);
	}
	
	public Boolean emUso(Long idIndicador){
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select i from PerguntaQuestionario pq ");
		jpql.append(" inner join pq.indicadorQuestionario i");		
		jpql.append(" where	i.id = :id");		
		TypedQuery<IndicadorQuestionario> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idIndicador);		
		return !query.getResultList().isEmpty();
	}

}
