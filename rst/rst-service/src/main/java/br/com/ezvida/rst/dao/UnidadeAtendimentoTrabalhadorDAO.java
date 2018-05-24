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
import br.com.ezvida.rst.dao.filter.EnderecoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UnidAtendTrabalhadorFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class UnidadeAtendimentoTrabalhadorDAO extends BaseDAO<UnidadeAtendimentoTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeAtendimentoTrabalhadorDAO.class);

	@Inject
	public UnidadeAtendimentoTrabalhadorDAO(EntityManager em) {
		super(em, UnidadeAtendimentoTrabalhador.class, "razaoSocial");
	}

	public List<UnidadeAtendimentoTrabalhador> pesquisarTodos() {
		LOGGER.debug("Pesquisando todos Unidade de Atendimento ao Trabalhador");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select uat from UnidadeAtendimentoTrabalhador uat ");
		jpql.append(" order by uat.razaoSocial ");
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		return query.getResultList();
	}

	public UnidadeAtendimentoTrabalhador pesquisarPorId(Long id, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando todos Unidade de Atendimento ao Trabalhador por Id");
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		jpql.append("select uat from UnidadeAtendimentoTrabalhador uat ");
		montarJoinPesquisarPorId(segurancaFilter, jpql);

		jpql.append(" where uat.id = :id");
		
		if (segurancaFilter != null) {
			montarFiltroPesquisarPorId(id, segurancaFilter, jpql, parametros);
		}
		
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void montarFiltroPesquisarPorId(Long id, DadosFilter segurancaFilter, StringBuilder jpql,
			Map<String, Object> parametros) {
		if (segurancaFilter.temIdsEmpresa()) {
			jpql.append(" and empresa.id IN (:idsEmpresa) ");
			parametros.put("idsEmpresa", segurancaFilter.getIdsEmpresa());
		}

		if (segurancaFilter.temIdsDepRegional()) {
			if (id != null || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}

			jpql.append(" departamentoRegional.id IN (:idsDepRegional) ");
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

	private void montarJoinPesquisarPorId(DadosFilter segurancaFilter, StringBuilder jpql) {
		jpql.append("left join fetch uat.departamentoRegional departamentoRegional ");

		if (segurancaFilter != null && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa())) {
			jpql.append("left join uat.empresaUats empresaUat ");
			jpql.append("left join empresaUat.empresa empresa ");

			if (segurancaFilter.temIdsTrabalhador()) {
				jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
				jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
			}
		}
	}

	public ListaPaginada<UnidadeAtendimentoTrabalhador> pesquisarPaginado(
			UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Unidade de Atendimento ao Trabalhador por filtro");
		ListaPaginada<UnidadeAtendimentoTrabalhador> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, unidAtendTrabalhadorFilter, false, segurancaFilter);
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		listaPaginada.setQuantidade(getCountQueryPaginado(unidAtendTrabalhadorFilter, segurancaFilter));

		if (unidAtendTrabalhadorFilter != null) {
			query.setFirstResult(
					(unidAtendTrabalhadorFilter.getPagina() - 1) * unidAtendTrabalhadorFilter.getQuantidadeRegistro());
			query.setMaxResults(unidAtendTrabalhadorFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private long getCountQueryPaginado(UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter,
			DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, unidAtendTrabalhadorFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter, boolean count, DadosFilter segurancaFilter) {

		boolean cnpj = false;
		boolean razaoSocial = false;
		boolean depRegional = false;
		boolean status = false;

		montarJoinPaginado(jpql, count, segurancaFilter);

		if (unidAtendTrabalhadorFilter != null) {
			jpql.append(" where ");

			cnpj = StringUtils.isNotEmpty(unidAtendTrabalhadorFilter.getCnpj());
			razaoSocial = StringUtils.isNotEmpty(unidAtendTrabalhadorFilter.getRazaoSocial());
			depRegional = unidAtendTrabalhadorFilter.getIdDepRegional() != null
					&& unidAtendTrabalhadorFilter.getIdDepRegional().intValue() > 0;
			status = StringUtils.isNotBlank(unidAtendTrabalhadorFilter.getStatusCat());

			montarFiltroPaginado(jpql, parametros, unidAtendTrabalhadorFilter, cnpj, razaoSocial, depRegional);

			addAnd(jpql, cnpj, razaoSocial, depRegional, status);

			montarFiltroSituacaoPaginado(jpql, unidAtendTrabalhadorFilter);
		}

		addFiltroIds(jpql, parametros, segurancaFilter, cnpj, razaoSocial, depRegional, status);

		if (!count) {
			jpql.append(" order by uat.razaoSocial ");
		}
	}

	private void addFiltroIds(StringBuilder jpql, Map<String, Object> parametros, DadosFilter segurancaFilter,
			boolean cnpj, boolean razaoSocial, boolean depRegional, boolean status) {
		if (segurancaFilter != null) {
			if ((cnpj || razaoSocial || depRegional || status)
					&& (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) && !segurancaFilter.isAdministrador()) {
				jpql.append(" and ");
				montarFiltroIdsPaginado(jpql, parametros, segurancaFilter);
			}
		}
	}

	private void montarFiltroIdsPaginado(StringBuilder jpql, Map<String, Object> parametros,
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

		if (segurancaFilter.temIdsTrabalhador()) {

			if (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa()) {
				jpql.append(" and ");
			}

			jpql.append(" trabalhador.id IN (:idsTrabalhador) ");
			parametros.put("idsTrabalhador", segurancaFilter.getIdsTrabalhador());
		}
	}

	private void montarFiltroSituacaoPaginado(StringBuilder jpql,
			UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter) {
		if (Situacao.ATIVO.getCodigo().equals(unidAtendTrabalhadorFilter.getStatusCat())) {
			jpql.append(" uat.dataDesativacao ").append(Situacao.ATIVO.getQuery());
		} else if (Situacao.INATIVO.getCodigo().equals(unidAtendTrabalhadorFilter.getStatusCat())) {
			jpql.append(" uat.dataDesativacao ").append(Situacao.INATIVO.getQuery());
		}
	}

	private void addAnd(StringBuilder jpql, boolean cnpj, boolean razaoSocial, boolean depRegional, boolean status) {
		if (status) {
			if (cnpj || razaoSocial || depRegional) {
				jpql.append(" and");
			}
		}
	}

	private void montarFiltroPaginado(StringBuilder jpql, Map<String, Object> parametros,
			UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter, boolean cnpj, boolean razaoSocial,
			boolean depRegional) {
		if (cnpj) {
			jpql.append(" uat.cnpj like :cnpj escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("cnpj", "%" + unidAtendTrabalhadorFilter.getCnpj().replace("%", "\\%") + "%");
		}

		if (razaoSocial) {
			if (cnpj) {
				jpql.append(" and");
			}
			jpql.append(" UPPER(uat.razaoSocial) like :razaoSocial escape :sc");
			parametros.put("sc", "\\");
			parametros.put("razaoSocial", "%" + unidAtendTrabalhadorFilter.getRazaoSocial().replace("%", "\\%").toUpperCase() + "%");
		}

		if (depRegional) {
			if (cnpj || razaoSocial) {
				jpql.append(" and");
			}
			jpql.append(" uat.departamentoRegional.id = :idDepRegional");
			parametros.put("idDepRegional", unidAtendTrabalhadorFilter.getIdDepRegional());
		}
	}

	private void montarJoinPaginado(StringBuilder jpql, boolean count, DadosFilter segurancaFilter) {
		if (count) {
			jpql.append("select count(distinct uat.id) from UnidadeAtendimentoTrabalhador uat ");
			jpql.append("left join uat.empresaUats empresaUat ");
			jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
		} else {
			jpql.append("select DISTINCT uat from UnidadeAtendimentoTrabalhador uat ");
			jpql.append("left join fetch uat.endereco endUat ");
			jpql.append("left join uat.empresaUats empresaUat ");
			jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
			jpql.append("left join fetch endUat.endereco endereco ");
			jpql.append("left join fetch endereco.municipio municipio ");
		}

		if (segurancaFilter != null && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa()) && !segurancaFilter.isAdministrador()) {
			jpql.append("left join empresaUat.empresa empresa ");

			if (segurancaFilter.temIdsTrabalhador() && !segurancaFilter.isAdministrador()) {
				jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
				jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
			}
		}
	}

	public UnidadeAtendimentoTrabalhador pesquisarPorCNPJ(String cnpj) {
		LOGGER.debug("Pesquisando Unidade de Atendimento ao Trabalhador por CNPJ");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select uat from UnidadeAtendimentoTrabalhador uat where uat.cnpj = :cnpj");
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cnpj", cnpj);
		return DAOUtil.getSingleResult(query);
	}

	public List<UnidadeAtendimentoTrabalhador> pesquisarPorDepartamento(Long departamentoId) {
		LOGGER.debug("Pesquisando Unidade de Atendimento ao Trabalhador Ativas por Departamento");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select uat from UnidadeAtendimentoTrabalhador uat ");
		jpql.append("left join fetch uat.departamentoRegional departamentoRegional ");
		jpql.append("where uat.departamentoRegional.id = :departamentoRegional ");
		jpql.append("and uat.dataDesativacao ").append(Situacao.ATIVO.getQuery());
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("departamentoRegional", departamentoId);
		return query.getResultList();
	}
	
	public List<UnidadeAtendimentoTrabalhador> pesquisarPorEndereco(EnderecoFilter enderecoFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando todos Unidade de Atendimento ao Trabalhador por Id");
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		boolean idEstado = false;
		boolean idMunicipio = false;
		boolean bairro = false;
		
		montarJoinPesquisarPorEndereco(segurancaFilter, jpql);

		if (enderecoFilter != null) {
			jpql.append(" where ");

			idEstado = enderecoFilter.getIdEstado() != null
					&& enderecoFilter.getIdEstado().intValue() > 0;
			idMunicipio = enderecoFilter.getIdMunicipio() != null
					&& enderecoFilter.getIdMunicipio().intValue() > 0;
			bairro = StringUtils.isNotEmpty(enderecoFilter.getBairro());
					
			montarFiltroPesquisarPorEndereco(enderecoFilter, jpql, parametros, idEstado, idMunicipio, bairro);

		}

		addFiltroDepRegEmpPesquisarPorEndereco(segurancaFilter, jpql, parametros, idEstado, idMunicipio, bairro);
		
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return query.getResultList();
	}

	private void addFiltroDepRegEmpPesquisarPorEndereco(DadosFilter segurancaFilter, StringBuilder jpql,
			Map<String, Object> parametros, boolean idEstado, boolean idMunicipio, boolean bairro) {
		if (segurancaFilter != null) {
			if ((idEstado || idMunicipio || bairro)
					&& (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa())) {
				jpql.append(" and ");

				montarFiltroIdsPaginado(jpql, parametros, segurancaFilter);
			}
		}
	}

	private void montarFiltroPesquisarPorEndereco(EnderecoFilter enderecoFilter, StringBuilder jpql,
			Map<String, Object> parametros, boolean idEstado, boolean idMunicipio, boolean bairro) {
		if (idEstado) {
			jpql.append(" estado.id = :idEstado");
			parametros.put("idEstado", enderecoFilter.getIdEstado());
		}

		if (idMunicipio) {
			if (idEstado) {
				jpql.append(" and");
			}
			jpql.append(" municipio.id = :idMunicipio");
			parametros.put("idMunicipio", enderecoFilter.getIdMunicipio());
		}

		if (bairro) {
			if (idEstado || idMunicipio) {
				jpql.append(" and");
			}
			jpql.append(" UPPER(endereco.bairro) like :bairro escape :sc");
			parametros.put("sc", "\\");
			parametros.put("bairro", "%" + enderecoFilter.getBairro().replace("%", "\\%").toUpperCase() + "%");
		}
	}

	private void montarJoinPesquisarPorEndereco(DadosFilter segurancaFilter, StringBuilder jpql) {
		jpql.append("select DISTINCT uat from UnidadeAtendimentoTrabalhador uat ");
		jpql.append("left join fetch uat.endereco endUat ");
		jpql.append("left join uat.empresaUats empresaUat ");
		jpql.append("left join uat.departamentoRegional departamentoRegionalUat ");
		jpql.append("left join fetch endUat.endereco endereco ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");

		if (segurancaFilter != null && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa())) {
			jpql.append("left join uat.empresaUats empresaUat ");
			jpql.append("left join empresaUat.empresa empresa ");

			if (segurancaFilter.temIdsTrabalhador()) {
				jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
				jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
			}
		}
	}

	public List<UnidadeAtendimentoTrabalhador> buscarPorEmpresaEAtivas(Long id) {
		LOGGER.debug("Pesquisando Unidade de Atendimento ao Trabalhador Ativas por Empresa");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select new UnidadeAtendimentoTrabalhador (uat.cnpj, uat.razaoSocial, ");
		jpql.append("  departamentoRegionalUat.cnpj, departamentoRegionalUat.razaoSocial, departamentoRegionalUat.siglaDR, estado.siglaUF) ");
		jpql.append(" from EmpresaUnidadeAtendimentoTrabalhador empresaUnidadeAtendimentoTrabalhador ");
		jpql.append("	left join empresaUnidadeAtendimentoTrabalhador.unidadeAtendimentoTrabalhador uat ");
		jpql.append("	left join uat.departamentoRegional departamentoRegionalUat ");
		jpql.append("	left join departamentoRegionalUat.listaEndDepRegional enderecos ");
		jpql.append("	left join enderecos.endereco endereco ");
		jpql.append("	left join endereco.municipio municipio ");
		jpql.append("	left join municipio.estado estado ");
		jpql.append("	where empresaUnidadeAtendimentoTrabalhador.empresa.id = :id ");
		jpql.append("		and empresaUnidadeAtendimentoTrabalhador.dataExclusao is null ");

		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList();
	}
}