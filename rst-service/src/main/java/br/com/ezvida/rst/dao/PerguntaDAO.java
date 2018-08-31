package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.PerguntaFilter;
import br.com.ezvida.rst.model.Pergunta;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class PerguntaDAO extends BaseDAO<Pergunta, Long> {

	@Inject
	public PerguntaDAO(EntityManager em) {
		super(em, Pergunta.class);
	}

	public Pergunta pesquisarPorId(Long id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select pergunta from Pergunta pergunta ");
		jpql.append(" where pergunta.id = :id");
		TypedQuery<Pergunta> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return DAOUtil.getSingleResult(query);
	}

	public List<Pergunta> listarTodos() {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select p from Pergunta p ");
		TypedQuery<Pergunta> query = criarConsultaPorTipo(jpql.toString());
		return query.getResultList();
	}

	public ListaPaginada<Pergunta> pesquisarPaginado(PerguntaFilter perguntaFilter) {
		ListaPaginada<Pergunta> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, perguntaFilter, false);

		TypedQuery<Pergunta> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(perguntaFilter));

		if (perguntaFilter != null) {
			query.setFirstResult((perguntaFilter.getPagina() - 1) * perguntaFilter.getQuantidadeRegistro());
			query.setMaxResults(perguntaFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public long getCountQueryPaginado(PerguntaFilter perguntaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, perguntaFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, PerguntaFilter perguntaFilter,
			boolean count) {
		if (count) {
			jpql.append(" select DISTINCT count(p.id)from Pergunta p ");
		} else {
			jpql.append(" select DISTINCT p from Pergunta p ");
		}

		jpql.append(" where p.dataExclusao is null ");

		if (perguntaFilter != null && StringUtils.isNotBlank(perguntaFilter.getDescricao())) {
			jpql.append(" and UPPER(p.descricao) like :descricao escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("descricao", "%" + perguntaFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
		}

		if (!count) {
			jpql.append(" order by p.descricao");
		}
	}

	public Pergunta pesquisarPorDescricao(Pergunta pergunta) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		jpql.append("select pergunta from Pergunta pergunta ");
		jpql.append(" where UPPER(pergunta.descricao) like :descricao escape :sc ");
		jpql.append(" and pergunta.dataExclusao IS NULL ");
		parametros.put("sc", "\\");
		parametros.put("descricao", "%" + pergunta.getDescricao().replaceAll("%", "\\%").toUpperCase() + "%");
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public List<Pergunta> getPerguntasPorIdQuestionario(Long idQUestionario) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select p from Pergunta p ");
		jpql.append(" join fetch PerguntaQuestionario perguntaQuestionario ");
		jpql.append(" join fetch Questionario questionario ");
		jpql.append(" join fetch Resposta resposta ");

		jpql.append(" where	questionario.id = :id");
		TypedQuery<Pergunta> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idQUestionario);
		return query.getResultList();
	}

	public Boolean perguntaEmUso(Long idPergunta) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select p from PerguntaQuestionario pq ");
		jpql.append(" inner join pq.pergunta p");
		jpql.append(" where	p.id = :id");
		TypedQuery<Pergunta> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idPergunta);
		return !query.getResultList().isEmpty();
	}

}
