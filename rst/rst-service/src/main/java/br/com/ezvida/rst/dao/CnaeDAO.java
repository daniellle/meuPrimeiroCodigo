package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.CnaeFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Cnae;
import br.com.ezvida.rst.utils.CollectionUtil;
import fw.core.jpa.DAOUtil;

public class CnaeDAO extends BaseRstDAO<Cnae, Long> {
	private static final String ORDER_BY = "descricao";

	private static final Logger LOGGER = LoggerFactory.getLogger(CnaeDAO.class);
	
	private boolean filtroAplicado;

	@Inject
	public CnaeDAO(EntityManager em) {
		super(em, Cnae.class, ORDER_BY);
	}

	public ListaPaginada<Cnae> pesquisaPorPaginado(CnaeFilter cnaeFilter) {
		LOGGER.debug("Pesquisando cnae por filter ... ");

		ListaPaginada<Cnae> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, cnaeFilter, false);
		TypedQuery<Cnae> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(cnaeFilter));

		if (cnaeFilter != null) {
			query.setFirstResult((cnaeFilter.getPagina() - 1) * cnaeFilter.getQuantidadeRegistro());
			query.setMaxResults(cnaeFilter.getQuantidadeRegistro());
		}
		listaPaginada.setList(query.getResultList());

		return listaPaginada;

	}
	
		private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, CnaeFilter cnaeFilter,
			boolean count) {
		
		boolean versao = false;
		boolean codigo = false;
		boolean descricao = false;
		boolean ids = false;

		if (count) {
			jpql.append("select count(cnae.id) from Cnae cnae ");
		} else {
			jpql.append("select cnae from Cnae cnae ");
		}
		
		jpql.append("  where cnae.dataExclusao is null ");
		setFiltroAplicado(true);
		
		if (cnaeFilter != null) {
			versao = StringUtils.isNotBlank(cnaeFilter.getVersao());
			codigo = StringUtils.isNotBlank(cnaeFilter.getCodigo());
			descricao = StringUtils.isNotBlank(cnaeFilter.getDescricao());
			ids = StringUtils.isNotBlank(cnaeFilter.getIdsCnae());
			
			if (versao) {
				adicionarAnd(jpql);
				jpql.append(" cnae.versao = :versao ");
				parametros.put("versao", cnaeFilter.getVersao());
				setFiltroAplicado(true);
			}
			if (descricao) {
				adicionarAnd(jpql);
				jpql.append("  upper(cnae.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", ("%" + cnaeFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%"));
				setFiltroAplicado(true);
			}

			if (codigo) {
				adicionarAnd(jpql);
				jpql.append("  upper(cnae.codigo) like :codigo escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("codigo", cnaeFilter.getCodigo().replace("%", "\\%").toUpperCase().concat("%"));
				setFiltroAplicado(true);
			}

			if (ids) {
				adicionarAnd(jpql);
				jpql.append("  cnae.id NOT IN ( :ids ) ");
				parametros.put("ids", CollectionUtil.getIds(cnaeFilter.getIdsCnae()));
				setFiltroAplicado(true);
			}
		}

		if (!count) {
			jpql.append(" order by cnae.descricao ");
		}

	}

	private Long getCountQueryPaginado(CnaeFilter cnaeFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, cnaeFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarVersoes() {
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("select distinct cnae.versao from Cnae cnae order by cnae.versao ");
		Query query = criarConsulta(sqlBuilder.toString());

		return query.getResultList();
	}
	
	private boolean isFiltroAplicado() {
		return filtroAplicado;
	}

	private void setFiltroAplicado(boolean filtroAplicado) {
		this.filtroAplicado = filtroAplicado;
	}

	private void adicionarAnd(StringBuilder jpql) {
		if (isFiltroAplicado()) {
			jpql.append(" and ");
		}
	}

}
