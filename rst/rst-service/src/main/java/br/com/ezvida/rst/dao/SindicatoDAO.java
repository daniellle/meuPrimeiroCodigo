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
import br.com.ezvida.rst.dao.filter.SindicatoFilter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.model.Sindicato;
import br.com.ezvida.rst.utils.CollectionUtil;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class SindicatoDAO extends BaseDAO<Sindicato, Long> {

	private static final String LEFT_JOIN_EMPRESA_SINDICATO_EMPRESA_EMPRESA = "left join empresaSindicato.empresa empresa ";
	private static final String LEFT_JOIN_C_EMPRESA_SINDICATO_EMPRESA_SINDICATO = "left join c.empresaSindicato empresaSindicato ";
	private static final String SELECT_DISTINCT_C_FROM_SINDICATO_C = "select distinct c from Sindicato c ";
	private static final String AND = " and ";
	private static final Logger LOGGER = LoggerFactory.getLogger(SindicatoDAO.class);

	@Inject
	public SindicatoDAO(EntityManager em) {
		super(em, Sindicato.class, "razaoSocial");
	}

	public Sindicato pesquisarPorId(Long id, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando todos os Sindicatos por Id");
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		jpql.append(SELECT_DISTINCT_C_FROM_SINDICATO_C);
		jpql.append(LEFT_JOIN_C_EMPRESA_SINDICATO_EMPRESA_SINDICATO);
		jpql.append(LEFT_JOIN_EMPRESA_SINDICATO_EMPRESA_EMPRESA);
		jpql.append(" left join empresa.empresaUats empresaUats ");
		jpql.append(" left join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
		jpql.append(" left join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
		jpql.append(" where c.id = :id");
		
		parametros.put("id", id);
		if (segurancaFilter.temIdsDepRegional()) {
			jpql.append(" and depRegional.id IN (:idsDepRegional) ");
			parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
		}
		
		TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<Sindicato> pesquisarPaginado(SindicatoFilter sindicatoFilter, DadosFilter segurancaFilter) {
		LOGGER.debug("Pesquisando paginado Sindicatos por filtro");

		ListaPaginada<Sindicato> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, sindicatoFilter, false, segurancaFilter);
		TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		listaPaginada.setQuantidade(getCountQueryPaginado(sindicatoFilter, segurancaFilter));

		query.setFirstResult((sindicatoFilter.getPagina() - 1) * sindicatoFilter.getQuantidadeRegistro());
		query.setMaxResults(sindicatoFilter.getQuantidadeRegistro());

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}
	
	public long getCountQueryPaginado(SindicatoFilter sindicatoFilter, DadosFilter segurancaFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros,  sindicatoFilter, true, segurancaFilter);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}
	
	
	public void getQueryPaginado(StringBuilder jpql, Map <String, Object> parametros, 
			SindicatoFilter sindicatoFilter, boolean count, DadosFilter segurancaFilter) {
		
		boolean cnpj = false;
		boolean razaoSocial = false;
		boolean nomeFantasia = false;
		boolean situacao = false;
		boolean ids = false;
		
		if(count) {
			jpql.append("select count(distinct c.id) from Sindicato c ");
		} else { 
			jpql.append(SELECT_DISTINCT_C_FROM_SINDICATO_C);
		}
		
		if (segurancaFilter != null &&  segurancaFilter.temIdsDepRegional()) {
			jpql.append(LEFT_JOIN_C_EMPRESA_SINDICATO_EMPRESA_SINDICATO);
			jpql.append(LEFT_JOIN_EMPRESA_SINDICATO_EMPRESA_EMPRESA);
		}
		
		if (sindicatoFilter != null) {
			
			cnpj = StringUtils.isNotEmpty(sindicatoFilter.getCnpj());
			razaoSocial= StringUtils.isNotEmpty(sindicatoFilter.getRazaoSocial());
			nomeFantasia = StringUtils.isNotEmpty(sindicatoFilter.getNomeFantasia());
			situacao = StringUtils.isNotBlank(sindicatoFilter.getSituacao());
			ids = StringUtils.isNotBlank(sindicatoFilter.getIds());
			
			if(segurancaFilter.temIdsDepRegional()) {
				jpql.append(" left join empresa.empresaUats empresaUats ");
				jpql.append(" left join empresaUats.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
				jpql.append(" left join unidadeAtendimentoTrabalhador.departamentoRegional depRegional ");
			}
			
			if (cnpj || razaoSocial || nomeFantasia || situacao || ids) {
				jpql.append(" where ");
			}
			
			if (cnpj){
				jpql.append(" c.cnpj = :cnpj ");
				parametros.put("cnpj", sindicatoFilter.getCnpj());
			}
			if (razaoSocial){
				if(cnpj) {
					jpql.append(AND);
				}
				jpql.append(" UPPER(c.razaoSocial) like :razaoSocial escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("razaoSocial", "%" + sindicatoFilter.getRazaoSocial().replace("%", "\\%").toUpperCase() + "%");
				
			}
			if (nomeFantasia){
				if(cnpj || razaoSocial) {
					jpql.append(AND);
				}
				jpql.append(" UPPER(c.nomeFantasia) like :nomeFantasia escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("nomeFantasia", "%" + sindicatoFilter.getNomeFantasia().replace("%", "\\%").toUpperCase() + "%");
			}
			if (situacao) {
				if(cnpj || razaoSocial || nomeFantasia) {
					jpql.append(AND);
				}
				if (Situacao.ATIVO.getCodigo().equals(sindicatoFilter.getSituacao())) {
					jpql.append(" c.dataDesativacao ").append(Situacao.ATIVO.getQuery());
				} else if (Situacao.INATIVO.getCodigo().equals(sindicatoFilter.getSituacao())) {
					jpql.append(" c.dataDesativacao ").append(Situacao.INATIVO.getQuery());
				}
			}

			if (ids) {
				if (cnpj || razaoSocial || nomeFantasia || situacao) {
					jpql.append(AND);
				}

				jpql.append(" c.id not in (:ids) ");
				parametros.put("ids", CollectionUtil.getIds(sindicatoFilter.getIds()));
			}
		}
		
		if(segurancaFilter != null && sindicatoFilter != null && sindicatoFilter.isAplicarDadosFilter()) {
			
			boolean hasFilters = cnpj || razaoSocial || nomeFantasia || situacao || ids;
			
			if (hasFilters && (segurancaFilter.temIdsDepRegional())) {
				jpql.append(AND);
				jpql.append(" depRegional.id IN (:idsDepRegional) ");
				parametros.put("idsDepRegional", segurancaFilter.getIdsDepartamentoRegional());
			}
		}
		
		if(!count) {
			jpql.append(" order by c.razaoSocial ");
		}
		
	}
	
	
	public List<Sindicato> listarTodos() {
		LOGGER.debug("Listando todos os sindicatos");
		
		StringBuilder jpql = new StringBuilder();
		montarSelecetComJoins(jpql);
		jpql.append(" order by c.razaoSocial ");
		TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());

		return query.getResultList();
	}

	private void montarSelecetComJoins(StringBuilder jpql) {
		jpql.append(SELECT_DISTINCT_C_FROM_SINDICATO_C);
		jpql.append("left join fetch c.endereco endSind ");
		jpql.append("left join fetch endSind.endereco e ");
		jpql.append("left join fetch e.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
		jpql.append("left join fetch c.email emSind ");
		jpql.append("left join fetch emSind.email email ");
		jpql.append("left join fetch c.telefone telSind ");
		jpql.append("left join fetch telSind.telefone telefone ");
	}

	public Sindicato pesquisarPorCNPJ(String cnpj) {
		LOGGER.debug("Pesquisando Sindicato por CNPJ");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select c from Sindicato c where c.cnpj = :cnpj");
		TypedQuery<Sindicato> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cnpj", cnpj);
		return DAOUtil.getSingleResult(query);
	}
}
