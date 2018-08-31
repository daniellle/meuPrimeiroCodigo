package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.TrabalhadorDependente;
import fw.core.jpa.DAOUtil;

public class TrabalhadorDependenteDAO extends BaseRstDAO<TrabalhadorDependente, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorDependenteDAO.class);

	@Inject
	public TrabalhadorDependenteDAO(EntityManager em) {
		super(em, TrabalhadorDependente.class);
	}

	public Set<TrabalhadorDependente> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Pesquisando TrabalhadorDependente por idTrabalhador");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where trabalhadorDependente.dataExclusao is null and trabalhador.id = :idTrabalhador");
		TypedQuery<TrabalhadorDependente> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);
		return Sets.newHashSet(query.getResultList());

	}

	public ListaPaginada<TrabalhadorDependente> pesquisarPorTrabalhador(TrabalhadorFilter trabalhadorFilter) {
		LOGGER.debug("Pesquisando TrabalhadorDependente por idTrabalhador");

		ListaPaginada<TrabalhadorDependente> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		getQueryPaginado(jpql, parametros, trabalhadorFilter, false);

		TypedQuery<TrabalhadorDependente> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(trabalhadorFilter));

		if (trabalhadorFilter != null) {
			query.setFirstResult((trabalhadorFilter.getPagina() - 1) * trabalhadorFilter.getQuantidadeRegistro());
			query.setMaxResults(trabalhadorFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public Long getCountQueryPaginado(TrabalhadorFilter trabalhadorFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, trabalhadorFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}
	
	@Override
	public TrabalhadorDependente pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando TrabalhadorDependente por id");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where trabalhadorDependente.id = :id ");
		TypedQuery<TrabalhadorDependente> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return DAOUtil.getSingleResult(query);
	}

	public TrabalhadorDependente pesquisarDependentePorCPF(String cpf) {
		LOGGER.debug("Pesquisando TrabalhadorDependente por cpf");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where dependente.cpf = :cpf and trabalhadorDependente.dataExclusao = null");
		TypedQuery<TrabalhadorDependente> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);
		return DAOUtil.getSingleResult(query);
	}
	
	public List<TrabalhadorDependente> pesquisarPorCPFLista(String cpf) {
		LOGGER.debug("Pesquisando TrabalhadorDependente por cpf");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where dependente.cpf = :cpf");
		TypedQuery<TrabalhadorDependente> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);
		return query.getResultList();
	}

	public List<TrabalhadorDependente> pesquisarPorCPFAtivos(Long idTrabalhador, List<String> cpfs) {
		LOGGER.debug("Pesquisando TrabalhadorDependente por CPF");
		StringBuilder jpql = new StringBuilder();

		jpql.append("select trabalhadorDependente from TrabalhadorDependente trabalhadorDependente ");
		jpql.append("left join fetch trabalhadorDependente.trabalhador trabalhador ");
		jpql.append("left join fetch trabalhadorDependente.dependente dependente ");
		jpql.append(" where dependente.cpf in (:cpf) and trabalhador.id <> :idTrabalhador ");
		jpql.append(" and trabalhadorDependente.dataExclusao is null ");
		jpql.append(" and trabalhadorDependente.inativo = :inativo ");
		jpql.append(" order by dependente.nome ");

		TypedQuery<TrabalhadorDependente> query = criarConsultaPorTipo(jpql.toString());

		query.setParameter("cpf", cpfs);
		query.setParameter("idTrabalhador", idTrabalhador);
		query.setParameter("inativo", SimNao.SIM);

		return query.getResultList();
	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select trabalhadorDependente from TrabalhadorDependente trabalhadorDependente ");
		jpql.append("left join fetch trabalhadorDependente.dependente dependente ");
		jpql.append("left join fetch trabalhadorDependente.trabalhador trabalhador ");
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			TrabalhadorFilter trabalhadorFilter, boolean count) {

		if (count) {
			jpql.append(" select count(trabalhadorDependente.id) ");
			jpql.append(" from TrabalhadorDependente trabalhadorDependente ");
			jpql.append(" inner join trabalhadorDependente.dependente dependente ");
			jpql.append(" inner join trabalhadorDependente.trabalhador trabalhador ");
		} else {
			jpql.append(" select trabalhadorDependente ");
			jpql.append(" from TrabalhadorDependente trabalhadorDependente ");
			jpql.append(" inner join fetch trabalhadorDependente.dependente dependente ");
			jpql.append(" inner join fetch trabalhadorDependente.trabalhador trabalhador ");
		}

		if (trabalhadorFilter != null) {

			jpql.append(" where  trabalhadorDependente.dataExclusao is null ");

			if (trabalhadorFilter.getId() != null) {
				jpql.append(" and trabalhador.id = :trabalhador ");
				parametros.put("trabalhador", trabalhadorFilter.getId());
			}

			if (!count) {
				jpql.append(" order by dependente.nome ");
			}
		}

	}

}
