package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.TelefoneSindicato;
import fw.core.jpa.BaseDAO;

public class TelefoneSindicatoDAO extends BaseDAO<TelefoneSindicato, Long>{

	@Inject
	public TelefoneSindicatoDAO(EntityManager em) {
		super(em, TelefoneSindicato.class);
	}

	public List<TelefoneSindicato> pesquisarPorIdSindicato(Long id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select telSind from TelefoneSindicato telSind ");
		sqlBuilder.append(" inner join fetch telSind.sindicato sindicato ");
		sqlBuilder.append(" inner join fetch telSind.telefone telefone ");
		sqlBuilder.append(" where sindicato.id = :idSindicato and telefone.dataExclusao is null ");
		sqlBuilder.append(" order by telefone.numero ");
		TypedQuery<TelefoneSindicato> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idSindicato", id);

		return query.getResultList();
	}

}
