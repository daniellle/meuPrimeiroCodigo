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

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ParceiroFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Parceiro;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class ParceiroDAO extends BaseDAO<Parceiro, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroDAO.class);

	@Inject
	public ParceiroDAO(EntityManager em) {
		super(em, Parceiro.class);
	}

	@Override
	public Parceiro pesquisarPorId(Long id) {
		LOGGER.debug("Pesquisando Parceiro por Id");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select parceiro from Parceiro parceiro ");
		jpql.append(" left join fetch parceiro.segmento segmento ");
		jpql.append(" left join fetch parceiro.porteEmpresa porteEmpresa ");
		jpql.append(" left join fetch parceiro.tipoEmpresa tipoEmpresa ");

		jpql.append(" where parceiro.id = :id");

		TypedQuery<Parceiro> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);

		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<Parceiro> pesquisarPaginado(ParceiroFilter parceiroFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Parceiro por filtro");

		ListaPaginada<Parceiro> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, parceiroFilter, false, segurancaFilter);
		TypedQuery<Parceiro> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(parceiroFilter, segurancaFilter));

		if (parceiroFilter != null) {
			query.setFirstResult((parceiroFilter.getPagina() - 1) * parceiroFilter.getQuantidadeRegistro());
			query.setMaxResults(parceiroFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private Long getCountQueryPaginado(ParceiroFilter parceiroFilter, DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, parceiroFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);

	}
	
	private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter) {
		if (count) {
			jpql.append("select count(DISTINCT parceiro.id) from Parceiro parceiro ");
		} else {
			jpql.append("select DISTINCT parceiro from Parceiro parceiro ");
		}
		
		jpql.append(" left join parceiro.parceiroEspecialidades parceiroEspecialidades ");
		jpql.append(" left join parceiroEspecialidades.especialidade especialidade ");

		if (segurancaFilter != null && !segurancaFilter.isAdministrador() && segurancaFilter.temIdsDepRegional() || segurancaFilter.isGetorUnidadeSESI()) {
			jpql.append(" left join parceiro.parceiroProdutoServicos parceiroProdutoServicos ");
			jpql.append(" left join parceiroProdutoServicos.produtoServico produtoServico ");
			jpql.append(
					" left join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos ");
			jpql.append(" left join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional ");
			jpql.append(" left join departamentoRegional.unidadeAtendimentoTrabalhador uat ");
			jpql.append(" left join uat.empresaUats empresaUat ");
			jpql.append(" left join empresaUat.empresa empresa ");
			jpql.append(" left join empresa.empresaTrabalhadores empresaTrabalhadores ");
			jpql.append(" left join empresaTrabalhadores.trabalhador trabalhador ");
		}
	}
	
	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, ParceiroFilter parceiroFilter,
			boolean count, DadosFilter segurancaFilter) {
		
		montarJoinPaginado( jpql,  count,  segurancaFilter);
		
		boolean temIdDepRegional = segurancaFilter != null && segurancaFilter.temIdsDepRegional() && !segurancaFilter.isAdministrador();
		if (parceiroFilter != null) {
			boolean cnpj = StringUtils.isNotEmpty(parceiroFilter.getCpfCnpj());
			boolean razaoSocialNome = StringUtils.isNotEmpty(parceiroFilter.getRazaoSocialNome());
			boolean especialidade = parceiroFilter.getEspecialidade() != null;
			boolean status = parceiroFilter.getSituacao() != null;
			
			if (cnpj || razaoSocialNome || especialidade || status) {
				jpql.append(" where ");
			}
			
			montarFiltroPaginado(jpql, parametros, parceiroFilter, cnpj, razaoSocialNome, especialidade, status);

			montarDepId(jpql, parametros, segurancaFilter, temIdDepRegional, cnpj, razaoSocialNome, especialidade,
					status);

			if (!count) {
				jpql.append(" order by parceiro.nome ");

			}
		}
	}

	private void montarDepId(StringBuilder jpql, Map<String, Object> parametros, DadosFilter segurancaFilter,
			boolean temIdDepRegional, boolean cnpj, boolean razaoSocialNome, boolean especialidade, boolean status) {
		if ((cnpj || razaoSocialNome || especialidade || status) && (temIdDepRegional)) {

			jpql.append("  and departamentoRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());

		}
		if(segurancaFilter.isGetorUnidadeSESI()){
			if (cnpj || razaoSocialNome || especialidade || status || temIdDepRegional){
				jpql.append(" and ");
			}
			jpql.append(" uat.id IN (:idsUnidadeSESI) ");
			parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
		}
	}

	private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros, ParceiroFilter parceiroFilter,
			boolean cnpj, boolean razaoSocialNome, boolean especialidade, boolean status) {
		String and = "and";
		if (cnpj) {
			jpql.append(" parceiro.numeroCnpjCpf = :numeroCnpjCpf ");
			parametros.put("numeroCnpjCpf", parceiroFilter.getCpfCnpj());
		}
		
		if (razaoSocialNome) {
			if (cnpj) {
				jpql.append(and);
			}
			jpql.append(" set_simple_name(UPPER(parceiro.nome)) like set_simple_name(:nome) escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("nome", "%" + parceiroFilter.getRazaoSocialNome().replace("%", "\\%").toUpperCase().replace(" ", "%") + "%");
		}
		
		if (especialidade) {
			if (cnpj || razaoSocialNome) {
				jpql.append(and);
			}

			jpql.append(" (parceiroEspecialidades.dataExclusao is null and especialidade.id = :idEspecialidade) ");
			parametros.put("idEspecialidade", parceiroFilter.getEspecialidade());
		}
		
		montarFiltroStatus(jpql, parceiroFilter, cnpj, razaoSocialNome, especialidade, status);
	}

	private void montarFiltroStatus(StringBuilder jpql, ParceiroFilter parceiroFilter, boolean cnpj,
			boolean razaoSocialNome, boolean especialidade, boolean status) {
		if (status) {
			if (cnpj || razaoSocialNome || especialidade) {
				jpql.append("and");
			}

			if (Situacao.ATIVO.getCodigo().equals(parceiroFilter.getSituacao())) {
				jpql.append(" parceiro.dataDesligamento ").append(Situacao.ATIVO.getQuery());
			} else if (Situacao.INATIVO.getCodigo().equals(parceiroFilter.getSituacao())) {
				jpql.append(" parceiro.dataDesligamento ").append(Situacao.INATIVO.getQuery());
			}
		}
	}

	public List<Parceiro> listarTodos() {
		LOGGER.debug("Listando todos os Parceiro ... ");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select parceiro from Parceiro parceiro ");
		jpql.append(" order by parceiro.numeroCnpjCpf ");
		TypedQuery<Parceiro> query = criarConsultaPorTipo(jpql.toString());

		return query.getResultList();
	}

	public Parceiro pesquisarPorCNPJ(String cnpj) {
		LOGGER.debug("Pesquisando Parceiro por CNPJ");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select parceiro from Parceiro parceiro where parceiro.numeroCnpjCpf = :numeroCnpjCpf");

		TypedQuery<Parceiro> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("numeroCnpjCpf", cnpj);

		return DAOUtil.getSingleResult(query);
	}
}
