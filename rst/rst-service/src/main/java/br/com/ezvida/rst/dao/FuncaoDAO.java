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

import br.com.ezvida.rst.dao.filter.FuncaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Funcao;
import fw.core.jpa.DAOUtil;

public class FuncaoDAO extends BaseRstDAO<Funcao, Long> {

	private static final String ORDER_BY = "codigo";

	private static final Logger LOGGER = LoggerFactory.getLogger(FuncaoDAO.class);

	@Inject
	public FuncaoDAO(EntityManager em) {
		super(em, Funcao.class, ORDER_BY);
	}

	public ListaPaginada<Funcao> pesquisaPorPaginado(FuncaoFilter funcaoFilter) {
		LOGGER.debug("Pesquisando funcoes por filter ... ");

		ListaPaginada<Funcao> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, funcaoFilter, false);
		TypedQuery<Funcao> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(funcaoFilter));

		if (funcaoFilter != null) {
			query.setFirstResult((funcaoFilter.getPagina() - 1) * funcaoFilter.getQuantidadeRegistro());
			query.setMaxResults(funcaoFilter.getQuantidadeRegistro());
		}
		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, FuncaoFilter funcaoFilter,
			boolean count) {

		if (count) {
			jpql.append("select count(funcao.id) from Funcao funcao ");
		} else {
			jpql.append("select funcao from Funcao funcao ");
		}

		if (funcaoFilter != null) {
			boolean codigo = StringUtils.isNotBlank(funcaoFilter.getCodigo());
			boolean descricao = StringUtils.isNotBlank(funcaoFilter.getDescricao());
			if (codigo || descricao) {
				jpql.append("  where ");
			}
			if (descricao) {
				jpql.append("  upper(funcao.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + funcaoFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
			}

			if (codigo) {
				if (descricao) {
					jpql.append(" and ");
				}
				jpql.append("  upper(funcao.codigo) like :codigo escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("codigo", funcaoFilter.getCodigo().replace("%", "\\%").toUpperCase().concat("%"));
			}
		}

		if (!count) {
			jpql.append(" order by funcao.descricao ");
		}

	}

	private Long getCountQueryPaginado(FuncaoFilter funcaoFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, funcaoFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

}
