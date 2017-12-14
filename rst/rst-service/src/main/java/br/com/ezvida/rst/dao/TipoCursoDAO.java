package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.TipoCurso;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class TipoCursoDAO extends BaseDAO<TipoCurso, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TipoCursoDAO.class);
	
	@Inject
	public TipoCursoDAO(EntityManager em) {
		super(em, TipoCurso.class, "descricao");
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoCurso> pesquisarTodos(){
		LOGGER.debug("Pesquisando todos tipos de curso");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select t from TipoCurso t ");
		jpql.append(" order by t.descricao ");
		return criarConsulta(jpql.toString()).getResultList();
	}
	
	public TipoCurso pesquisarPorDescricao(String descricao) {
		LOGGER.debug("Pesquisando Tipo Curso por Descricao");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select tipoCurso from TipoCurso tipoCurso where upper(tipoCurso.descricao) = :descricao");
		jpql.append(" order by tipoCurso.descricao");
		TypedQuery<TipoCurso> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("descricao", descricao.toUpperCase());

		return DAOUtil.getSingleResult(query);
	}

}
