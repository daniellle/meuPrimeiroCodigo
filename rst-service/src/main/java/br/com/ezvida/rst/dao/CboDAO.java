package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.CboFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Cbo;
import fw.core.jpa.DAOUtil;

public class CboDAO extends BaseRstDAO<Cbo, Long> {

	private static final String ORDER_BY = "codigo";

	private static final Logger LOGGER = LoggerFactory.getLogger(CboDAO.class);

	@Inject
	public CboDAO(EntityManager em) {
		super(em, Cbo.class, ORDER_BY);
	}

	public ListaPaginada<Cbo> pesquisaPaginada(CboFilter cboFilter) {
		LOGGER.debug("Pesquisando Cbo paginado");

		ListaPaginada<Cbo> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, cboFilter, false);
		TypedQuery<Cbo> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(cboFilter));

		if (cboFilter != null) {
			query.setFirstResult((cboFilter.getPagina() - 1) * cboFilter.getQuantidadeRegistro());
			query.setMaxResults(cboFilter.getQuantidadeRegistro());
		}
		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, CboFilter cboFilter,
			boolean count) {

		if (count) {
			jpql.append("select count(cbo.id) from Cbo cbo ");
		} else {
			jpql.append("select cbo from Cbo cbo ");
		}

		if (cboFilter != null) {
			boolean codigo = StringUtils.isNotBlank(cboFilter.getCodigo());
			boolean descricao = StringUtils.isNotBlank(cboFilter.getDescricao());
			if (codigo || descricao) {
				jpql.append("  where ");
			}
			if (descricao) {
				jpql.append("  upper(cbo.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%".concat(cboFilter.getDescricao().replace("%", "\\%").toUpperCase()).concat("%"));
			}

			if (codigo) {
				if (descricao) {
					jpql.append(" and ");
				}
				jpql.append("  upper(cbo.codigo) like :codigo escape :sc");
				parametros.put("sc", "\\");
				parametros.put("codigo", cboFilter.getCodigo().replace("%", "\\%").toUpperCase().concat("%"));
			}
		}

		if (!count) {
			jpql.append(" Order By cbo.descricao ");
		}
	}

	private long getCountQueryPaginado(CboFilter cboFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, cboFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

}
