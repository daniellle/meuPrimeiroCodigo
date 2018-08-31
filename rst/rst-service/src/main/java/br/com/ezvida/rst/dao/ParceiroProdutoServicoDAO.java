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

import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.ParceiroFilter;
import br.com.ezvida.rst.model.ParceiroProdutoServico;
import fw.core.jpa.DAOUtil;

public class ParceiroProdutoServicoDAO extends BaseRstDAO<ParceiroProdutoServico, Long> {

	
	 @Inject
	public  ParceiroProdutoServicoDAO(EntityManager em) {
		super(em, ParceiroProdutoServico.class);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroProdutoServicoDAO.class);

	
	private void queryPorParceiro(StringBuilder jpql, Map<String, Object> parametros, Long id, boolean count) {
		
		if (!count) {
			jpql.append("select parceiroProdutoServico from ParceiroProdutoServico parceiroProdutoServico ");
			jpql.append(" left join fetch parceiroProdutoServico.parceiro parceiro ");
			jpql.append(" left join fetch parceiroProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join fetch produtoServico.linha linha ");
		} else {

			jpql.append("select count(parceiroProdutoServico.id) from ParceiroProdutoServico parceiroProdutoServico ");
			jpql.append(" left join parceiroProdutoServico.parceiro parceiro ");
			jpql.append(" left join parceiroProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join produtoServico.linha linha ");
			
		}
		
		jpql.append(" where parceiroProdutoServico.dataExclusao is null ");
		
		
		if(id != null) {
			jpql.append(" and parceiro.id = :id ");
			parametros.put("id", id);
		}
		
		if(!count) {
			jpql.append(" Order By linha.descricao ");
		}
		
	}
	
	private Long getCountQueryPaginado(Long idParceiro) {
		
		Map<String, Object> parametros = Maps.newHashMap();
		
		StringBuilder jpql = new StringBuilder();
		
		queryPorParceiro(jpql, parametros, idParceiro, true);
		
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		
		return DAOUtil.getSingleResult(query);
		
	}
	
	
	public ListaPaginada<ParceiroProdutoServico> retornarPorParceiro(ParceiroFilter parceiroFilter){
		LOGGER.debug("Pesquisando Departamento Regional produtos e serviços por Departamento Regional");
		
		ListaPaginada<ParceiroProdutoServico> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		
		queryPorParceiro(jpql, parametros, parceiroFilter.getId(), false);
		TypedQuery<ParceiroProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		
		listaPaginada.setQuantidade(getCountQueryPaginado(parceiroFilter.getId()));
		
		query.setFirstResult((parceiroFilter.getPagina() - 1) * parceiroFilter.getQuantidadeRegistro());
		query.setMaxResults(parceiroFilter.getQuantidadeRegistro());
		
		listaPaginada.setList(query.getResultList());
		
		return listaPaginada;
	}

	public ParceiroProdutoServico verificandoExistenciaProdutoServico(Long idparceiro, Long idProdutosServico) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select parceiroProdutoServico from ParceiroProdutoServico parceiroProdutoServico ");
		jpql.append(" left join fetch parceiroProdutoServico.parceiro parceiro ");
		jpql.append(" left join fetch parceiroProdutoServico.produtoServico produtoServico ");
		jpql.append(" left join fetch produtoServico.linha linha ");
		
		jpql.append(" where parceiroProdutoServico.dataExclusao is null ");
		jpql.append(" and produtoServico.id = :idProdutosServico  ");
		jpql.append(" and parceiro.id = :id  ");
		
		TypedQuery<ParceiroProdutoServico> query = getEm().createQuery(jpql.toString(), ParceiroProdutoServico.class);
		query.setParameter("id", idparceiro);
		query.setParameter("idProdutosServico" ,idProdutosServico);
		
		return DAOUtil.getSingleResult(query);
	}
	
}
