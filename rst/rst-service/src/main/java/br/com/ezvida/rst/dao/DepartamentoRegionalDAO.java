package br.com.ezvida.rst.dao;

import java.util.ArrayList;
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

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.DepartamentoRegionalFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.DepartamentoRegional;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class DepartamentoRegionalDAO extends BaseDAO<DepartamentoRegional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoRegionalDAO.class);

	@Inject
	public DepartamentoRegionalDAO(EntityManager em) {
		super(em, DepartamentoRegional.class, "razaoSocial");
	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select d from DepartamentoRegional d ");
		jpql.append("left join fetch d.listaEndDepRegional listaEnd ");
		jpql.append("left join fetch listaEnd.endereco endereco ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
	}

	@Override
	public DepartamentoRegional pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando DepartamentoRegional por Id");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select d from DepartamentoRegional d ");
		jpql.append("left join fetch d.listaEndDepRegional listaEnd ");
		jpql.append("left join fetch listaEnd.endereco endereco ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
		jpql.append("left join fetch d.listaTelDepRegional listaTel ");
		jpql.append("left join fetch listaTel.telefone telefone ");
		jpql.append("left join fetch d.listaEmailDepRegional listaEmail ");
		jpql.append("left join fetch listaEmail.email email ");
		jpql.append("where d.id = :id");

		Query query = criarConsulta(jpql.toString());
		query.setParameter("id", id);

		return DAOUtil.getSingleResult(query);
	}
	
	private void montarJoinsListarTodos(boolean dadosFilter, StringBuilder jpql) {
		if (dadosFilter) {
			jpql.append("left join departamentoRegional.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
			jpql.append("left join unidadeAtendimentoTrabalhador.empresaUats empresaUats ");
			jpql.append("left join empresaUats.empresa empresa ");
			jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhadores ");
			jpql.append("left join empresaTrabalhadores.trabalhador trabalhador ");
		}
	}
	
	private void montarFiltroSituacao(Situacao situacao, StringBuilder jpql ) {
		if (!Situacao.TODOS.equals(situacao)) {
			jpql.append("where");
			if (Situacao.ATIVO.equals(situacao)) {
				jpql.append(" departamentoRegional.dataDesativacao ").append(Situacao.ATIVO.getQuery());
			}

			if (Situacao.INATIVO.equals(situacao)) {
				jpql.append(" departamentoRegional.dataDesativacao ").append(Situacao.INATIVO.getQuery());
			}
		}
	}
	
	private void montarFiltroListarTodos(Situacao situacao, Map<String, Object> parametros, DadosFilter dados,
			StringBuilder jpql) {

		if (dados.temIdsDepRegional()) {
			if (!Situacao.TODOS.equals(situacao)) {
				jpql.append(" and ");
			} else {
				jpql.append(" where ");
			}
			jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", dados.getIdsDepartamentoRegional());
		}

		if (dados.temIdsEmpresa()) {
			if (dados.temIdsDepRegional() || !Situacao.TODOS.equals(situacao)) {
				jpql.append(" and ");
			} else {
				jpql.append(" where ");
			}
			jpql.append(" empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", dados.getIdsEmpresa());
		}

		if (dados.temIdsTrabalhador()) {
			if (dados.temIdsEmpresa() || dados.temIdsDepRegional() || !Situacao.TODOS.equals(situacao)) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
			parametros.put("idsTrabalhador", dados.getIdsTrabalhador());
		}
	}
	
	public List<DepartamentoRegional> listarTodos(Situacao situacao, DadosFilter dados,
			DepartamentoRegionalFilter departamentoRegionalFilter) {
		LOGGER.debug("Listando todos os departamentos regionais");

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		
		boolean dadosFilter = departamentoRegionalFilter != null && departamentoRegionalFilter.isAplicarDadosFilter();

		jpql.append("select DISTINCT departamentoRegional from DepartamentoRegional departamentoRegional ");
		montarJoinsListarTodos(dadosFilter,jpql);
		montarFiltroSituacao(situacao,jpql);
		if(dadosFilter) {			
			montarFiltroListarTodos(situacao, parametros,  dados, jpql);
		}
		
		jpql.append(" order by departamentoRegional.razaoSocial ");

		TypedQuery<DepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return query.getResultList();
	}

	public ListaPaginada<DepartamentoRegional> pesquisarPaginado(DepartamentoRegionalFilter departamentoRegionalFilter,
			DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado departamento regional por filtro");

		ListaPaginada<DepartamentoRegional> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		getQueryPaginado(jpql, parametros, departamentoRegionalFilter, false, segurancaFilter);

		TypedQuery<DepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(departamentoRegionalFilter, segurancaFilter));

		if (departamentoRegionalFilter != null) {
			query.setFirstResult(
					(departamentoRegionalFilter.getPagina() - 1) * departamentoRegionalFilter.getQuantidadeRegistro());
			query.setMaxResults(departamentoRegionalFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			DepartamentoRegionalFilter departamentoRegionalFilter, boolean count, DadosFilter segurancaFilter) {

		if (count) {
			jpql.append("select count(d.id) from DepartamentoRegional d ");
			jpql.append("left join d.listaEndDepRegional listaEnd ");
			jpql.append("left join listaEnd.endereco endereco ");
			jpql.append("left join endereco.municipio municipio ");
			jpql.append("left join municipio.estado estado ");
		} else {
			jpql.append("select d from DepartamentoRegional d ");
			jpql.append("left join fetch d.listaEndDepRegional listaEnd ");
			jpql.append("left join fetch listaEnd.endereco endereco ");
			jpql.append("left join fetch endereco.municipio municipio ");
			jpql.append("left join fetch municipio.estado estado ");
		}

		if (departamentoRegionalFilter != null) {
			this.montarFiltro(jpql, departamentoRegionalFilter, parametros, segurancaFilter, count);
		}
	}

	private void adicionarFiltroSituacao(StringBuilder jpql, DepartamentoRegionalFilter departamentoRegionalFilter) {
		if (Situacao.ATIVO.getCodigo().equals(departamentoRegionalFilter.getSituacao())) {
			jpql.append(" ( d.dataExclusao ").append(Situacao.ATIVO.getQuery());
			jpql.append(" and ");
			jpql.append(" d.dataDesativacao ").append(Situacao.ATIVO.getQuery());
			jpql.append(" )");

		} else if (Situacao.INATIVO.getCodigo().equals(departamentoRegionalFilter.getSituacao())) {
			jpql.append(" ( d.dataExclusao ").append(Situacao.INATIVO.getQuery());
			jpql.append(" or ");
			jpql.append(" d.dataDesativacao ").append(Situacao.INATIVO.getQuery());
			jpql.append(" )");
		}
	}

	private void montarFiltro(StringBuilder jpql, DepartamentoRegionalFilter departamentoRegionalFilter,
			Map<String, Object> parametros, DadosFilter segurancaFilter, boolean count) {

		jpql.append(" where ");

		boolean cnpj = StringUtils.isNotEmpty(departamentoRegionalFilter.getCnpj());
		boolean razaoSocial = StringUtils.isNotEmpty(departamentoRegionalFilter.getRazaoSocial());
		boolean departamento = departamentoRegionalFilter.getIdEstado() != null
				&& departamentoRegionalFilter.getIdEstado() > 0;
		boolean status = StringUtils.isNotBlank(departamentoRegionalFilter.getSituacao());
		boolean and = false;

		if (cnpj) {
			jpql.append(" d.cnpj = :cnpj  ");
			parametros.put("cnpj", departamentoRegionalFilter.getCnpj());
			and = true;
		}

		if (razaoSocial) {
			if (and) {
				jpql.append(" and ");
			}
			jpql.append(" UPPER(d.razaoSocial) like :razaoSocial escape :sc  ");
			parametros.put("sc", "\\");
			parametros.put("razaoSocial",
					"%" + departamentoRegionalFilter.getRazaoSocial().replace("%", "\\%").toUpperCase() + "%");
			and = true;
		}
		if (departamento) {
			if (and) {
				jpql.append(" and ");
			}
			jpql.append(" estado.id = :idEstado ");
			parametros.put("idEstado", departamentoRegionalFilter.getIdEstado());
			and = true;
		}

		if (status) {
			if (and) {
				jpql.append(" and ");
			}
			this.adicionarFiltroSituacao(jpql, departamentoRegionalFilter);
		}
		this.inserirSeguracaFilter(jpql, segurancaFilter, count, parametros);
	}

	private void inserirSeguracaFilter(StringBuilder jpql, DadosFilter segurancaFilter, boolean count,
			Map<String, Object> parametros) {
		if (segurancaFilter.temIdsDepRegional()) {
			jpql.append("and");
			jpql.append(" d.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}
		if (!count) {
			jpql.append(" order by d.razaoSocial");

		}
	}

	public long getCountQueryPaginado(DepartamentoRegionalFilter departamentoRegionalFilter,
			DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, departamentoRegionalFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public DepartamentoRegional pesquisarPorCNPJ(String cnpj) {
		LOGGER.debug("Pesquisando departamento regional por CNPJ");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where d.cnpj = :cnpj");
		TypedQuery<DepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cnpj", cnpj);
		return DAOUtil.getSingleResult(query);

	}

	public List<DepartamentoRegional> buscarPorTrabalhador(Long idTrabalhador) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select distinct departamentoRegional from EmpresaTrabalhador et ");
		jpql.append(" inner join et.empresa empresa ");
		jpql.append(" inner join empresa.empresaUats empresaUats ");
		jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
		jpql.append(" inner join unidadeAtendimentoTrabalhador.departamentoRegional departamentoRegional ");

		jpql.append(" where et.trabalhador.id = :idTrabalhador ");

		TypedQuery<DepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);

		return query.getResultList();
	}

	public List<DepartamentoRegional> pesquisarPorIds(Set<Long> ids) {
		LOGGER.debug("Pesquisando Departamento por Ids");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select new DepartamentoRegional( ");
		jpql.append("  departamentoRegional.cnpj, departamentoRegional.razaoSocial, departamentoRegional.siglaDR, estado.siglaUF) ");
		jpql.append("  from DepartamentoRegional departamentoRegional ");
		jpql.append("	left join departamentoRegional.listaEndDepRegional enderecos ");
		jpql.append("	left join enderecos.endereco endereco ");
		jpql.append("	left join endereco.municipio municipio ");
		jpql.append("	left join municipio.estado estado ");
		jpql.append(" where departamentoRegional.id in (:ids) ");

		TypedQuery<DepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("ids", ids);

		return query.getResultList();
	}
}
