package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.TelefoneUnidadeAtendimentoTrabalhador;
import fw.core.jpa.BaseDAO;

public class TelefoneUnidadeAtendimentoTrabalhadorDAO extends BaseDAO<TelefoneUnidadeAtendimentoTrabalhador, Long> {

	@Inject
	public TelefoneUnidadeAtendimentoTrabalhadorDAO(EntityManager em) {
		super(em, TelefoneUnidadeAtendimentoTrabalhador.class);
	}

	public List<TelefoneUnidadeAtendimentoTrabalhador> pesquisarPorIdUat(Long id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select telUat from TelefoneUnidadeAtendimentoTrabalhador telUat ");
		sqlBuilder.append(" inner join fetch telUat.telefone telefone ");
		sqlBuilder.append(" inner join fetch telUat.unidadeAtendimentoTrabalhador uat ");
		sqlBuilder.append(" where telefone.dataExclusao is null and uat.id = :id ");
		sqlBuilder.append(" order by telefone.numero ");
		TypedQuery<TelefoneUnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("id", id);

		return query.getResultList();
	}
}
