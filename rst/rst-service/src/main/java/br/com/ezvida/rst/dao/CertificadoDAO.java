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

import br.com.ezvida.rst.dao.filter.CertificadoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.Certificado;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class CertificadoDAO extends BaseDAO<Certificado, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CertificadoDAO.class);

	private static final String ORDER_BY = "descricao";

	@Inject
	public CertificadoDAO(EntityManager em) {
		super(em, Certificado.class, ORDER_BY);
	}

	@Override
	public Certificado pesquisarPorId(Long id) {
		LOGGER.debug("Buscar Certificado por Id");
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		jpql.append("select certificado from Certificado certificado ");
		jpql.append(" inner join certificado.trabalhador trabalhador");
		jpql.append(" left join fetch certificado.tipoCurso tipoCurso");
		jpql.append(" where certificado.id = :id");
		parametros.put("id", id);

		TypedQuery<Certificado> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<Certificado> pesquisarPaginado(CertificadoFilter filter) {
		LOGGER.debug("Pesquisando paginado Produtos e Servicos por filtro");

		ListaPaginada<Certificado> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, filter, false);
		TypedQuery<Certificado> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(filter));

		if (filter != null) {
			query.setFirstResult((filter.getPagina() - 1) * filter.getQuantidadeRegistro());
			query.setMaxResults(filter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, CertificadoFilter filter,
			boolean count) {

		boolean descricao = false;
		boolean tipoCurso = false;

		if (count) {
			jpql.append("select count(certificado.id) from Certificado certificado ");
			jpql.append(" left join certificado.tipoCurso tipoCurso");
		} else {
			jpql.append("select certificado from Certificado certificado ");
			jpql.append(" left join fetch certificado.tipoCurso tipoCurso");
		}

		if (filter != null) {
			jpql.append("  where certificado.trabalhador.id = :idTrabalhador");
			parametros.put("idTrabalhador", filter.getIdTrabalhador());

			descricao = StringUtils.isNotBlank(filter.getDescricao());
			tipoCurso = filter.getIdTipoCurso() != null;

			if (descricao) {
				jpql.append(" and UPPER(certificado.descricao) like :descricao escape :sc");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + filter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
			}
			if (tipoCurso) {

				jpql.append(" and tipoCurso.id = :idTipoCurso");
				parametros.put("idTipoCurso", filter.getIdTipoCurso());
			}

			jpql.append(" and certificado.dataExclusao is null ");

			jpql.append(" and certificado.inclusaoTrabalhador = :inclusaoTrabalhador");
			parametros.put("inclusaoTrabalhador", filter.getInclusaoTrabalhador());
		}
		
		if (!count) {
			jpql.append(" order by certificado.descricao ");
		}
	}

	private long getCountQueryPaginado(CertificadoFilter filter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, filter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

}
