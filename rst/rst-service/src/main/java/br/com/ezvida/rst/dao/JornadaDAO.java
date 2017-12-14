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

import br.com.ezvida.rst.dao.filter.JornadaFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Jornada;
import fw.core.jpa.DAOUtil;

public class JornadaDAO extends BaseRstDAO<Jornada, Long> {

	private static final String ORDER_BY = "descricao";

	private static final Logger LOGGER = LoggerFactory.getLogger(JornadaDAO.class);

	@Inject
	public JornadaDAO(EntityManager em) {
		super(em, Jornada.class, ORDER_BY);
	}

	public ListaPaginada<Jornada> pesquisaPorDescricaoHoras(JornadaFilter jornadaFilter) {
		LOGGER.debug("Pesquisando modal por Turno e horas Semanais");

		ListaPaginada<Jornada> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, jornadaFilter, false);
		TypedQuery<Jornada> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(jornadaFilter));

		if (jornadaFilter != null) {
			query.setFirstResult((jornadaFilter.getPagina() - 1) * jornadaFilter.getQuantidadeRegistro());
			query.setMaxResults(jornadaFilter.getQuantidadeRegistro());
		}
		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, JornadaFilter jornadaFilter,
			boolean count) {

		if (count) {
			jpql.append("select count(jornada.id) from Jornada jornada ");
		} else {
			jpql.append("select jornada from Jornada jornada ");
		}

		if (jornadaFilter != null) {
			boolean turno = StringUtils.isNotBlank(jornadaFilter.getTurno());
			boolean horas = StringUtils.isNotBlank(jornadaFilter.getQtdHoras());
			if (turno || horas) {
				jpql.append("  where ");
			}
			if (turno) {
				jpql.append("  upper(jornada.descricao) like :turno escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("turno", "%" + jornadaFilter.getTurno().replace("%", "\\%").toUpperCase() + "%");
			}

			if (horas) {
				if (turno) {
					jpql.append(" and ");
				}
				jpql.append("  jornada.qtdHoras = :horas");
				parametros.put("horas", Integer.parseInt(jornadaFilter.getQtdHoras()));
			}
		}

		if (!count) {
			jpql.append(" Order By jornada.descricao ");
		}
	}

	private long getCountQueryPaginado(JornadaFilter jornadaFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, jornadaFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

}
