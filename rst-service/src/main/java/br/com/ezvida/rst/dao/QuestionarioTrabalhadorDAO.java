package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.QuestionarioTrabalhadorFilter;
import br.com.ezvida.rst.model.QuestionarioTrabalhador;
import fw.core.jpa.DAOUtil;

public class QuestionarioTrabalhadorDAO extends BaseRstDAO<QuestionarioTrabalhador, Long> {

	private static final String ORDER_BY = "descricao";

	@Inject
	public QuestionarioTrabalhadorDAO(EntityManager em) {
		super(em, QuestionarioTrabalhador.class, ORDER_BY);
	}

	public ListaPaginada<QuestionarioTrabalhador> pesquisarPaginado(
			QuestionarioTrabalhadorFilter questionarioTrabalhadorFilter) {

		ListaPaginada<QuestionarioTrabalhador> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		StringBuilder jpql = new StringBuilder();

		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, questionarioTrabalhadorFilter, true);
		TypedQuery<QuestionarioTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(questionarioTrabalhadorFilter));

		if (questionarioTrabalhadorFilter != null && questionarioTrabalhadorFilter.getPagina() != null
				&& questionarioTrabalhadorFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((questionarioTrabalhadorFilter.getPagina() - 1)
					* questionarioTrabalhadorFilter.getQuantidadeRegistro());
			query.setMaxResults(questionarioTrabalhadorFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;

	}

	public long getCountQueryPaginado(QuestionarioTrabalhadorFilter questionarioTrabalhadorFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, questionarioTrabalhadorFilter, false);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			QuestionarioTrabalhadorFilter questionarioTrabalhadorFilter, boolean count) {

		if (!count) {
			jpql.append(
					"select count(DISTINCT questionarioTrabalhador.id) from QuestionarioTrabalhador questionarioTrabalhador"
							+ " left join questionarioTrabalhador.trabalhador trabalhador "
							+ " left join questionarioTrabalhador.questionario questionario "
							+ " left join questionarioTrabalhador.classificacaoPontuacao classificacaoPontuacao "
							+ " where trabalhador.id = (:id)");
			parametros.put("id", questionarioTrabalhadorFilter.getId());
		} else {
			jpql.append("select DISTINCT questionarioTrabalhador from QuestionarioTrabalhador questionarioTrabalhador "
					+ " left join fetch questionarioTrabalhador.trabalhador trabalhador"
					+ " left join fetch questionarioTrabalhador.questionario questionario "
					+ " left join fetch questionarioTrabalhador.classificacaoPontuacao classificacaoPontuacao where trabalhador.id = (:id)"
					+ " order by questionarioTrabalhador.dataQuestionarioTrabalhador  ");
			parametros.put("id", questionarioTrabalhadorFilter.getId());
		}
	}

	public QuestionarioTrabalhador getUltimoRegisto(Long idTrabalhador) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select questionarioTrabalhador from QuestionarioTrabalhador questionarioTrabalhador ");
		jpql.append(" left join fetch questionarioTrabalhador.trabalhador trabalhador ");
		jpql.append(" left join fetch questionarioTrabalhador.questionario questionario ");
		jpql.append(" left join fetch  questionario.periodicidade");
		jpql.append(" where trabalhador.id = (:idTrabalhador) ");
		jpql.append(" ORDER BY questionarioTrabalhador.id DESC ");
		TypedQuery<QuestionarioTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);
		return DAOUtil.getSingleResult(query.setMaxResults(1));
	}

	public QuestionarioTrabalhador buscarPorIdCompleto(Long idQuestionarioTrabalhador) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select questionarioTrabalhador from QuestionarioTrabalhador questionarioTrabalhador ");
		jpql.append(" left join fetch questionarioTrabalhador.trabalhador trabalhador ");
		jpql.append(" left join fetch questionarioTrabalhador.questionario questionario ");
		jpql.append(" left join fetch questionarioTrabalhador.classificacaoPontuacao classificacaoPontuacao ");
		jpql.append(" where questionarioTrabalhador.id = (:idQuestionarioTrabalhador) ");
		TypedQuery<QuestionarioTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idQuestionarioTrabalhador", idQuestionarioTrabalhador);
		return DAOUtil.getSingleResult(query);
	}
}
