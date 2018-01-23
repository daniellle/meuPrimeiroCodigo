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

import br.com.ezvida.rst.dao.filter.GrupoPerguntaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.GrupoPergunta;
import fw.core.jpa.DAOUtil;

public class GrupoPerguntaDAO extends BaseRstDAO<GrupoPergunta, Long> {

	private static final String ORDER_BY = "descricao";

	private static final Logger LOGGER = LoggerFactory.getLogger(GrupoPerguntaDAO.class);

	@Inject
	public GrupoPerguntaDAO(EntityManager em) {
		super(em, GrupoPergunta.class, ORDER_BY);
	}

	public ListaPaginada<GrupoPergunta> pesquisarPaginado(GrupoPerguntaFilter grupoPerguntaFilter) {

		ListaPaginada<GrupoPergunta> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		StringBuilder jpql = new StringBuilder();

		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, grupoPerguntaFilter, false);
		TypedQuery<GrupoPergunta> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(grupoPerguntaFilter));

		if (grupoPerguntaFilter != null && grupoPerguntaFilter.getPagina() != null
				&& grupoPerguntaFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((grupoPerguntaFilter.getPagina() - 1) * grupoPerguntaFilter.getQuantidadeRegistro());
			query.setMaxResults(grupoPerguntaFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;

	}

	public long getCountQueryPaginado(GrupoPerguntaFilter trabalhadorFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, trabalhadorFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			GrupoPerguntaFilter grupoPerguntaFilter, boolean count) {

		if (count) {
			jpql.append("select count(DISTINCT grupoPergunta.id) from GrupoPergunta grupoPergunta ");
		} else {
			jpql.append("select DISTINCT grupoPergunta from GrupoPergunta grupoPergunta ");
		}

		jpql.append("where grupoPergunta.dataExclusao is null ");		
		
		if (grupoPerguntaFilter != null && grupoPerguntaFilter.getDescricao() != null) {
			jpql.append("and UPPER(grupoPergunta.descricao) like :descricao escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("descricao",
					"%" + grupoPerguntaFilter.getDescricao().replaceAll("%", "\\%").toUpperCase() + "%");
		}
		
		if(!count) {
			jpql.append(" order by grupoPergunta.descricao ");
		}
	}

	public GrupoPergunta pesquisarPorDescricao(String descricao) {
		LOGGER.debug("Pesquisando GrupoPergunta por Descricao");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select grupoPergunta from GrupoPergunta grupoPergunta ");
		jpql.append("where upper(grupoPergunta.descricao) = :descricao and ");
		jpql.append("grupoPergunta.dataExclusao is null ");
		TypedQuery<GrupoPergunta> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("descricao", descricao.toUpperCase());

		return DAOUtil.getSingleResult(query);
	}
	
	public Boolean validarUso(Long idGrupoPergunta){
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select g from PerguntaQuestionario pq ");
		jpql.append(" inner join pq.grupoPergunta g");		
		jpql.append(" where	g.id = :id");		
		TypedQuery<GrupoPergunta> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idGrupoPergunta);
		return !query.getResultList().isEmpty();
	}

}
