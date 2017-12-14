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

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.RespostaFilter;
import br.com.ezvida.rst.model.Resposta;
import fw.core.jpa.DAOUtil;

public class RespostaDAO extends BaseRstDAO<Resposta, Long> {

	private static final String ORDER_BY = "descricao";

	private static final Logger LOGGER = LoggerFactory.getLogger(RespostaDAO.class);

	@Inject
	public RespostaDAO(EntityManager em) {
		super(em, Resposta.class, ORDER_BY);
	}

	public ListaPaginada<Resposta> pesquisarPaginado(RespostaFilter respostaFilter) {

		ListaPaginada<Resposta> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		StringBuilder jpql = new StringBuilder();

		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, respostaFilter, false);
		TypedQuery<Resposta> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(respostaFilter));

		if (respostaFilter != null && respostaFilter.getPagina() != null
				&& respostaFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((respostaFilter.getPagina() - 1) * respostaFilter.getQuantidadeRegistro());
			query.setMaxResults(respostaFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;

	}

	public long getCountQueryPaginado(RespostaFilter respostaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, respostaFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, RespostaFilter respostaFilter,
			boolean count) {

		if (count) {
			jpql.append("select count(DISTINCT resposta.id) from Resposta resposta ");
		} else {
			jpql.append("select DISTINCT resposta from Resposta resposta ");
		}

		jpql.append(" where resposta.dataExclusao is null ");

		if (respostaFilter != null && respostaFilter.getDescricao() != null) {
			jpql.append("and UPPER(resposta.descricao) like :descricao escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("descricao", "%" + respostaFilter.getDescricao().replaceAll("%", "\\%").toUpperCase() + "%");
		}

		if (!count) {
			jpql.append(" order by resposta.descricao ");
		}
	}

	public Resposta pesquisarPorDescricao(String descricao) {
		LOGGER.debug("Pesquisando Resposta por Descricao");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select resposta from Resposta resposta ");
		jpql.append("where upper(resposta.descricao) = :descricao ");
		jpql.append(" and resposta.dataExclusao IS NULL ");
		TypedQuery<Resposta> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("descricao", descricao.toUpperCase());

		return DAOUtil.getSingleResult(query);
	}

	public Boolean respostaEmUso(Long idResposta) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select r from RespostaQuestionario rq ");
		jpql.append(" inner join rq.resposta r");
		jpql.append(" where	r.id = :id");
		TypedQuery<Resposta> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idResposta);
		return !query.getResultList().isEmpty();
	}

}
