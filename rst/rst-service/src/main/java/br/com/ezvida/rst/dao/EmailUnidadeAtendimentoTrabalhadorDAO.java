package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.EmailUnidadeAtendimentoTrabalhador;
import fw.core.jpa.BaseDAO;

public class EmailUnidadeAtendimentoTrabalhadorDAO extends BaseDAO<EmailUnidadeAtendimentoTrabalhador, Long> {

	@Inject
	public EmailUnidadeAtendimentoTrabalhadorDAO(EntityManager em) {
		super(em, EmailUnidadeAtendimentoTrabalhador.class);
	}

	public List<EmailUnidadeAtendimentoTrabalhador> pesquisarPorIdUat(Long id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select distinct emailUat from EmailUnidadeAtendimentoTrabalhador emailUat ");
		sqlBuilder.append(" inner join fetch emailUat.email email ");
		sqlBuilder.append(" inner join fetch emailUat.unidadeAtendimentoTrabalhador unidAtendTrabalhador ");
		sqlBuilder.append(" where unidAtendTrabalhador.id = :idUat and email.dataExclusao is null ");
		TypedQuery<EmailUnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idUat", id);

		return query.getResultList();
	}
}
