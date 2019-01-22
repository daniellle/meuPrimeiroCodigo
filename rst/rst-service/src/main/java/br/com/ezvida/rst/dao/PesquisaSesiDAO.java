package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.ezvida.rst.dao.filter.PesquisaSesiFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class PesquisaSesiDAO extends BaseDAO<UnidadeAtendimentoTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PesquisaSesiDAO.class);

	@Inject
	public PesquisaSesiDAO(EntityManager em) {
		super(em, UnidadeAtendimentoTrabalhador.class, "razaoSocial");
	}

	public List<UnidadeAtendimentoTrabalhador> buscarUnidadesSesi(DadosFilter segurancaFilter) {
		LOGGER.debug("Buscando Unidades Sesi");

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		jpql.append("select DISTINCT uat from UnidadeAtendimentoTrabalhador uat ");
		jpql.append("left join fetch uat.endereco endUat ");
		jpql.append("left join uat.empresaUats empresaUat ");
		jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
		jpql.append("left join fetch endUat.endereco endereco ");
		jpql.append("left join fetch endereco.municipio municipio ");

		if (segurancaFilter != null && !segurancaFilter.isAdministrador() && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa())) {
			jpql.append("left join empresaUat.empresa empresa ");

			if (segurancaFilter.temIdsTrabalhador()) {
				jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
				jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
			}
		}
		jpql.append(" where ");
		jpql.append(" uat.dataDesativacao ").append(Situacao.ATIVO.getQuery());

		if (segurancaFilter != null && !segurancaFilter.isAdministrador()) {
			if (segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
				jpql.append(" empresa.id IN (:idsEmpresa) ");
				parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
			}

			if (segurancaFilter.temIdsDepRegional()) {
				jpql.append(" and ");
				jpql.append(" uat.departamentoRegional.id IN (:idsDepRegional) ");
				parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
			}

			if (segurancaFilter.isGetorUnidadeSESI()) {
				jpql.append(" and ");
				jpql.append(" uat.id IN (:idsUnidadeSESI) ");
				parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
			}

			if (segurancaFilter.temIdsTrabalhador()) {

				jpql.append(" and ");
				jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
				parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
			}
		}

		jpql.append(" order by uat.razaoSocial ");

		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return query.getResultList();
	}

	public List<UnidadeAtendimentoTrabalhador> listarUnidadesSesi(){
		LOGGER.debug("Buscando Unidades Sesi");

		StringBuilder jpql = new StringBuilder();

		jpql.append("select DISTINCT uat from UnidadeAtendimentoTrabalhador uat ");
		jpql.append("left join fetch uat.endereco endUat ");
		jpql.append("left join uat.empresaUats empresaUat ");
		jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
		jpql.append("left join fetch endUat.endereco endereco ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append(" where ");
		jpql.append(" uat.dataDesativacao ").append(Situacao.ATIVO.getQuery());
		jpql.append(" order by uat.razaoSocial ");

		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());

		return query.getResultList();
	}
	
	public ListaPaginada<UnidadeAtendimentoTrabalhador> pesquisarPaginado(PesquisaSesiFilter pesquisaSesiFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Unidades Sesi Produto Serviço por filtro");

		ListaPaginada<UnidadeAtendimentoTrabalhador> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, pesquisaSesiFilter, false, segurancaFilter);
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(pesquisaSesiFilter, segurancaFilter));

		if (pesquisaSesiFilter != null) {
			query.setFirstResult((pesquisaSesiFilter.getPagina() - 1) * pesquisaSesiFilter.getQuantidadeRegistro());
			query.setMaxResults(pesquisaSesiFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}
	
	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			PesquisaSesiFilter pesquisaSesiFilter, boolean count, DadosFilter segurancaFilter) {

		boolean unidadeSesi = false;
		boolean estado = false;
		boolean municipio = false;
		boolean bairro = false;
		boolean linha = false;
		boolean produto = false;

		montarJoinPaginado(jpql, count, segurancaFilter);

		if (pesquisaSesiFilter != null) {
			jpql.append(" where ");

			unidadeSesi = pesquisaSesiFilter.getIdUnidadeSesi() != null && pesquisaSesiFilter.getIdUnidadeSesi().intValue() > 0;
			estado = pesquisaSesiFilter.getIdEstado() != null && pesquisaSesiFilter.getIdEstado().intValue() > 0;
			municipio = pesquisaSesiFilter.getIdMunicipio() != null && pesquisaSesiFilter.getIdMunicipio().intValue() > 0;
			bairro = StringUtils.isNotBlank(pesquisaSesiFilter.getBairro());
			linha = StringUtils.isNotBlank(pesquisaSesiFilter.getIdsLinha());
			produto = StringUtils.isNotBlank(pesquisaSesiFilter.getIdsProduto());
			
			if (unidadeSesi) {
				jpql.append(" uat.id = :idUat ");
				parametros.put("idUat", pesquisaSesiFilter.getIdUnidadeSesi());
			}
			
			montarFiltroPaginado(jpql, parametros, pesquisaSesiFilter, unidadeSesi, estado, municipio, bairro);
			
			montarFiltroLinhaPaginado(jpql, parametros, pesquisaSesiFilter, unidadeSesi, estado, municipio, bairro,
					linha);
			montarFiltroProduto(jpql, parametros, pesquisaSesiFilter, unidadeSesi, estado, municipio, bairro,
					linha, produto);
		}

		montarFiltroSegurancaFilterPaginado(jpql, parametros, segurancaFilter, unidadeSesi, estado, municipio, bairro,
				linha, produto);

		if (!count) {
			jpql.append(" order by uat.razaoSocial ");
		}
	}

	private void montarFiltroLinhaPaginado(StringBuilder jpql, Map<String, Object> parametros,
			PesquisaSesiFilter pesquisaSesiFilter, boolean unidadeSesi, boolean estado, boolean municipio,
			boolean bairro, boolean linha) {
		if (linha) {
			List<String> listLinhaString = Arrays.asList(pesquisaSesiFilter.getIdsLinha().split(","));
			List<Long> listLinhaLong = new ArrayList<Long>();
			listLinhaString.stream().forEach(n -> listLinhaLong.add(Long.parseLong(n)));
			
			if (unidadeSesi || estado || municipio || bairro) {
				jpql.append(" and");
			}
			jpql.append(" linha.id IN :idsLinha");
			parametros.put("idsLinha", listLinhaLong);
		}
	}

	private void montarFiltroProduto(StringBuilder jpql, Map<String, Object> parametros,
			PesquisaSesiFilter pesquisaSesiFilter, boolean unidadeSesi, boolean estado, boolean municipio,
			boolean bairro, boolean linha, boolean produto) {
		
		
		if (produto) {
			List<String> listProdutoString = Arrays.asList(pesquisaSesiFilter.getIdsProduto().split(","));
			List<Long> listProdutoLong = new ArrayList<Long>();
			listProdutoString.stream().forEach(n -> listProdutoLong.add(Long.parseLong(n)));
			
			if (unidadeSesi || estado || municipio || bairro || linha) {
				jpql.append(" and");
			}
			jpql.append(" produtoServico.id IN :idsProduto");
			parametros.put("idsProduto", listProdutoLong);
		}
	}

	private void montarFiltroSegurancaFilterPaginado(StringBuilder jpql, Map<String, Object> parametros,
			DadosFilter segurancaFilter, boolean unidadeSesi, boolean estado, boolean municipio, boolean bairro,
			boolean linha, boolean produto) {
		if (segurancaFilter != null) {
			if ((unidadeSesi || estado || municipio || bairro || linha || produto)
					&& (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) && !segurancaFilter.isAdministrador()) {
				jpql.append(" and ");
				montarFiltroSegurancaFilterIds(jpql, parametros, segurancaFilter);
			}
		}
	}

	private void montarFiltroSegurancaFilterIds(StringBuilder jpql, Map<String, Object> parametros,
			DadosFilter segurancaFilter) {
		if (segurancaFilter.temIdsEmpresa()) {
			jpql.append(" empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}

		if (segurancaFilter.temIdsEmpresa() && segurancaFilter.temIdsDepRegional()) {
			jpql.append(" and ");
		}

		if (segurancaFilter.temIdsDepRegional()) {
			jpql.append(" uat.departamentoRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}

		if (segurancaFilter.isGetorUnidadeSESI()) {
			if(segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()){
				jpql.append(" and ");
			}
			jpql.append(" uat.id IN (:idsUnidadeSESI) ");
			parametros.put("idsUnidadeSESI", segurancaFilter.getIdsUnidadeSESI());
		}

		if (segurancaFilter.temIdsTrabalhador()) {

			if (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa() || segurancaFilter.temIdsUnidadeSESI()) {
				jpql.append(" and ");
			}

			jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
			parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
		}
	}

	private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros,
			PesquisaSesiFilter pesquisaSesiFilter, boolean unidadeSesi, boolean estado, boolean municipio,
			boolean bairro) {
		if (estado) {
			if (unidadeSesi) {
				jpql.append(" and");
			}
			jpql.append(" estado.id = :idEstado ");
			parametros.put("idEstado", pesquisaSesiFilter.getIdEstado());
		}

		if (municipio) {
			if (unidadeSesi || estado) {
				jpql.append(" and");
			}
			jpql.append(" municipio.id = :idMunicipio");
			parametros.put("idMunicipio", pesquisaSesiFilter.getIdMunicipio());
		}

		if (bairro) {
			if (unidadeSesi || estado || municipio) {
				jpql.append(" and ");
			}
			jpql.append("UPPER(endereco.bairro) like :bairro escape :sc ");
			parametros.put("sc", "/");
			parametros.put("bairro", "%" + pesquisaSesiFilter.getBairro().replace("%", "/%").toUpperCase() + "%");
		}
	}

	private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter) {
		if (count) {
			jpql.append("select count(distinct uat.id) from UnidadeAtendimentoTrabalhador uat ");
			jpql.append("left join uat.uatProdutoServico uatProdutoServico ");
			jpql.append("left join uatProdutoServico.produtoServico produtoServico ");
			jpql.append("left join produtoServico.linha linha ");
			jpql.append("left join uat.telefone tel ");
			jpql.append("left join uat.endereco endUat ");
			jpql.append("left join endUat.endereco endereco ");
			jpql.append("left join endereco.municipio municipio ");
			jpql.append("left join municipio.estado estado ");
			jpql.append("left join uat.empresaUats empresaUat ");
			jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
		} else {
			jpql.append("select DISTINCT uat from UnidadeAtendimentoTrabalhador uat ");
			jpql.append("left join fetch uat.uatProdutoServico uatProdutoServico ");
			jpql.append("left join fetch uatProdutoServico.produtoServico produtoServico ");
			jpql.append("left join fetch produtoServico.linha linha ");
			jpql.append("left join fetch uat.telefone tel ");
			jpql.append("left join fetch uat.endereco endUat ");
			jpql.append("left join fetch endUat.endereco endereco ");
			jpql.append("left join fetch endereco.municipio municipio ");
			jpql.append("left join fetch municipio.estado estado ");
			jpql.append("left join uat.empresaUats empresaUat ");
			jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
		}

		if (segurancaFilter != null && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa()) && !segurancaFilter.isAdministrador()) {
			jpql.append("left join empresaUat.empresa empresa ");

			if (segurancaFilter.temIdsTrabalhador()) {
				jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
				jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
			}
		}
	}
	
	private long getCountQueryPaginado(PesquisaSesiFilter pesquisaSesiFilter,
			DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, pesquisaSesiFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}
	
}
