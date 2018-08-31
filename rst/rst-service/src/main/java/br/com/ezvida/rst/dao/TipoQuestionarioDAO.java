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
import br.com.ezvida.rst.dao.filter.TipoQuestionarioFilter;
import br.com.ezvida.rst.model.TipoQuestionario;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class TipoQuestionarioDAO extends BaseDAO<TipoQuestionario, Long> {

	@Inject
	public TipoQuestionarioDAO(EntityManager em) {
		super(em, TipoQuestionario.class);
	}

	public TipoQuestionario pesquisarPorId(Long id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select tipoQuestionario from TipoQuestionario tipoQuestionario ");
		jpql.append(" where tipoQuestionario.id = :id");
		TypedQuery<TipoQuestionario> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return DAOUtil.getSingleResult(query);
	}

	public List<TipoQuestionario> listarTodos() {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select p from TipoQuestionario p ");
		TypedQuery<TipoQuestionario> query = criarConsultaPorTipo(jpql.toString());
		return query.getResultList();
	}

	public ListaPaginada<TipoQuestionario> pesquisarPaginado(TipoQuestionarioFilter tipoQuestionarioFilter) {
		ListaPaginada<TipoQuestionario> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, tipoQuestionarioFilter, false);

		TypedQuery<TipoQuestionario> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(tipoQuestionarioFilter));

		if (tipoQuestionarioFilter != null) {
			query.setFirstResult((tipoQuestionarioFilter.getPagina() - 1) * tipoQuestionarioFilter.getQuantidadeRegistro());
			query.setMaxResults(tipoQuestionarioFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public long getCountQueryPaginado(TipoQuestionarioFilter tipoQuestionarioFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, tipoQuestionarioFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, TipoQuestionarioFilter tipoQuestionarioFilter,
			boolean count) {
		if (count) {
			jpql.append(" select DISTINCT count(p.id)from TipoQuestionario p ");
		} else {
			jpql.append(" select DISTINCT p from TipoQuestionario p ");
		}

		if (tipoQuestionarioFilter != null && StringUtils.isNotBlank(tipoQuestionarioFilter.getDescricao())) {
				jpql.append(" where UPPER(p.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + tipoQuestionarioFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");			
		}

		if (!count) {
			jpql.append(" order by p.descricao");
		}
	}

	public TipoQuestionario pesquisarPorDescricao(TipoQuestionario tipoQuestionario) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		jpql.append("select tipoQuestionario from TipoQuestionario tipoQuestionario ");
		jpql.append(" where UPPER(tipoQuestionario.descricao) like :descricao escape :sc ");
		parametros.put("sc", "\\");
		parametros.put("descricao", "%" + tipoQuestionario.getDescricao().replace("%", "\\%").toUpperCase() + "%");
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}
}
