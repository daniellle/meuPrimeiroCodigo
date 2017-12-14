package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Date;
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

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Trabalhador;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class TrabalhadorDAO extends BaseDAO<Trabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorDAO.class);

	@Inject
	public TrabalhadorDAO(EntityManager em) {
		super(em, Trabalhador.class);
	}

	public Trabalhador pesquisarPorId(TrabalhadorFilter trabalhadorFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Buscando Trabalhador por Id");
		
		Long id = trabalhadorFilter.getId();

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		jpql.append("select trabalhador from Trabalhador trabalhador ");
		jpql.append(" left join fetch trabalhador.profissao p ");
		jpql.append(" left join fetch trabalhador.pais a ");
		jpql.append(" left join fetch trabalhador.municipio m ");
		jpql.append(" left join fetch m.estado e ");

		if (segurancaFilter != null) {
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsDepRegional()) {
				jpql.append(" left join trabalhador.listaEmpresaTrabalhador listaEmpresaTrabalhador ");
				jpql.append(" left join listaEmpresaTrabalhador.empresa empresa ");
			}

			if (segurancaFilter.temIdsDepRegional()) {
				jpql.append(" left join empresa.empresaUats empresaUats ");
				jpql.append(" left join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
				jpql.append(" left join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
			}
		}

		jpql.append(" where trabalhador.id = :id");

		if (segurancaFilter != null && trabalhadorFilter.isAplicarDadosFilter()) {
			if (segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and empresa.id IN (:idsEmpresa) ");
				parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
			}

			if (segurancaFilter.temIdsDepRegional()) {
				if (id != null || segurancaFilter.temIdsEmpresa()) {
					jpql.append(" and ");
				}

				jpql.append(" depRegional.id IN (:idsDepRegional) ");
				parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
			}

			if (segurancaFilter.temIdsTrabalhador()) {
				if (id != null || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
					jpql.append(" and ");
				}
				jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
				parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
			}
		}

		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		parametros.put("id", id);
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	public Trabalhador pesquisarPorCpfDataNascimento(String cpf, Date dataNascimento) {
		LOGGER.debug("Buscando Trabalhador por cpf e data de nascimento");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select trabalhador from Trabalhador trabalhador ");
		jpql.append(" left join fetch trabalhador.profissao p ");
		jpql.append(" left join fetch trabalhador.pais a ");
		jpql.append(" left join fetch trabalhador.municipio m ");
		jpql.append(" left join fetch m.estado e ");
		jpql.append(" where trabalhador.cpf = :cpf");
		jpql.append(" and trabalhador.dataNascimento = :dataNascimento");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);
		query.setParameter("dataNascimento", dataNascimento);

		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<Trabalhador> pesquisarPaginado(TrabalhadorFilter trabalhadorFilter,
			DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Trabalhador por filtro");

		ListaPaginada<Trabalhador> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, trabalhadorFilter, false, segurancaFilter);
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(trabalhadorFilter, segurancaFilter));

		if (trabalhadorFilter != null && trabalhadorFilter.getPagina() != null
				&& trabalhadorFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((trabalhadorFilter.getPagina() - 1) * trabalhadorFilter.getQuantidadeRegistro());
			query.setMaxResults(trabalhadorFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public long getCountQueryPaginado(TrabalhadorFilter trabalhadorFilter, DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, trabalhadorFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			TrabalhadorFilter trabalhadorFilter, boolean count, DadosFilter segurancaFilter) {

		boolean situacao = false;
		boolean cpf = false;
		boolean nome = false;
		boolean nit = false;
		boolean falecidos = false;

		if (count) {
			jpql.append("select count( DISTINCT trabalhador.id) from Trabalhador trabalhador ");
		} else {
			jpql.append("select DISTINCT trabalhador from Trabalhador trabalhador ");
		}

		if (segurancaFilter != null) {
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsDepRegional()) {
				jpql.append(" inner join trabalhador.listaEmpresaTrabalhador listaEmpresaTrabalhador ");
				jpql.append(" inner join listaEmpresaTrabalhador.empresa empresa ");
			}

			if (segurancaFilter.temIdsDepRegional()) {
				jpql.append(" inner join empresa.empresaUats empresaUats ");
				jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
				jpql.append(" inner join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
			}
		}

		if (trabalhadorFilter != null || segurancaFilter != null) {
			jpql.append(" where ");
		}

		if (trabalhadorFilter != null) {
			situacao = StringUtils.isNotBlank(trabalhadorFilter.getSituacao());
			cpf = StringUtils.isNotEmpty(trabalhadorFilter.getCpf());
			nome = StringUtils.isNotEmpty(trabalhadorFilter.getNome());
			nit = StringUtils.isNotEmpty(trabalhadorFilter.getNit());
			falecidos = trabalhadorFilter.isFalecidos();

			if (situacao) {
				boolean verificarSituacao = false;
				if (Situacao.ATIVO.getCodigo().equals(trabalhadorFilter.getSituacao())) {
					jpql.append(" trabalhador.dataFalecimento ").append(Situacao.ATIVO.getQuery());
					verificarSituacao = true;
				}

				if (Situacao.INATIVO.getCodigo().equals(trabalhadorFilter.getSituacao())) {
					jpql.append(" trabalhador.dataFalecimento ").append(Situacao.INATIVO.getQuery());
					verificarSituacao = true;
				}
				situacao = verificarSituacao;
			}

			if (cpf) {
				if (situacao) {
					jpql.append(" and ");
				}
				jpql.append(" trabalhador.cpf = :cpf ");
				parametros.put("cpf", trabalhadorFilter.getCpf());
			}
			if (nome) {
				if (situacao || cpf) {
					jpql.append(" and ");
				}
				jpql.append(" UPPER(trabalhador.nome) like :nome escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("nome", "%" + trabalhadorFilter.getNome().replaceAll("%", "\\%").toUpperCase() + "%");
			}
			if (nit) {
				if (situacao || cpf || nome) {
					jpql.append(" and ");
				}
				jpql.append(" trabalhador.nit = :nit ");
				parametros.put("nit", trabalhadorFilter.getNit());
			}

			if (!trabalhadorFilter.isFalecidos()) {
				if (situacao || cpf || nome || nit) {
					jpql.append(" and ");
				}
				jpql.append(" trabalhador.dataFalecimento is null ");
			}
		}

		if (segurancaFilter != null && trabalhadorFilter != null && trabalhadorFilter.isAplicarDadosFilter()) {
			boolean hasFilters = cpf || nome || nit || situacao || !falecidos;

			if (segurancaFilter.temIdsEmpresa()) {
				if (hasFilters) {
					jpql.append(" and ");
				}

				jpql.append(" empresa.id IN (:idsEmpresa) ");
				parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
			}

			if (segurancaFilter.temIdsDepRegional()) {
				if (hasFilters || segurancaFilter.temIdsEmpresa()) {
					jpql.append(" and ");
				}

				jpql.append(" depRegional.id IN (:idsDepRegional) ");
				parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
			}

			if (segurancaFilter.temIdsTrabalhador()) {
				if (hasFilters || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
					jpql.append(" and ");
				}
				jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
				parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
			}
		}

		if (!count) {
			jpql.append(" order by trabalhador.nome");
		}

	}

	public List<Trabalhador> listarTodos() {
		LOGGER.debug("Listando todos os trabalhadores");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select trabalhador from Trabalhador trabalhador ");
		jpql.append(" order by trabalhador.nome");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());

		return query.getResultList();
	}

	public List<Trabalhador> pesquisarPorCPF(List<String> cpf) {
		LOGGER.debug("Pesquisando Trabalhador por CPF");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select t from Trabalhador t where t.cpf in (:cpf)");
		jpql.append(" order by t.nome");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return query.getResultList();
	}

	public Trabalhador pesquisarPorCpf(String cpf) {
		LOGGER.debug("Pesquisando Trabalhador por CPF");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select t from Trabalhador t where t.cpf = :cpf");
		jpql.append(" order by t.nome");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return DAOUtil.getSingleResult(query);
	}

}
