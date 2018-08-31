package br.com.ezvida.rst.dao;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;
import fw.core.model.BaseModel;

public abstract class BaseRstDAO<T extends BaseModel<?>, P> extends BaseDAO<T, P> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRstDAO.class);

	public BaseRstDAO(EntityManager em, Class<T> type) {
		super(em, type);
	}
	
	public BaseRstDAO(EntityManager em, Class<T> tipoClasse, String ordem) {
		super(em, tipoClasse, ordem);
	}

	public int desativar(Long id, List<Long> ids, Class<?> type, String... fieldClass) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ").append(getTipoClasse().getSimpleName()).append(" set dataExclusao = :dataAtual ");

		sqlBuilder.append(" where ");
		try {
			sqlBuilder.append(" id in (select entity.").append(type.getDeclaredField(fieldClass[1]).getName()).append(".id from ");
			sqlBuilder.append(type.getSimpleName());
			sqlBuilder.append(" entity where entity.").append(type.getDeclaredField(fieldClass[1]).getName()).append(".dataExclusao is null ");
			sqlBuilder.append(" and entity.").append(type.getDeclaredField(fieldClass[0]).getName()).append(".id = :id ");
		} catch (Exception e) {
			LOGGER.debug("Field não encontrado á classe {}", type.getSimpleName(), e);
		}
		if (!ids.isEmpty()) {
			sqlBuilder.append(" and entity.id not in (:ids) ");
		}
		sqlBuilder.append(" ) ");

		Query query = criarConsulta(sqlBuilder.toString());

		query.setParameter("dataAtual", new Date());
		query.setParameter("id", id);

		if (!ids.isEmpty()) {
			query.setParameter("ids", ids);
		}

		return query.executeUpdate();
	}

	public int desativar(Long id, List<Long> ids, String field) {
		StringBuilder sqlBuilder = new StringBuilder();
		try {
			sqlBuilder.append("update ").append(getTipoClasse().getSimpleName()).append(" entity set dataExclusao = :dataAtual ");
			sqlBuilder.append(" where entity.").append(getTipoClasse().getDeclaredField(field).getName()).append(".id = :id ");
			sqlBuilder.append(" and entity.dataExclusao is null ");
			if (!ids.isEmpty()) {
				sqlBuilder.append(" and entity.id not in (:ids) ");
			}
		} catch (Exception e) {
			LOGGER.debug("Field não encontrado á classe {}", getClass().getSimpleName(), e);
		}

		Query query = criarConsulta(sqlBuilder.toString());
		query.setParameter("dataAtual", new Date());
		query.setParameter("id", id);

		if (!ids.isEmpty()) {
			query.setParameter("ids", ids);
		}

		return query.executeUpdate();
	}

	public T pesquisarPorIdFetchAll(P id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select entity ");
		jpql.append(" from ").append(getTipoClasse().getSimpleName()).append(" entity ");

		for (Field field : getTipoClasse().getDeclaredFields()) {
			if (field.getDeclaredAnnotation(ManyToOne.class) != null) {
				jpql.append(" inner join fetch entity.").append(field.getName());
			}
		}

		jpql.append(" where entity.id = :id ");

		TypedQuery<T> query = criarConsultaPorTipo(jpql.toString());

		query.setParameter("id", id);

		return DAOUtil.getSingleResult(query);
	}

	public void getAnd(StringBuilder sqlBuilder, boolean and) {
		if (and) {
			sqlBuilder.append(" and ");
		}
	}
}
