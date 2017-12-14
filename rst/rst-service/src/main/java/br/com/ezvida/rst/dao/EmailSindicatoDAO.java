package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.EmailSindicato;
import fw.core.jpa.BaseDAO;

public class EmailSindicatoDAO extends BaseDAO<EmailSindicato, Long>{

	@Inject
	public EmailSindicatoDAO(EntityManager em) {
		super(em, EmailSindicato.class);
	}

	public List<EmailSindicato> pesquisarPorIdSindicato(Long id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select emailSind from EmailSindicato emailSind ");
		sqlBuilder.append(" inner join fetch emailSind.sindicato sindicato ");
		sqlBuilder.append(" inner join fetch emailSind.email email ");
		sqlBuilder.append(" where sindicato.id = :idSindicato and email.dataExclusao is null ");
		sqlBuilder.append(" order by email.descricao ");
		TypedQuery<EmailSindicato> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idSindicato", id);

		return query.getResultList();
	}

}
