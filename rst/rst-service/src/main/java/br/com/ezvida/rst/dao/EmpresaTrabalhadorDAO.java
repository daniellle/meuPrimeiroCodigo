package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import br.com.ezvida.rst.dao.filter.EmpresaTrabalhadorFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class EmpresaTrabalhadorDAO extends BaseDAO<EmpresaTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorDAO.class);

	@Inject
	public EmpresaTrabalhadorDAO(EntityManager em) {
		super(em, EmpresaTrabalhador.class);
	}

	public Set<EmpresaTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Pesquisando EmpresaTrabalhador por idTrabalhador");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where trabalhador.id = :idTrabalhador");
		TypedQuery<EmpresaTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);
		return Sets.newHashSet(query.getResultList());

	}

	public EmpresaTrabalhador pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando EmpresaTrabalhador por id");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where empresaTrabalhador.id = :id");
		TypedQuery<EmpresaTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return DAOUtil.getSingleResult(query);

	}

	public Set<EmpresaTrabalhador> pesquisarPorTrabalhador(String cpf) {
		LOGGER.debug("Pesquisando EmpresaTrabalhador por cpf");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where trabalhador.cpf = :cpf");
		TypedQuery<EmpresaTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);
		return Sets.newHashSet(query.getResultList());

	}

	public long getCountQueryPaginado(EmpresaTrabalhadorFilter empresaTrabalhadorFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, empresaTrabalhadorFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<EmpresaTrabalhador> pesquisarPaginado(EmpresaTrabalhadorFilter empresaTrabalhadorFilter) {
		LOGGER.debug("Pesquisando TrabalhadorDependente paginado");

		ListaPaginada<EmpresaTrabalhador> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, empresaTrabalhadorFilter, false);
		TypedQuery<EmpresaTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(empresaTrabalhadorFilter));

		if (empresaTrabalhadorFilter != null) {
			query.setFirstResult(
					(empresaTrabalhadorFilter.getPagina() - 1) * empresaTrabalhadorFilter.getQuantidadeRegistro());
			query.setMaxResults(empresaTrabalhadorFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			EmpresaTrabalhadorFilter empresaTrabalhadorFilter, boolean count) {
		if (count) {
			jpql.append("select count(empresaTrabalhador.id) from EmpresaTrabalhador empresaTrabalhador ");
			jpql.append("left join empresaTrabalhador.empresa empresa ");
			jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
		} else {
			jpql.append("select empresaTrabalhador from EmpresaTrabalhador empresaTrabalhador ");
			jpql.append("left join fetch empresaTrabalhador.empresa empresa ");
			jpql.append("left join fetch empresaTrabalhador.trabalhador trabalhador ");
		}

		jpql.append(" where empresaTrabalhador.dataExclusao is null ");

		if (empresaTrabalhadorFilter != null) {

			boolean idEmpresa = empresaTrabalhadorFilter.getIdEmpresa() != null;
			boolean cpf = StringUtils.isNotEmpty(empresaTrabalhadorFilter.getCpf());
			boolean nome = StringUtils.isNotEmpty(empresaTrabalhadorFilter.getNome());

			if (idEmpresa) {
				jpql.append(" and empresa.id = :idEmpresa ");
				parametros.put("idEmpresa", empresaTrabalhadorFilter.getIdEmpresa());
			}

			if (cpf) {
				jpql.append(" and trabalhador.cpf = :cpf ");
				parametros.put("cpf", empresaTrabalhadorFilter.getCpf());
			}

			if (nome) {
				jpql.append(" and UPPER(trabalhador.nome) like :nome escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("nome", "%" + empresaTrabalhadorFilter.getNome().replaceAll("%", "\\%").toUpperCase() + "%");
			}

			if (!count) {
				jpql.append(" order by empresaTrabalhador.dataAdmissao desc ");
			}
		}

	}

	public void inativar(Long idEmpresa) {
		LOGGER.debug("Inativando EmpresaTrabalhador");
		StringBuilder jpql = new StringBuilder();
		jpql.append(" update EmpresaTrabalhador empresaTrabalhador set empresaTrabalhador.situacao = :situacao ");
		jpql.append(" where empresaTrabalhador.empresa.id = :idEmpresa");
		Query query = criarConsulta(jpql.toString());

		query.setParameter("idEmpresa", idEmpresa);
		query.setParameter("situacao", Situacao.INATIVO);
		query.executeUpdate();
	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select empresaTrabalhador from EmpresaTrabalhador empresaTrabalhador ");
		jpql.append("inner join fetch empresaTrabalhador.empresa empresa ");
		jpql.append("inner join fetch empresaTrabalhador.trabalhador trabalhador ");
	}

	@SuppressWarnings("unchecked")
	public List<EmpresaTrabalhador> buscarEmpresaTrabalhadorNoPeriodo(EmpresaTrabalhador empresaTrabalhador) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> parameters = Maps.newHashMap();
		sql.append(" select * from ");
		sql.append(" emp_trabalhador empresaTrabalhador ");
		sql.append(
				" where empresaTrabalhador.dt_exclusao is null and id_empresa_fk = :idEmpresa and id_trabalhador_fk = :idTrabalhador and ");
		sql.append(" (date_trunc('day', to_timestamp(:dataAdmissaoAtual, 'YYYY-MM-dd')) ");
		sql.append(" between date_trunc('day', empresaTrabalhador.dt_admissao) and ");
		sql.append(" date_trunc('day', empresaTrabalhador.dt_demissao) or ");

		sql.append(" date_trunc('day', to_timestamp(:dataDemissaoAtual, 'YYYY-MM-dd')) ");
		sql.append(" between date_trunc('day', empresaTrabalhador.dt_admissao) and ");
		sql.append(" date_trunc('day', empresaTrabalhador.dt_demissao) or ");

		sql.append(" (date_trunc('day', empresaTrabalhador.dt_admissao) ");
		sql.append(" between date_trunc('day', to_timestamp(:dataAdmissaoAtual, 'YYYY-MM-dd')) and ");
		sql.append(" date_trunc('day', to_timestamp(:dataDemissaoAtual, 'YYYY-MM-dd')) ");
		sql.append(" and date_trunc('day', empresaTrabalhador.dt_demissao) ");
		sql.append(" between date_trunc('day', to_timestamp(:dataAdmissaoAtual, 'YYYY-MM-dd')) and ");
		sql.append(" date_trunc('day', to_timestamp(:dataDemissaoAtual, 'YYYY-MM-dd')))");
		sql.append(") ");

		Query query = criarConsultaNativa(sql.toString(), EmpresaTrabalhador.class);

		DAOUtil.setParameterMap(query, parameters);
		query.setParameter("idEmpresa", empresaTrabalhador.getEmpresa().getId());
		query.setParameter("idTrabalhador", empresaTrabalhador.getTrabalhador().getId());
		query.setParameter("dataAdmissaoAtual", empresaTrabalhador.getDataAdmissao());
		query.setParameter("dataDemissaoAtual",
				empresaTrabalhador.getDataDemissao() == null ? new Date() : empresaTrabalhador.getDataDemissao());

		return query.getResultList();
	}

	public EmpresaTrabalhador buscarEmpresaTrabalhadorAtivo(EmpresaTrabalhador empresaTrabalhador) {
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where empresaTrabalhador.dataExclusao is null and empresaTrabalhador.dataDemissao is null  ");
		jpql.append(" and  empresa.id = :idEmpresa");
		jpql.append(" and  trabalhador.id = :idTrabalhador");

		TypedQuery<EmpresaTrabalhador> query = criarConsultaPorTipo(jpql.toString(), EmpresaTrabalhador.class);
		query.setParameter("idEmpresa", empresaTrabalhador.getEmpresa().getId());
		query.setParameter("idTrabalhador", empresaTrabalhador.getTrabalhador().getId());
		


		return DAOUtil.getSingleResult(query);
	}

	public List<EmpresaTrabalhador> buscarEmpresaTrabalhadorAdmitido (EmpresaTrabalhador empresaTrabalhador) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select empresaTrabalhador ");
		jpql.append(" from EmpresaTrabalhador empresaTrabalhador ");
		
		jpql.append(" where empresaTrabalhador.dataExclusao is null and empresaTrabalhador.dataDemissao is null ");
		jpql.append(" and  empresaTrabalhador.id = :idEmpresaTrabalhador");

		if (empresaTrabalhador.getId() != null) {
			jpql.append(" and  empresaTrabalhador.id != :idEmpresaTrabalhador ");
		}

		if (empresaTrabalhador.getDataDemissao() != null) {
			jpql.append(" and  empresaTrabalhador.dataAdmissao <= :dataDemissao ");
		}

		TypedQuery<EmpresaTrabalhador> query = criarConsultaPorTipo(jpql.toString(), EmpresaTrabalhador.class);
		query.setParameter("idEmpresaTrabalhador", empresaTrabalhador.getId());

		if (empresaTrabalhador.getId() != null) {
			query.setParameter("idEmpresaTrabalhador", empresaTrabalhador.getId());
		}

		if (empresaTrabalhador.getDataDemissao() != null) {
			query.setParameter("dataDemissao", empresaTrabalhador.getDataDemissao());
		}

		return query.getResultList();
	}

	public EmpresaTrabalhador pesquisarPorIdEIdEmpresa(Long idEmpresaTrabalhador, Long idEmpresa) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select empresaTrabalhador ");
		jpql.append(" from EmpresaTrabalhador empresaTrabalhador ");
		jpql.append(" where empresaTrabalhador.dataExclusao is null ");
		jpql.append(" and empresaTrabalhador.id = :idEmpresaTrabalhador and empresaTrabalhador.empresa.id = :idEmpresa ");

		TypedQuery<EmpresaTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idEmpresaTrabalhador", idEmpresaTrabalhador);
		query.setParameter("idEmpresa", idEmpresa);

		return DAOUtil.getSingleResult(query);
	}
}
