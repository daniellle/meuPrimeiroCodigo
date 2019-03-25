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
import br.com.ezvida.rst.dao.filter.QuestionarioFilter;
import br.com.ezvida.rst.enums.StatusQuestionario;
import br.com.ezvida.rst.model.Questionario;
import br.com.ezvida.rst.model.TipoQuestionario;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class QuestionarioDAO extends BaseDAO<Questionario, Long> {

	private boolean filtroAplicado;
	@Inject
	public QuestionarioDAO(EntityManager em) {
		super(em, Questionario.class);
	}

	public Questionario pesquisarPorId(Long id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select questionario from Questionario questionario ");
		jpql.append("left join fetch questionario.tipoQuestionario tipoQuestionario ");
		jpql.append("left join fetch questionario.periodicidade periodicidade ");
		jpql.append(" where questionario.id = :id");
		TypedQuery<Questionario> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return DAOUtil.getSingleResult(query);
	}

	public List<Questionario> listarTodos() {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select p from Questionario p ");
		TypedQuery<Questionario> query = criarConsultaPorTipo(jpql.toString());
		return query.getResultList();
	}

	public ListaPaginada<Questionario> pesquisarPaginado(QuestionarioFilter questionarioFilter) {
		ListaPaginada<Questionario> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, questionarioFilter, false);

		TypedQuery<Questionario> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(questionarioFilter));

		if (questionarioFilter != null) {
			query.setFirstResult((questionarioFilter.getPagina() - 1) * questionarioFilter.getQuantidadeRegistro());
			query.setMaxResults(questionarioFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public long getCountQueryPaginado(QuestionarioFilter questionarioFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, questionarioFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			QuestionarioFilter questionarioFilter, boolean count) {
		this.filtroAplicado = false;
		if (count) {
			jpql.append(" select DISTINCT count(p.id) from Questionario p ");
			jpql.append(" inner join p.tipoQuestionario tipoQuestionario ");
		} else {
			jpql.append(" select DISTINCT p from Questionario p ");
			jpql.append(" inner join fetch p.tipoQuestionario tipoQuestionario ");
		}

		if (questionarioFilter != null) {
			jpql.append("where p.dataExclusao is null and");
			if (StringUtils.isNotBlank(questionarioFilter.getNome())) {
				jpql.append(" UPPER(p.nome) like :nome escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("nome", "%" + questionarioFilter.getNome().replace("%", "\\%").toUpperCase() + "%");
				setFiltroAplicado(true);
			}
			
			if (StringUtils.isNotBlank(questionarioFilter.getSituacao())) {
				adicionarAnd(jpql);
				jpql.append(" p.status = :status");
				parametros.put("status", StatusQuestionario.getStatusQuestionario(questionarioFilter.getSituacao()));
				setFiltroAplicado(true);
			}

			if (StringUtils.isNotBlank(questionarioFilter.getTipo())) {
				adicionarAnd(jpql);
				jpql.append(" p.tipoQuestionario.id = :tipo");
				parametros.put("tipo",Long.parseLong(questionarioFilter.getTipo()));
				setFiltroAplicado(true);
			}
			
			if (StringUtils.isNotBlank(questionarioFilter.getVersao())) {
				adicionarAnd(jpql);
				jpql.append(" p.versao = :versao");
				parametros.put("versao", Integer.parseInt(questionarioFilter.getVersao()));
				setFiltroAplicado(true);
			}
		}

		if (!count) {
			jpql.append(" order by p.descricao");
		}
	}

	public Questionario pesquisarPorDescricao(Questionario questionario) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		jpql.append("select questionario from Questionario questionario ");
		jpql.append(" where UPPER(questionario.descricao) like :descricao escape :sc ");
		parametros.put("sc", "\\");
		parametros.put("descricao", "%" + questionario.getDescricao().replace("%", "\\%").toUpperCase() + "%");
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}
	
	@SuppressWarnings("unchecked")
	public List<Questionario> pesquisarPorNome(Questionario questionario) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		jpql.append("select questionario from Questionario questionario ");
		jpql.append(" where UPPER(questionario.nome) like :nome escape :sc ");
		jpql.append(" and questionario.dataExclusao is null ");
		parametros.put("sc", "\\");
		parametros.put("nome", questionario.getNome().replace("%", "\\%").toUpperCase());
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return query.getResultList();
	}
	
	public Questionario pesquisarPorPublicado(TipoQuestionario tipo) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		jpql.append("select questionario from Questionario questionario ");
		jpql.append(" where questionario.status like :status ");
		jpql.append(" and questionario.tipoQuestionario.id = :tipo ");
		parametros.put("status", StatusQuestionario.PUBLICADO);
		parametros.put("tipo", tipo.getId());
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public Long getIdQuestionarioPublicado() {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		jpql.append("select questionario.id from Questionario questionario ");
		jpql.append(" where questionario.dataCriacao in (");
		jpql.append("                     select max(b.dataCriacao) from Questionario b ");
		jpql.append("                     where UPPER(questionario.ds_status) like :ds_status escape :sc )");
		parametros.put("sc", "/");
		parametros.put("descricao", "%PUBLICADO%");
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}
	@SuppressWarnings("unchecked")
	public List<String> buscarVersoes() {
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("select distinct questionario.versao from Questionario questionario where questionario.versao is not null order by questionario.versao ");
		Query query = criarConsulta(sqlBuilder.toString());
		return query.getResultList();
	}
	
	private void adicionarAnd(StringBuilder jpql) {
		if (isFiltroAplicado()) {
			jpql.append(" and ");
		}
	}

	public List<Questionario> buscarQuestionarioPublicado(){
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT q FROM Questionario q ");
		jpql.append(" JOIN FETCH q.periodicidade p ");
		jpql.append(" WHERE q.status = :status");
		TypedQuery<Questionario> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("status", StatusQuestionario.PUBLICADO);
		return query.getResultList();
	}

	public boolean isFiltroAplicado() {
		return filtroAplicado;
	}

	public void setFiltroAplicado(boolean filtroAplicado) {
		this.filtroAplicado = filtroAplicado;
	}
	
}
