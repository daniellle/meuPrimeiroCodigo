package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Trabalhador;
import com.google.common.collect.Maps;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

		montarJoinPesquisarPorId(segurancaFilter, jpql);

		jpql.append(" where trabalhador.id = :id");

		if (segurancaFilter != null && trabalhadorFilter.isAplicarDadosFilter()) {
			filtroIdsPesquisarPorId(segurancaFilter, id, jpql, parametros);
		}

		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		parametros.put("id", id);
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	private void filtroIdsPesquisarPorId(DadosFilter segurancaFilter, Long id, StringBuilder jpql,
			Map<String, Object> parametros) {
		if (segurancaFilter.temIdsEmpresa() && !segurancaFilter.isAdministrador()) {
			jpql.append(" and empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}

		if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
			if (id != null || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}

			jpql.append(" depRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}

		if (segurancaFilter.temIdsTrabalhador() && !segurancaFilter.isAdministrador()) {
			if (id != null || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
			parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
		}
	}

	private void montarJoinPesquisarPorId(DadosFilter segurancaFilter, StringBuilder jpql) {
		jpql.append("select trabalhador from Trabalhador trabalhador ");
		jpql.append(" left join fetch trabalhador.profissao p ");
		jpql.append(" left join fetch trabalhador.pais a ");
		jpql.append(" left join fetch trabalhador.municipio m ");
		jpql.append(" left join fetch m.estado e ");

		if (segurancaFilter != null) {
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" left join trabalhador.listaEmpresaTrabalhador listaEmpresaTrabalhador ");
				jpql.append(" left join listaEmpresaTrabalhador.empresa empresa ");
			}

			if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" left join empresa.empresaUats empresaUats ");
				jpql.append(" left join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
				jpql.append(" left join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
			}
		}
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

		montarJoinPaginado(jpql, count, segurancaFilter);

		if (trabalhadorFilter != null || segurancaFilter != null) {
			jpql.append(" where ");
		}

		if (trabalhadorFilter != null) {
			situacao = StringUtils.isNotBlank(trabalhadorFilter.getSituacao());
			cpf = StringUtils.isNotEmpty(trabalhadorFilter.getCpf());
			nome = StringUtils.isNotEmpty(trabalhadorFilter.getNome());
			nit = StringUtils.isNotEmpty(trabalhadorFilter.getNit());
			falecidos = trabalhadorFilter.isFalecidos();

			situacao = montarFiltroSituacaoPaginado(jpql, trabalhadorFilter, situacao);

			montarFiltroPaginado(jpql, parametros, trabalhadorFilter, situacao, cpf, nome, nit);

			montarFiltroFalecidoPaginado(jpql, trabalhadorFilter, situacao, cpf, nome, nit);
		}

		aplicarDadosFilter(jpql, parametros, trabalhadorFilter, segurancaFilter, situacao, cpf, nome, nit, falecidos);

		if (!count) {
			jpql.append(" order by trabalhador.nome");
		}

	}

	private void aplicarDadosFilter(StringBuilder jpql, Map<String, Object> parametros,
			TrabalhadorFilter trabalhadorFilter, DadosFilter segurancaFilter, boolean situacao, boolean cpf,
			boolean nome, boolean nit, boolean falecidos) {
		if (segurancaFilter != null && trabalhadorFilter != null && trabalhadorFilter.isAplicarDadosFilter()) {
			boolean hasFilters = cpf || nome || nit || situacao || !falecidos;
			addFiltroIds(jpql, parametros, segurancaFilter, hasFilters);
		}
	}

	private void addFiltroIds(StringBuilder jpql, Map<String, Object> parametros, DadosFilter segurancaFilter,
			boolean hasFilters) {
		if (segurancaFilter.temIdsEmpresa() && !segurancaFilter.isAdministrador()) {
			if (hasFilters) {
				jpql.append(" and ");
			}

			jpql.append(" empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}

		if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
			if (hasFilters || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}

			jpql.append(" depRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}

		if (segurancaFilter.temIdsTrabalhador() && !segurancaFilter.isAdministrador()) {
			if (hasFilters || segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
			parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
		}
	}

	private void montarFiltroFalecidoPaginado(StringBuilder jpql, TrabalhadorFilter trabalhadorFilter, boolean situacao,
			boolean cpf, boolean nome, boolean nit) {
		if (!trabalhadorFilter.isFalecidos()) {
			if (situacao || cpf || nome || nit) {
				jpql.append(" and ");
			}
			jpql.append(" trabalhador.dataFalecimento is null ");
		}
	}

	private boolean montarFiltroSituacaoPaginado(StringBuilder jpql, TrabalhadorFilter trabalhadorFilter,
			boolean situacao) {
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
		return situacao;
	}

	private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros,
			TrabalhadorFilter trabalhadorFilter, boolean situacao, boolean cpf, boolean nome, boolean nit) {
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
	}

	private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter) {
		if (count) {
			jpql.append("select count( DISTINCT trabalhador.id) from Trabalhador trabalhador ");
		} else {
			jpql.append("select DISTINCT trabalhador from Trabalhador trabalhador ");
		}

		if (segurancaFilter != null) {
			if (segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" inner join trabalhador.listaEmpresaTrabalhador listaEmpresaTrabalhador ");
				jpql.append(" inner join listaEmpresaTrabalhador.empresa empresa ");
			}

			if (segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador()) {
				jpql.append(" inner join empresa.empresaUats empresaUats ");
				jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
				jpql.append(" inner join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
			}
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
		TypedQuery<Trabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return DAOUtil.getSingleResult(query);
	}

	public Trabalhador buscarVacinasAlergiasMedicamentosAutoDeclarados(String cpf) {
		LOGGER.debug("Pesquisando dados auto-declarados do trabalhador por cpf.");
		TypedQuery<Trabalhador> query = criarConsultaPorTipo("select new Trabalhador(t.id, t.descricaoMedicamentos, t.descricaoAlergias, t.descricaoVacinas) from Trabalhador t where t.cpf = :cpf");
		query.setParameter("cpf", cpf);
		return DAOUtil.getSingleResult(query);
	}
	
	public Long buscarVidaAtiva(String id) {
		LOGGER.debug("Pesquisando Vida Ativa do trabalhador por id.");
		Query query = criarConsulta("select count(t.id) from EmpresaTrabalhador t where t.trabalhador.id = :id and t.dataFimContrato >= current_date ");
		query.setParameter("id", Long.parseLong(id));
		return DAOUtil.getSingleResult(query);
	}	

}
