package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.Municipio;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class MunicipioDAO extends BaseDAO<Municipio, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioDAO.class);

	@Inject
	public MunicipioDAO(EntityManager em) {
		super(em, Municipio.class, "descricao");
	}

	@Override
	public Municipio pesquisarPorId(Long id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select m from Municipio m ");
		sqlBuilder.append(" inner join fetch m.estado estado ");
		sqlBuilder.append(" where m.id = :id ");
		sqlBuilder.append(" order by m.descricao ");
		TypedQuery<Municipio> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("id", id);

		return DAOUtil.getSingleResult(query);

	}

	public List<Municipio> pesquisarTodosComEstado() {
		LOGGER.debug("Pesquisando todos municípios com estado");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select m from Municipio m left join fetch m.estado e ");
		jpql.append("left join fetch m.regiao r ");
		jpql.append(" order by m.descricao ");
		return criarConsultaPorTipo(jpql.toString()).getResultList();
	}

	public List<Municipio> pesquisar(String descricao) {
		LOGGER.debug("Pesquisando município por descrição");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select m from Municipio m left join fetch m.estado e ");
		jpql.append("left join fetch m.regiao r ");
		
		if (descricao != null && !descricao.isEmpty()) {
			jpql.append("where m.descricao like '%".concat(descricao).concat("%'"));
		}
		
		jpql.append(" order by m.descricao ");
		TypedQuery<Municipio> query = criarConsultaPorTipo(jpql.toString());
		
		return query.getResultList();
	}
	
	public List<Municipio> pesquisarPorIdEstado(Long idEstado) {
		LOGGER.debug("Pesquisando município por id de Estado");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select m from Municipio m");
		jpql.append(" where ");
		jpql.append(" m.estado.id = :idEstado ");
		jpql.append(" order by m.descricao ");
		
		TypedQuery<Municipio> query = criarConsultaPorTipo(jpql.toString());

		query.setParameter("idEstado", idEstado);
		
		return query.getResultList();
	}

}
