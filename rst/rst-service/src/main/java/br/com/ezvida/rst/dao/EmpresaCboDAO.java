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

import br.com.ezvida.rst.dao.filter.CboFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.EmpresaCbo;
import fw.core.jpa.DAOUtil;

public class EmpresaCboDAO extends BaseRstDAO<EmpresaCbo, Long> {

	@Inject
	public EmpresaCboDAO(EntityManager em) {
		super(em, EmpresaCbo.class);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaJornadaDAO.class);

	public ListaPaginada<EmpresaCbo> retornarPorEmpresa(CboFilter cboFilter) {
		LOGGER.debug("Pesquisando Empresa CBO por empresa");

		ListaPaginada<EmpresaCbo> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		queryPorEmpresa(jpql, parametros, cboFilter, false);

		TypedQuery<EmpresaCbo> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		listaPaginada.setQuantidade(getCountQueryPaginado(cboFilter));

		query.setFirstResult((cboFilter.getPagina() - 1) * cboFilter.getQuantidadeRegistro());
		query.setMaxResults(cboFilter.getQuantidadeRegistro());

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private Long getCountQueryPaginado(CboFilter cboFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		queryPorEmpresa(jpql, parametros, cboFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

	private void queryPorEmpresa(StringBuilder jpql, Map<String, Object> parametros, CboFilter cboFilter,
			boolean count) {

		if (!count) {
			jpql.append("select empresaCbo from EmpresaCbo empresaCbo ");
			jpql.append(" left join fetch empresaCbo.empresa empresa ");
			jpql.append(" left join fetch empresaCbo.cbo cbo ");
		} else {
			jpql.append("select count(empresaCbo.id) from EmpresaCbo empresaCbo ");
			jpql.append(" left join  empresaCbo.empresa empresa ");
			jpql.append(" left join  empresaCbo.cbo cbo ");
		}

		jpql.append(" where empresaCbo.dataExclusao is null ");

		if (cboFilter != null) {
			boolean codigo = StringUtils.isNotBlank(cboFilter.getCodigo());
			boolean descricao = StringUtils.isNotBlank(cboFilter.getDescricao());
			boolean id = cboFilter.getIdEmpresa() != null;

			if (id) {
				jpql.append(" and empresa.id = :id ");
				parametros.put("id", cboFilter.getIdEmpresa());
			}

			if (descricao) {
				jpql.append(" and upper(cbo.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + cboFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
			}

			if (codigo) {
				jpql.append(" and upper(cbo.codigo) like :codigo escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("codigo", "%" + cboFilter.getCodigo().replace("%", "\\%").toUpperCase().concat("%"));
			}

		}

		if (!count) {
			jpql.append(" Order By cbo.descricao ");
		}

	}

	public EmpresaCbo pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando Empresa cbo por empresa");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select empresaCbo from EmpresaCbo empresaCbo ");
		jpql.append(" left join fetch empresaCbo.empresa empresa ");
		jpql.append(" left join fetch empresaCbo.cbo cbo ");

		if (id != null) {
			jpql.append(" where empresaCbo.id = :id");
		}

		TypedQuery<EmpresaCbo> query = criarConsultaPorTipo(jpql.toString());

		if (id != null) {
			query.setParameter("id", id);
		}

		return query.getSingleResult();
	}

	public List<Long> pesquisarPorIds(Long idEmpresa) {
		LOGGER.debug("Pesquisando Empresa cbo por empresa");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select empresaCbo.id from EmpresaCbo empresaCbo ");
		jpql.append(" left join  empresaCbo.empresa empresa ");
		jpql.append(" left join  empresaCbo.cbo cbo ");

		if (idEmpresa != null) {
			jpql.append(" where empresaCbo.id = :id and empresaCbo.dataExclusao is null ");
		}

		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);

		if (idEmpresa != null) {
			query.setParameter("id", idEmpresa);
		}

		return query.getResultList();
	}

	public Long verificandoExistenciaCbo(Long idEmpresa, Long idCbo) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select cbo.id from EmpresaCbo empresaCbo ");
		jpql.append(" left join  empresaCbo.empresa empresa ");
		jpql.append(" left join  empresaCbo.cbo cbo ");

		jpql.append(" where empresaCbo.dataExclusao is null ");
		jpql.append(" and cbo.id = :idCbo  ");
		jpql.append(" and empresa.id = :id  ");

		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);
		query.setParameter("id", idEmpresa);
		query.setParameter("idCbo", idCbo);

		return DAOUtil.getSingleResult(query);
	}

}
