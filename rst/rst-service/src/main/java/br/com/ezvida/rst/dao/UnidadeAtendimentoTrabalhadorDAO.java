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
		jpql.append("left join fetch uat.departamentoRegional departamentoRegional ");

		if (segurancaFilter != null && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa())) {
			jpql.append("left join uat.empresaUats empresaUat ");
			jpql.append("left join empresaUat.empresa empresa ");

			if (segurancaFilter.temIdsTrabalhador()) {
				jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
				jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
			}
		}

		jpql.append(" where uat.id = :id");
		
		if (segurancaFilter != null) {
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
		
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
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

		if (segurancaFilter != null && (segurancaFilter.temIdsTrabalhador() || segurancaFilter.temIdsEmpresa())) {
			jpql.append("left join empresaUat.empresa empresa ");

			if (segurancaFilter.temIdsTrabalhador()) {
				jpql.append("left join empresa.empresaTrabalhadores empresaTrabalhador ");
				jpql.append("left join empresaTrabalhador.trabalhador trabalhador ");
			}
		}

		if (unidAtendTrabalhadorFilter != null) {
			jpql.append(" where ");

			cnpj = StringUtils.isNotEmpty(unidAtendTrabalhadorFilter.getCnpj());
			razaoSocial = StringUtils.isNotEmpty(unidAtendTrabalhadorFilter.getRazaoSocial());
			depRegional = unidAtendTrabalhadorFilter.getIdDepRegional() != null
					&& unidAtendTrabalhadorFilter.getIdDepRegional().intValue() > 0;
			status = StringUtils.isNotBlank(unidAtendTrabalhadorFilter.getStatusCat());

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

			if (status) {
				if (cnpj || razaoSocial || depRegional) {
					jpql.append(" and");
				}
			}

			if (Situacao.ATIVO.getCodigo().equals(unidAtendTrabalhadorFilter.getStatusCat())) {
				jpql.append(" uat.dataDesativacao ").append(Situacao.ATIVO.getQuery());
			} else if (Situacao.INATIVO.getCodigo().equals(unidAtendTrabalhadorFilter.getStatusCat())) {
				jpql.append(" uat.dataDesativacao ").append(Situacao.INATIVO.getQuery());
			}
		}

		if (segurancaFilter != null) {
			if ((cnpj || razaoSocial || depRegional || status)
					&& (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa())) {
				jpql.append(" and ");

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
		}

		if (!count) {
			jpql.append(" order by uat.razaoSocial ");
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

		if (enderecoFilter != null) {
			jpql.append(" where ");

			idEstado = enderecoFilter.getIdEstado() != null
					&& enderecoFilter.getIdEstado().intValue() > 0;
			idMunicipio = enderecoFilter.getIdMunicipio() != null
					&& enderecoFilter.getIdMunicipio().intValue() > 0;
			bairro = StringUtils.isNotEmpty(enderecoFilter.getBairro());
					
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

		if (segurancaFilter != null) {
			if ((idEstado || idMunicipio || bairro)
					&& (segurancaFilter.temIdsDepRegional() || segurancaFilter.temIdsEmpresa())) {
				jpql.append(" and ");

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
		}
		
		TypedQuery<UnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return query.getResultList();
	}
}