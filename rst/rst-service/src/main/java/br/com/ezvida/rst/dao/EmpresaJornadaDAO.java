package br.com.ezvida.rst.dao;

import java.util.ArrayList;
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
import br.com.ezvida.rst.model.EmpresaJornada;
import fw.core.jpa.DAOUtil;

public class EmpresaJornadaDAO extends BaseRstDAO<EmpresaJornada, Long> {
	
	@Inject
	public EmpresaJornadaDAO(EntityManager em) {
		super(em, EmpresaJornada.class);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaJornadaDAO.class);
	
	public ListaPaginada<EmpresaJornada> retornarPorEmpresa(EmpresaFilter empresaFilter){
		LOGGER.debug("Pesquisando Empresa Jornada por empresa");
		
		ListaPaginada<EmpresaJornada> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		queryPorEmpresa(jpql, parametros, empresaFilter.getId(), false);
		TypedQuery<EmpresaJornada> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		
		listaPaginada.setQuantidade(getCountQueryPaginado(empresaFilter.getId()));

		query.setFirstResult((empresaFilter.getPagina() - 1) * empresaFilter.getQuantidadeRegistro());
		query.setMaxResults(empresaFilter.getQuantidadeRegistro());

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}
	
	private Long getCountQueryPaginado(Long idEmpresa) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		queryPorEmpresa(jpql, parametros, idEmpresa, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

	private void queryPorEmpresa(StringBuilder jpql, Map<String, Object> parametros, Long id, boolean count) {

		if (!count) {
			jpql.append("select empresaJornada from EmpresaJornada empresaJornada ");
			jpql.append(" left join fetch empresaJornada.empresa empresa ");
			jpql.append(" left join fetch empresaJornada.jornada jornada ");
		} else {
			jpql.append("select count(empresaJornada.id) from EmpresaJornada empresaJornada ");
			jpql.append(" left join empresaJornada.empresa empresa ");
			jpql.append(" left join empresaJornada.jornada jornada ");
		}

		jpql.append(" where empresaJornada.dataExclusao is null ");
		
		
		if(id != null) {
			jpql.append(" and empresa.id = :id ");
			parametros.put("id", id);
		}
		
		if(!count) {
			jpql.append(" Order By jornada.descricao ");
		}
		
	}
	
	
	public EmpresaJornada pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando Empresa Jornada por empresa");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select empresaJornada from EmpresaJornada empresaJornada ");
		jpql.append(" left join fetch empresaJornada.empresa empresa ");
		jpql.append(" left join fetch empresaJornada.jornada jornada ");

		if (id != null) {
			jpql.append(" where empresaJornada.id = :id ");
		}

		TypedQuery<EmpresaJornada> query = criarConsultaPorTipo(jpql.toString());

		if (id != null) {
			query.setParameter("id", id);
		}

		return query.getSingleResult();
	}

	public List<EmpresaJornada> pesquisarPorEmpresa(Long idEmpresa) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select empresaJornada from EmpresaJornada empresaJornada ");
		jpql.append(" left join fetch empresaJornada.empresa empresa ");
		jpql.append(" left join fetch empresaJornada.jornada jornada ");

		jpql.append(" where empresaJornada.dataExclusao is null ");
		jpql.append(" and empresa.id = :id ");

		TypedQuery<EmpresaJornada> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idEmpresa);

		return query.getResultList();
	}
	
	public List<Long> pesquisarPorEmpresaids(Long idEmpresa) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select jornada.id from EmpresaJornada empresaJornada ");
		jpql.append(" left join  empresaJornada.empresa empresa ");
		jpql.append(" left join  empresaJornada.jornada jornada ");

		jpql.append(" where empresaJornada.dataExclusao is null ");
		jpql.append(" and empresa.id = :id ");

		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);
		query.setParameter("id", idEmpresa);

		return query.getResultList();
	}
		
	
	public Long verificandoExistenciaJornada(Long idEmpresa, Long idJornada) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select jornada.id from EmpresaJornada empresaJornada ");
		jpql.append(" left join  empresaJornada.empresa empresa ");
		jpql.append(" left join  empresaJornada.jornada jornada ");

		jpql.append(" where empresaJornada.dataExclusao is null ");
		jpql.append(" and jornada.id = :idJornada  ");
		jpql.append(" and empresa.id = :id  ");
		
		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);
		query.setParameter("id", idEmpresa);
		query.setParameter("idJornada", idJornada);

		return DAOUtil.getSingleResult(query);
	}

}
