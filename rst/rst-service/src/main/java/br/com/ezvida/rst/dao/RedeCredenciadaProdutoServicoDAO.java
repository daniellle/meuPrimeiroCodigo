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
import br.com.ezvida.rst.dao.filter.RedeCredenciadaFilter;
import br.com.ezvida.rst.model.RedeCredenciadaProdutoServico;
import fw.core.jpa.DAOUtil;

public class RedeCredenciadaProdutoServicoDAO extends BaseRstDAO<RedeCredenciadaProdutoServico, Long>{
	
	@Inject
	public RedeCredenciadaProdutoServicoDAO(EntityManager em) {
		super(em, RedeCredenciadaProdutoServico.class);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(RedeCredenciadaProdutoServicoDAO.class);

	private void QueryPorRedeCredenciada(StringBuilder jpql, Map<String, Object> parametros, Long id, boolean count) {
		
		if (!count) {
			jpql.append("select redeCredenciadaProdutoServico from RedeCredenciadaProdutoServico redeCredenciadaProdutoServico ");
			jpql.append(" left join fetch redeCredenciadaProdutoServico.redeCredenciada redeCredenciada ");
			jpql.append(" left join fetch redeCredenciadaProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join fetch produtoServico.linha linha ");
		} else {
			
			jpql.append("select count(redeCredenciadaProdutoServico.id) from RedeCredenciadaProdutoServico redeCredenciadaProdutoServico ");
			jpql.append(" left join redeCredenciadaProdutoServico.redeCredenciada redeCredenciada ");
			jpql.append(" left join redeCredenciadaProdutoServico.produtoServico produtoServico ");
			jpql.append(" left join produtoServico.linha linha ");
			
		}
		
		jpql.append(" where redeCredenciadaProdutoServico.dataExclusao is null ");
		
		
		if(id != null) {
			jpql.append(" and redeCredenciada.id = :id ");
			parametros.put("id", id);
		}
		
		if(!count) {
			jpql.append(" Order By linha.descricao ");
		}
		
	}
	
	private Long getCountQueryPaginado(Long idRedeCredenciada) {
		
		Map<String, Object> parametros = Maps.newHashMap();
		
		StringBuilder jpql = new StringBuilder();
		
		QueryPorRedeCredenciada(jpql, parametros, idRedeCredenciada, true);
		
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		
		return DAOUtil.getSingleResult(query);
		
	}
	
	public ListaPaginada<RedeCredenciadaProdutoServico> retornarPorRedeCredenciadal(RedeCredenciadaFilter redeCredenciada){
		LOGGER.debug("Pesquisando Rede Credenciada produtos e serviços por Rede Credenciada");
		
		ListaPaginada<RedeCredenciadaProdutoServico> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		
		QueryPorRedeCredenciada(jpql, parametros, redeCredenciada.getId(), false);
		TypedQuery<RedeCredenciadaProdutoServico> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		
		listaPaginada.setQuantidade(getCountQueryPaginado(redeCredenciada.getId()));
		
		query.setFirstResult((redeCredenciada.getPagina() - 1) * redeCredenciada.getQuantidadeRegistro());
		query.setMaxResults(redeCredenciada.getQuantidadeRegistro());
		
		listaPaginada.setList(query.getResultList());
		
		return listaPaginada;
	}
	public RedeCredenciadaProdutoServico verificandoExistenciaProdutoServico(Long idRedeCredenciada, Long idProdutosServico) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select redeCredenciadaProdutoServico from RedeCredenciadaProdutoServico redeCredenciadaProdutoServico ");
		jpql.append(" left join fetch redeCredenciadaProdutoServico.redeCredenciada redeCredenciada ");
		jpql.append(" left join fetch redeCredenciadaProdutoServico.produtoServico produtoServico ");
		jpql.append(" left join fetch produtoServico.linha linha ");
		
		jpql.append(" where RedeCredenciadaProdutoServico.dataExclusao is null ");
		jpql.append(" and produtoServico.id = :idProdutosServico  ");
		jpql.append(" and redeCredenciada.id = :id  ");
		
		TypedQuery<RedeCredenciadaProdutoServico> query = getEm().createQuery(jpql.toString(), RedeCredenciadaProdutoServico.class);
		query.setParameter("id", idRedeCredenciada);
		query.setParameter("idProdutosServico" ,idProdutosServico);
		
		return DAOUtil.getSingleResult(query);
	}
}
