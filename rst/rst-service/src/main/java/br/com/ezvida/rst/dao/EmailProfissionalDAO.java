package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.EmailProfissional;
import fw.core.jpa.BaseDAO;

public class EmailProfissionalDAO extends BaseDAO<EmailProfissional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailProfissionalDAO.class);

	@Inject
	public EmailProfissionalDAO(EntityManager em) {
		super(em, EmailProfissional.class);
	}

	public Set<EmailProfissional> pesquisarPorProfissional(Long idProfissional) {
		LOGGER.debug("Pesquisando EmailProfissional por idProfissional");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where emailProfissional.profissional.id = :idProfissional and email.dataExclusao is null ");
		jpql.append(" order by email.descricao ");
		TypedQuery<EmailProfissional> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idProfissional", idProfissional);
		return Sets.newHashSet(query.getResultList());

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select emailProfissional from EmailProfissional emailProfissional ");
		jpql.append("left join fetch emailProfissional.email email ");
		jpql.append("left join fetch emailProfissional.profissional profissional ");
	}

}
