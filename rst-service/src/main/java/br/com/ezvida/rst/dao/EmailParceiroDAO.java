package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EmailParceiro;

public class EmailParceiroDAO extends BaseRstDAO<EmailParceiro, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailParceiroDAO.class);
	
	@Inject
	public EmailParceiroDAO(EntityManager em) {
		super(em, EmailParceiro.class);
	}
	
	public List<EmailParceiro> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Pesquisando EmailParceiro por idEmailParceiro");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where parceiro.id = :idParceiro and email.dataExclusao is null ");
		jpql.append(" order by email.descricao ");
		TypedQuery<EmailParceiro> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idParceiro", idParceiro);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select emailParceiro from EmailParceiro emailParceiro ");
		jpql.append("left join fetch emailParceiro.email email ");
		jpql.append("left join fetch emailParceiro.parceiro parceiro ");
	}

}
