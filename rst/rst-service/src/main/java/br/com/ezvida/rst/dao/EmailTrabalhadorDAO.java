package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.EmailTrabalhador;
import fw.core.jpa.BaseDAO;

public class EmailTrabalhadorDAO extends BaseDAO<EmailTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTrabalhadorDAO.class);
	
	@Inject
	public EmailTrabalhadorDAO(EntityManager em) {
		super(em, EmailTrabalhador.class);
	}
	
	public Set<EmailTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Pesquisando EmailTrabalhador por idTrabalhador");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where emailTrabalhador.trabalhador.id = :idTrabalhador and email.dataExclusao is null ");
		jpql.append(" order by email.descricao ");
		TypedQuery<EmailTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);
		return Sets.newHashSet(query.getResultList());

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select emailTrabalhador from EmailTrabalhador emailTrabalhador ");
		jpql.append("left join fetch emailTrabalhador.email email ");
		jpql.append("left join fetch emailTrabalhador.trabalhador trabalhador ");
	}
	
	public Set<EmailTrabalhador> pesquisarPorEmail(String email) {
	    
	    StringBuilder jpql = new StringBuilder();
	    
	    jpql.append(" select emailTrabalhador from EmailTrabalhador emailTrabalhador ");
	    jpql.append(" left join fetch emailTrabalhador.email email ");
	    jpql.append(" left join fetch emailTrabalhador.trabalhador trabalhador ");
	    jpql.append(" where email.descricao = :email ");
	    
	    TypedQuery<EmailTrabalhador> query = criarConsultaPorTipo(jpql.toString());
	    query.setParameter("email", email);
	    
	    return Sets.newHashSet(query.getResultList());
	}

}
