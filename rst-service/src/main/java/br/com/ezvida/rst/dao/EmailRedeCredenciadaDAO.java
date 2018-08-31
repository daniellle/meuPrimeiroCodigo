package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EmailRedeCredenciada;

public class EmailRedeCredenciadaDAO extends BaseRstDAO<EmailRedeCredenciada, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRedeCredenciadaDAO.class);
	
	@Inject
	public EmailRedeCredenciadaDAO(EntityManager em) {
		super(em, EmailRedeCredenciada.class);
	}
	
	public List<EmailRedeCredenciada> pesquisarPorRedeCredenciada(Long idRedeCredenciada) {
		LOGGER.debug("Pesquisando EmailRedeCredenciada por idEmailRedeCredenciada");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where redeCredenciada.id = :idRedeCredenciada and email.dataExclusao is null ");
		jpql.append(" order by email.descricao ");
		TypedQuery<EmailRedeCredenciada> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idRedeCredenciada", idRedeCredenciada);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select emailRedeCredenciada from EmailRedeCredenciada emailRedeCredenciada ");
		jpql.append("left join fetch emailRedeCredenciada.email email ");
		jpql.append("left join fetch emailRedeCredenciada.redeCredenciada redeCredenciada ");
	}

}
