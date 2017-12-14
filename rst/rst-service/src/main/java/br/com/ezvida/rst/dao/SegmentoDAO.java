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

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SegmentoFilter;
import br.com.ezvida.rst.model.Segmento;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class SegmentoDAO extends BaseDAO<Segmento, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentoDAO.class);

	@Inject
	public SegmentoDAO(EntityManager em) {
		super(em, Segmento.class);
	}
	
	public List<Segmento> pesquisarTodos() {
		LOGGER.debug("Listando todos os trabalhadores");
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("select segmento from Segmento segmento ");
		jpql.append(" order by segmento.descricao");
		TypedQuery<Segmento> query = criarConsultaPorTipo(jpql.toString());

		return query.getResultList();
	}

	public ListaPaginada<Segmento> pesquisarPaginado(SegmentoFilter segmentoFilter) {
		LOGGER.debug("Pesquisando paginado Segmento por filtro");

		ListaPaginada<Segmento> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, segmentoFilter, false);
		TypedQuery<Segmento> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(segmentoFilter));
		if(segmentoFilter != null) {
		query.setFirstResult((segmentoFilter.getPagina() - 1) * segmentoFilter.getQuantidadeRegistro());
		query.setMaxResults(segmentoFilter.getQuantidadeRegistro());
		}
		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}
	
	
	private long getCountQueryPaginado(SegmentoFilter segmentoFilter) {
		
		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, segmentoFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
		
	}
	
	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, SegmentoFilter segmentoFilter, boolean count) {
		if(count) {
			jpql.append("select count(segmento)  from Segmento segmento ");
		} else {
			jpql.append("select segmento from Segmento segmento ");
		}
		boolean codigo = StringUtils.isNotEmpty(segmentoFilter.getCodigo());
		boolean descricao = StringUtils.isNotEmpty(segmentoFilter.getDescricao());
		
		if (codigo || descricao) {
			jpql.append(" where ");
			if (codigo) {
				jpql.append(" segmento.codigo like :codigo escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("codigo", "%" + segmentoFilter.getCodigo().replace("%", "\\%"));
			}
			if (descricao) {
				if (codigo) {
					jpql.append(" and ");
				}
				jpql.append(" UPPER(segmento.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + segmentoFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
			}
			
		}
		
		if (!count) {
			jpql.append(" order by segmento.descricao ");
		}
		
	}

}
