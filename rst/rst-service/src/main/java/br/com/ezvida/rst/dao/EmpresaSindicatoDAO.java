package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.EmpresaSindicato;
import fw.core.jpa.DAOUtil;

public class EmpresaSindicatoDAO extends BaseRstDAO<EmpresaSindicato, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaSindicatoDAO.class);

	@Inject
	public EmpresaSindicatoDAO(EntityManager em) {
		super(em, EmpresaSindicato.class);
	}

	public ListaPaginada<EmpresaSindicato> pesquisarPorEmpresa(EmpresaFilter empresaFilter) {
		LOGGER.debug("Buscando sindicatos associados a empresa... ");

		ListaPaginada<EmpresaSindicato> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		getQueryPaginado(jpql, parametros, empresaFilter, false);

		TypedQuery<EmpresaSindicato> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(empresaFilter));

		if (empresaFilter != null) {
			query.setFirstResult((empresaFilter.getPagina() - 1) * empresaFilter.getQuantidadeRegistro());
			query.setMaxResults(empresaFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}
	
	public List<EmpresaSindicato> pesquisarAssociadosPorCNPJ(Long idEmpresa, String cnpj) {
		LOGGER.debug("Pesquisando Empresa Sindicato por CNPJ");
		StringBuilder jpql = new StringBuilder();

		jpql.append("select empresaSindicato from EmpresaSindicato empresaSindicato ");
		jpql.append("left join fetch empresaSindicato.sindicato sindicato ");
		jpql.append("left join fetch empresaSindicato.empresa empresa ");
		jpql.append(" where sindicato.cnpj = :cnpj and empresa.id = :idEmpresa ");

		TypedQuery<EmpresaSindicato> query = criarConsultaPorTipo(jpql.toString());

		query.setParameter("cnpj", cnpj);
		query.setParameter("idEmpresa", idEmpresa);

		return query.getResultList();
	}

	public Long getCountQueryPaginado(EmpresaFilter empresaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, empresaFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, EmpresaFilter empresaFilter, boolean count) {

		if (count) {
			jpql.append(" select count(empresaSindicato.id) ");
			jpql.append(" from EmpresaSindicato empresaSindicato ");
			jpql.append(" inner join empresaSindicato.empresa empresa ");
			jpql.append(" inner join empresaSindicato.sindicato sindicato ");
		} else {
			jpql.append(" select empresaSindicato ");
			jpql.append(" from EmpresaSindicato empresaSindicato ");
			jpql.append(" inner join fetch empresaSindicato.empresa empresa ");
			jpql.append(" inner join fetch empresaSindicato.sindicato sindicato ");
		}


		if (empresaFilter != null) {

			jpql.append(" where  empresaSindicato.dataExclusao is null ");

			if (empresaFilter.getId() != null) {
				jpql.append(" and empresa.id = :idEmpresa ");
				parametros.put("idEmpresa", empresaFilter.getId());
			}

			if (!count) {
				jpql.append(" order by empresaSindicato.dataAssociacao desc");
			}
		}

	}

	public EmpresaSindicato buscarPorSindicatoEEmpresa(Long idSinticato, Long idEmpresa) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select empresaSindicato ");
		jpql.append(" from EmpresaSindicato empresaSindicato ");
		jpql.append(" inner join fetch empresaSindicato.empresa empresa ");
		jpql.append(" inner join fetch empresaSindicato.sindicato sindicato ");
		jpql.append(" where  empresaSindicato.dataExclusao is null and empresaSindicato.dataDesligamento is null ");
		jpql.append(" and empresa.id = :idEmpresa and sindicato.id = :idSindicato ");
		TypedQuery<EmpresaSindicato> query = criarConsultaPorTipo(jpql.toString());

		query.setParameter("idEmpresa", idEmpresa);
		query.setParameter("idSindicato", idSinticato);

		return DAOUtil.getSingleResult(query);
	}

	public EmpresaSindicato buscarSindicatoEEmpresaNaoDesligado(Long idEmpresa) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select empresaSindicato ");
		jpql.append(" from EmpresaSindicato empresaSindicato ");
		jpql.append(" inner join fetch empresaSindicato.empresa empresa ");
		jpql.append(" inner join fetch empresaSindicato.sindicato sindicato ");
		jpql.append(" where  empresaSindicato.dataExclusao is null ");
		jpql.append(" and empresa.id = :idEmpresa and empresaSindicato.dataDesligamento is null ");
		
		TypedQuery<EmpresaSindicato> query = criarConsultaPorTipo(jpql.toString());

		query.setParameter("idEmpresa", idEmpresa);

		return DAOUtil.getSingleResult(query);
	}

	@SuppressWarnings("unchecked")
	public List<EmpresaSindicato> buscarEmpresaSindicatoNoPeriodo(Long idEmpresa, EmpresaSindicato empresaSindicato) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> parameters = Maps.newHashMap();
		sql.append(" select * from ");
		sql.append("emp_sindicato emp_sindicato ");
		sql.append("inner join EMPRESA empresa on emp_sindicato.ID_EMPRESA_FK=empresa.ID_EMPRESA ");
		sql.append("inner join SINDICATO sindicato on emp_sindicato.ID_SINDICATO_FK=sindicato.ID_SINDICATO ");
		sql.append("where empresa.ID_EMPRESA=:idEmpresa  and  emp_sindicato.dt_exclusao is null and ");
		if (empresaSindicato.getId() != null) {
			sql.append("emp_sindicato.id_empresa_sindicato <> :idSindicato and ");
			parameters.put("idSindicato", empresaSindicato.getId());
		}
		sql.append(" (date_trunc('day', to_timestamp(:dataAssociacaoAtual, 'YYYY-MM-dd')) ");
		sql.append(" between date_trunc('day', emp_sindicato.dt_associacao) and ");
		sql.append(" date_trunc('day', emp_sindicato.dt_desligamento) or ");

		sql.append(" date_trunc('day', to_timestamp(:dataDesligamentoAtual, 'YYYY-MM-dd')) ");
		sql.append(" between date_trunc('day', emp_sindicato.dt_associacao) and ");
		sql.append(" date_trunc('day', emp_sindicato.dt_desligamento) or ");


		sql.append(" (date_trunc('day', emp_sindicato.dt_associacao) ");
		sql.append(" between date_trunc('day', to_timestamp(:dataAssociacaoAtual, 'YYYY-MM-dd')) and ");
		sql.append(" date_trunc('day', to_timestamp(:dataDesligamentoAtual, 'YYYY-MM-dd')) ");
		sql.append(" and date_trunc('day', emp_sindicato.dt_desligamento) ");
		sql.append(" between date_trunc('day', to_timestamp(:dataAssociacaoAtual, 'YYYY-MM-dd')) and ");
		sql.append(" date_trunc('day', to_timestamp(:dataDesligamentoAtual, 'YYYY-MM-dd')))");
		sql.append(") ");


		Query query = criarConsultaNativa(sql.toString(), EmpresaSindicato.class);
		DAOUtil.setParameterMap(query, parameters);
		query.setParameter("idEmpresa", idEmpresa);
		query.setParameter("dataAssociacaoAtual", empresaSindicato.getDataAssociacao());
		query.setParameter("dataDesligamentoAtual", empresaSindicato.getDataDesligamento() == null ? new Date() : empresaSindicato.getDataDesligamento());


		return query.getResultList();
	}
}
