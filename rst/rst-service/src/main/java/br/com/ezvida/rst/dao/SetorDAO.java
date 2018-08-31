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

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SetorFilter;
import br.com.ezvida.rst.model.Setor;
import fw.core.jpa.DAOUtil;

public class SetorDAO extends BaseRstDAO<Setor, Long> {

	private static final String ORDER_BY = "sigla";

	private static final Logger LOGGER = LoggerFactory.getLogger(SetorDAO.class);

	@Inject
	public SetorDAO(EntityManager em) {
		super(em, Setor.class, ORDER_BY);
	}

	public ListaPaginada<Setor> pesquisaPorPaginado(SetorFilter setorFilter) {
		LOGGER.debug("Pesquisando setores por filter ... ");

		ListaPaginada<Setor> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, setorFilter, false);
		TypedQuery<Setor> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(setorFilter));

		if (setorFilter != null) {
			query.setFirstResult((setorFilter.getPagina() - 1) * setorFilter.getQuantidadeRegistro());
			query.setMaxResults(setorFilter.getQuantidadeRegistro());
		}
		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, SetorFilter setorFilter,
			boolean count) {

		if (count) {
			jpql.append("select count(setor.id) from Setor setor ");
		} else {
			jpql.append("select setor from Setor setor ");
		}

		if (setorFilter != null) {
			boolean sigla = StringUtils.isNotBlank(setorFilter.getSigla());
			boolean descricao = StringUtils.isNotBlank(setorFilter.getDescricao());
			if (sigla || descricao) {
				jpql.append("  where ");
			}
			if (descricao) {
				jpql.append("  UPPER(setor.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + setorFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
			}

			if (sigla) {
				if (descricao) {
					jpql.append(" and ");
				}
				jpql.append(" UPPER(setor.sigla) like :sigla escape :sc ");
				parametros.put("sc", "/");
				parametros.put("sigla", "%" + setorFilter.getSigla().replace("%", "/%").toUpperCase() + "%");
			}
		}

		if (!count) {
			jpql.append(" order by setor.descricao ");
		}

	}

	private Long getCountQueryPaginado(SetorFilter setorFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, setorFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

}
