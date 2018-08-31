package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.SetorFilter;
import br.com.ezvida.rst.model.EmpresaSetor;
import fw.core.jpa.DAOUtil;

public class EmpresaSetorDAO extends BaseRstDAO<EmpresaSetor, Long> {

	@Inject
	public EmpresaSetorDAO(EntityManager em) {
		super(em, EmpresaSetor.class);
	}

	public ListaPaginada<EmpresaSetor> pesquisarPorPaginado(SetorFilter setorFilter) {
		ListaPaginada<EmpresaSetor> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		getQueryPaginado(jpql, parametros, setorFilter, false);

		TypedQuery<EmpresaSetor> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(setorFilter));

		if (setorFilter != null) {
			query.setFirstResult((setorFilter.getPagina() - 1) * setorFilter.getQuantidadeRegistro());
			query.setMaxResults(setorFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getSelect(StringBuilder jpql, boolean count) {
		if (count) {
			jpql.append(" select count(empresaSetor.id) ");
			jpql.append(" from EmpresaSetor empresaSetor ");
			jpql.append(" inner join empresaSetor.empresa empresa ");
			jpql.append(" inner join empresaSetor.setor setor ");
		} else {
			jpql.append(" select empresaSetor ");
			jpql.append(" from EmpresaSetor empresaSetor ");
			jpql.append(" inner join fetch empresaSetor.empresa empresa ");
			jpql.append(" inner join fetch empresaSetor.setor setor ");
		}
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, SetorFilter setorFilter, boolean count) {
		getSelect(jpql, count);
		jpql.append(" where  empresaSetor.dataExclusao is null ");

		if (setorFilter != null) {

			boolean sigla = StringUtils.isNotBlank(setorFilter.getSigla());
			boolean descricao = StringUtils.isNotBlank(setorFilter.getDescricao());
			boolean id = setorFilter.getIdEmpresa() != null;


			if (id) {
				jpql.append(" and empresa.id = :idEmpresa ");
				parametros.put("idEmpresa", setorFilter.getIdEmpresa());
			}

			if (descricao) {
				jpql.append(" and upper(setor.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + setorFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
			}

			if (sigla) {
				jpql.append(" and upper(setor.sigla) like :sigla escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("sigla", "%" + setorFilter.getSigla().replace("%", "\\%").toUpperCase() + "%");
			}

			if (!count) {
				jpql.append(" order by setor.descricao ");
			}
		}
	}

	public Long getCountQueryPaginado(SetorFilter setorFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, setorFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}
	
	public Long verificandoExistenciaSetor(Long idEmpresa, Long idSetor) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select setor.id from EmpresaSetor empresaSetor ");
		jpql.append(" left join  empresaSetor.empresa empresa ");
		jpql.append(" left join  empresaSetor.setor setor ");

		jpql.append(" where empresaSetor.dataExclusao is null ");
		jpql.append(" and setor.id = :idSetor  ");
		jpql.append(" and empresa.id = :id  ");
		
		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);
		query.setParameter("id", idEmpresa);
		query.setParameter("idSetor", idSetor);

		return DAOUtil.getSingleResult(query);
	}
	
	
	
}
