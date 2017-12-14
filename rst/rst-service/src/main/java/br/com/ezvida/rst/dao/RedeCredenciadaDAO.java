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

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.RedeCredenciadaFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.RedeCredenciada;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class RedeCredenciadaDAO extends BaseDAO<RedeCredenciada, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedeCredenciadaDAO.class);

	@Inject
	public RedeCredenciadaDAO(EntityManager em) {
		super(em, RedeCredenciada.class);
	}

	public RedeCredenciada pesquisarPorId(Long id, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando Parceiro por Id");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select rede from RedeCredenciada rede ");
		jpql.append(" left join fetch rede.porteEmpresa porteEmpresa ");
		jpql.append(" left join fetch rede.tipoEmpresa tipoEmpresa ");
		jpql.append(" left join fetch rede.segmento segmento ");
		if (segurancaFilter.temIdsDepRegional()) {
			jpql.append("left join rede.redeCredenciadaProdutoServico redeCredenciadaProdutoServico ");
			jpql.append("left join redeCredenciadaProdutoServico.produtoServico produtoServico ");
			jpql.append(
					"left join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos ");
			jpql.append("left join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional ");
		}
		
		jpql.append(" where rede.id = :id");

		if (segurancaFilter.temIdsDepRegional()) {
			jpql.append("  and departamentoRegional.id IN (:idsDepRegional) ");
		}
		
		TypedQuery<RedeCredenciada> query = criarConsultaPorTipo(jpql.toString());
		
		query.setParameter("id", id);
		
		if (segurancaFilter.temIdsDepRegional()) {
			query.setParameter("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}

		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<RedeCredenciada> pesquisarPaginado(RedeCredenciadaFilter redeCredenciadaFilter,
			DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Rede Credenciada por filtro");

		ListaPaginada<RedeCredenciada> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, redeCredenciadaFilter, false, segurancaFilter);

		TypedQuery<RedeCredenciada> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(redeCredenciadaFilter, segurancaFilter));

		if (redeCredenciadaFilter != null) {
			query.setFirstResult(
					(redeCredenciadaFilter.getPagina() - 1) * redeCredenciadaFilter.getQuantidadeRegistro());
			query.setMaxResults(redeCredenciadaFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private long getCountQueryPaginado(RedeCredenciadaFilter redeCredenciadaFilter, DadosFilter segurancaFilter) {

		Map<String, Object> parametros = Maps.newHashMap();

		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, redeCredenciadaFilter, true, segurancaFilter);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);

	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			RedeCredenciadaFilter redeCredenciadaFilter, boolean count, DadosFilter segurancaFilter) {
		if (count) {
			jpql.append("select count(rede.id) from RedeCredenciada rede ");
			jpql.append(" left join rede.segmento segmento ");
		} else {
			jpql.append("select distinct rede from RedeCredenciada rede ");
			jpql.append(" left join fetch rede.segmento segmento ");
		}

		if (segurancaFilter.temIdsDepRegional()) {
			jpql.append("left join rede.redeCredenciadaProdutoServico redeCredenciadaProdutoServico ");
			jpql.append("left join redeCredenciadaProdutoServico.produtoServico produtoServico ");
			jpql.append(
					"left join produtoServico.departamentoRegionalProdutoServicos departamentoRegionalProdutoServicos ");
			jpql.append("left join departamentoRegionalProdutoServicos.departamentoRegional departamentoRegional ");
			jpql.append(" left join departamentoRegional.unidadeAtendimentoTrabalhador uat ");
			jpql.append(" left join uat.empresaUats empresaUat ");
			jpql.append(" left join empresaUat.empresa empresa ");
			jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhadores ");
			jpql.append("left join empresaTrabalhadores.trabalhador trabalhador ");
		}

		if (redeCredenciadaFilter != null) {
			boolean cnpj = StringUtils.isNotEmpty(redeCredenciadaFilter.getCnpj());
			boolean razaoSocialNome = StringUtils.isNotEmpty(redeCredenciadaFilter.getRazaoSocial());
			boolean status = StringUtils.isNotBlank(redeCredenciadaFilter.getSituacao());
			boolean segmento = redeCredenciadaFilter.getSegmento() != null && redeCredenciadaFilter.getSegmento() > 0;
			jpql.append(" where ");
			if (cnpj) {
				jpql.append(" rede.numeroCnpj = :numeroCnpj ");
				parametros.put("numeroCnpj", redeCredenciadaFilter.getCnpj());
			}
			if (razaoSocialNome) {
				if (cnpj) {
					jpql.append(" and ");
				}
				jpql.append(" UPPER(rede.razaoSocial) like :razaoSocial escape :sc  ");
				parametros.put("sc", "\\");
				parametros.put("razaoSocial", "%" + redeCredenciadaFilter.getRazaoSocial().replace("%", "\\%").toUpperCase() + "%");
			}
			if (segmento) {
				if (cnpj || razaoSocialNome) {
					jpql.append(" and ");
				}

				jpql.append(" segmento.id = :idSegmento  ");
				parametros.put("idSegmento", redeCredenciadaFilter.getSegmento());
			}
			if (status) {
				if (cnpj || razaoSocialNome || segmento) {
					jpql.append(" and ");
				}

				if (Situacao.ATIVO.getCodigo().equals(redeCredenciadaFilter.getSituacao())) {
					jpql.append(" rede.dataDesligamento ").append(Situacao.ATIVO.getQuery());
				} else if (Situacao.INATIVO.getCodigo().equals(redeCredenciadaFilter.getSituacao())) {
					jpql.append(" rede.dataDesligamento ").append(Situacao.INATIVO.getQuery());
				}
			}

			if ((cnpj || razaoSocialNome || segmento || status) && (segurancaFilter.temIdsDepRegional())) {

				jpql.append("  and departamentoRegional.id IN (:idsDepRegional) ");
				parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());

			}

			if (!count) {
				jpql.append(" order by rede.numeroCnpj ");
			}

		}
	}

	public RedeCredenciada pesquisarPorCNPJ(String cnpj) {
		LOGGER.debug("Pesquisando Rede Credenciada por CNPJ");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select rede from RedeCredenciada rede where rede.numeroCnpj = :numeroCnpj");

		TypedQuery<RedeCredenciada> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("numeroCnpj", cnpj);

		return DAOUtil.getSingleResult(query);
	}
}
