package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.DepartamentoRegionalFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.DepartamentoRegionalProdutoServico;
import fw.core.jpa.DAOUtil;

public class DepartamentoRegionalProdutoServicoDAO extends BaseRstDAO<DepartamentoRegionalProdutoServico, Long> {

	
	@Inject
	public DepartamentoRegionalProdutoServicoDAO(EntityManager em) {
		super(em, DepartamentoRegionalProdutoServico.class);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoRegionalProdutoServicoDAO.class);

	
	private void queryPorDepartamentoRegional(StringBuilder jpql, Map<String, Object> parametros, Long id, boolean count) {
		
		if (!count) {
			jpql.append("select departamentoRegionalProdutoServico from DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico ");
			jpql.append(" left join fetch departamentoRegionalProdutoServico.departamentoRegional departamentoRegional ");
			jpql.append(" left join fetch departamentoRegionalProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join fetch produtoServico.linha linha ");
		} else {

			jpql.append("select count(departamentoRegionalProdutoServico.id) from DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico ");
			jpql.append(" left join departamentoRegionalProdutoServico.departamentoRegional departamentoRegional ");
			jpql.append(" left join departamentoRegionalProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join produtoServico.linha linha ");
			
		}
		
		jpql.append(" where departamentoRegionalProdutoServico.dataExclusao is null ");
		
		
		if(id != null) {
			jpql.append(" and departamentoRegional.id = :id ");
			parametros.put("id", id);
		}
		
		if(!count) {
			jpql.append(" Order By linha.descricao ");
		}
		
	}
	
	private Long getCountQueryPaginado(Long idDepartamentoRegional) {
		
		Map<String, Object> parametros = Maps.newHashMap();
		
		StringBuilder jpql = new StringBuilder();
		
		queryPorDepartamentoRegional(jpql, parametros, idDepartamentoRegional, true);
		
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		
		return DAOUtil.getSingleResult(query);
		
	}
	
	
	public ListaPaginada<DepartamentoRegionalProdutoServico> retornarPorDepartamentoRegional(DepartamentoRegionalFilter departamentoRegionalFilter){
		LOGGER.debug("Pesquisando Departamento Regional produtos e serviços por Departamento Regional");
		
		ListaPaginada<DepartamentoRegionalProdutoServico> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		
		queryPorDepartamentoRegional(jpql, parametros, departamentoRegionalFilter.getId(), false);
		TypedQuery<DepartamentoRegionalProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		
		listaPaginada.setQuantidade(getCountQueryPaginado(departamentoRegionalFilter.getId()));
		
		query.setFirstResult((departamentoRegionalFilter.getPagina() - 1) * departamentoRegionalFilter.getQuantidadeRegistro());
		query.setMaxResults(departamentoRegionalFilter.getQuantidadeRegistro());
		
		listaPaginada.setList(query.getResultList());
		
		return listaPaginada;
	}

	public DepartamentoRegionalProdutoServico verificandoExistenciaProdutoServico(Long idDepartamentoRegional, Long idProdutosServico) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select departamentoRegionalProdutoServico from DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico ");
		jpql.append(" left join fetch departamentoRegionalProdutoServico.departamentoRegional departamentoRegional ");
		jpql.append(" left join fetch departamentoRegionalProdutoServico.produtoServico produtoServico ");
		jpql.append(" left join fetch  produtoServico.linha linha ");
		
		jpql.append(" where departamentoRegionalProdutoServico.dataExclusao is null ");
		jpql.append(" and produtoServico.id = :idProdutosServico  ");
		jpql.append(" and departamentoRegional.id = :id  ");
		
		TypedQuery<DepartamentoRegionalProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idDepartamentoRegional);
		query.setParameter("idProdutosServico" ,idProdutosServico);
		
		return DAOUtil.getSingleResult(query);
	}
	
	@Override
	public DepartamentoRegionalProdutoServico pesquisarPorId(Long id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select departamentoRegionalProdutoServico from DepartamentoRegionalProdutoServico departamentoRegionalProdutoServico ");
		jpql.append(" left join fetch departamentoRegionalProdutoServico.departamentoRegional departamentoRegional ");
		jpql.append(" left join fetch departamentoRegionalProdutoServico.produtoServico produtoServico ");
		jpql.append(" left join fetch departamentoRegional.listaTelDepRegional listaTelDepRegional ");
		jpql.append(" left join fetch departamentoRegional.listaEmailDepRegional listaEmailDepRegional ");
		jpql.append(" left join fetch departamentoRegional.listaEndDepRegional listaEndDepRegional ");
		jpql.append(" left join fetch  produtoServico.linha linha ");
		
		jpql.append(" where departamentoRegionalProdutoServico.id = :id ");
		
		TypedQuery<DepartamentoRegionalProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", id);
		
		return DAOUtil.getSingleResult(query);
	}

}
