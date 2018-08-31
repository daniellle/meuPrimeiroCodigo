package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.ClassificacaoPontuacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.ClassificacaoPontuacao;
import fw.core.jpa.DAOUtil;

public class ClassificacaoPontuacaoDAO extends BaseRstDAO<ClassificacaoPontuacao, Long> {

	private static final String DESCRICAO = "descricao";

	private static final String SELECT_CLASSIFICACAO_PONTUACAO = "select classificacaoPontuacao from ClassificacaoPontuacao classificacaoPontuacao ";

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassificacaoPontuacaoDAO.class);
	
	@Inject
	public ClassificacaoPontuacaoDAO(EntityManager em) {
		super(em, ClassificacaoPontuacao.class, DESCRICAO);
	}
	
	public ListaPaginada<ClassificacaoPontuacao> pesquisarPaginado(ClassificacaoPontuacaoFilter classificacaoPontuacaoFilter) {

		ListaPaginada<ClassificacaoPontuacao> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		StringBuilder jpql = new StringBuilder();

		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginado(jpql, parametros, classificacaoPontuacaoFilter, false);
		TypedQuery<ClassificacaoPontuacao> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(classificacaoPontuacaoFilter));

		if (classificacaoPontuacaoFilter != null && classificacaoPontuacaoFilter.getPagina() != null
				&& classificacaoPontuacaoFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((classificacaoPontuacaoFilter.getPagina() - 1) * classificacaoPontuacaoFilter.getQuantidadeRegistro());
			query.setMaxResults(classificacaoPontuacaoFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;

	}

	public long getCountQueryPaginado(ClassificacaoPontuacaoFilter classificacaoPontuacaoFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, classificacaoPontuacaoFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			ClassificacaoPontuacaoFilter classificacaoPontuacaoFilter, boolean count) {

		if (count) {
			jpql.append("select count(DISTINCT classificacaoPontuacao.id) from ClassificacaoPontuacao classificacaoPontuacao ");
		} else {
			jpql.append("select DISTINCT classificacaoPontuacao from ClassificacaoPontuacao classificacaoPontuacao ");
		}

		jpql.append("where classificacaoPontuacao.dataExclusao is null ");
		
		if (classificacaoPontuacaoFilter != null && classificacaoPontuacaoFilter.getDescricao() != null) {
			jpql.append("and UPPER(classificacaoPontuacao.descricao) like :descricao escape :sc ");
			parametros.put("sc", "\\");
			parametros.put(DESCRICAO,
					"%" + classificacaoPontuacaoFilter.getDescricao().replaceAll("%", "\\%").toUpperCase() + "%");
		}
		
		if(!count) {
			jpql.append(" order by classificacaoPontuacao.descricao ");
		}
	}

	public ClassificacaoPontuacao pesquisarPorDescricao(String descricao) {
		LOGGER.debug("Pesquisando ClassificacaoPontuacao por Descricao");

		StringBuilder jpql = new StringBuilder();
		jpql.append(SELECT_CLASSIFICACAO_PONTUACAO);
		jpql.append("where upper(classificacaoPontuacao.descricao) = :descricao ");
		jpql.append("and classificacaoPontuacao.dataExclusao is null ");
		TypedQuery<ClassificacaoPontuacao> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter(DESCRICAO, descricao.toUpperCase());

		return DAOUtil.getSingleResult(query);
	}
	
	public List<ClassificacaoPontuacao> buscarClassificacaoPontuacaoPorValorMinimoEMaximo(Integer valorMinimo, Integer valorMaximo) {
		LOGGER.debug("Pesquisando ClassificacaoPontuacao com valor minimo e máximo entre %d e %d ",valorMinimo, valorMaximo);

		StringBuilder jpql = new StringBuilder();
		jpql.append(SELECT_CLASSIFICACAO_PONTUACAO);
		jpql.append("where classificacaoPontuacao.dataExclusao is null and (:valorMinimo BETWEEN classificacaoPontuacao.valorMinimo AND classificacaoPontuacao.valorMaximo ");
		jpql.append("OR :valorMaximo BETWEEN classificacaoPontuacao.valorMinimo AND classificacaoPontuacao.valorMaximo) ");
		TypedQuery<ClassificacaoPontuacao> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("valorMinimo", valorMinimo);
		query.setParameter("valorMaximo", valorMaximo);
		
		return query.getResultList();
	}
	
	public ClassificacaoPontuacao buscarClassificacaoPorPontuacao(int pontuacao) {
		LOGGER.debug("Pesquisando ClassificacaoPontuacao por pontuacao %d", pontuacao );

		StringBuilder jpql = new StringBuilder();
		jpql.append(SELECT_CLASSIFICACAO_PONTUACAO);
		jpql.append("where classificacaoPontuacao.dataExclusao is null and :pontuacao BETWEEN classificacaoPontuacao.valorMinimo AND classificacaoPontuacao.valorMaximo ");
		TypedQuery<ClassificacaoPontuacao> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("pontuacao", pontuacao);
		
		return DAOUtil.getSingleResult(query);
	}
	
	public Boolean classificacaoPontuacaoEmUso(Long idClassificacaoPontuacao) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select cp from QuestionarioTrabalhador qt ");
		jpql.append(" inner join qt.classificacaoPontuacao cp");		
		jpql.append(" where	cp.id = :id");		
		TypedQuery<ClassificacaoPontuacao> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("id", idClassificacaoPontuacao);
		
		return !query.getResultList().isEmpty();
	}
	
}
