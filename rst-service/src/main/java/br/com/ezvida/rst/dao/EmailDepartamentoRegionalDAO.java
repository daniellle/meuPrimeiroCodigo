package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.EmailDepartamentoRegional;
import fw.core.jpa.BaseDAO;

public class EmailDepartamentoRegionalDAO extends BaseDAO<EmailDepartamentoRegional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailDepartamentoRegionalDAO.class);
	
	@Inject
	public EmailDepartamentoRegionalDAO(EntityManager em) {
		super(em, EmailDepartamentoRegional.class);
	}
	
	public Set<EmailDepartamentoRegional> pesquisarPorDepartamentoRegional(Long idDepartamentoRegional) {
		LOGGER.debug("Pesquisando EmailDepartamentoRegional por idDepartamentoRegional");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where departamentoRegional.id = :idDepartamentoRegional and email.dataExclusao is null ");
		jpql.append(" order by email.descricao ");
		TypedQuery<EmailDepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idDepartamentoRegional", idDepartamentoRegional);
		return Sets.newHashSet(query.getResultList());

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select emailDepartamentoRegional from EmailDepartamentoRegional emailDepartamentoRegional ");
		jpql.append("left join fetch emailDepartamentoRegional.email email ");
		jpql.append("left join fetch emailDepartamentoRegional.departamentoRegional departamentoRegional ");
	}

}
