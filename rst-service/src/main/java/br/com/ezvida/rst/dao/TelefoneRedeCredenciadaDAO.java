package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.TelefoneRedeCredenciada;
import fw.core.jpa.BaseDAO;

public class TelefoneRedeCredenciadaDAO extends BaseDAO<TelefoneRedeCredenciada, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneRedeCredenciadaDAO.class);

	@Inject
	public TelefoneRedeCredenciadaDAO(EntityManager em) {
		super(em, TelefoneRedeCredenciada.class);
	}

	public List<TelefoneRedeCredenciada> pesquisarPorRedeCredenciada(Long idRedeCredenciada) {
		LOGGER.debug("Pesquisando TelefoneRedeCredenciada por idRedeCredenciada");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where redeCredenciada.id = :idRedeCredenciada and telefone.dataExclusao is null ");
		jpql.append(" order by telefone.numero ");
		TypedQuery<TelefoneRedeCredenciada> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idRedeCredenciada", idRedeCredenciada);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select telefoneRedeCredenciada from TelefoneRedeCredenciada telefoneRedeCredenciada ");
		jpql.append("left join fetch telefoneRedeCredenciada.telefone telefone ");
		jpql.append("left join fetch telefoneRedeCredenciada.redeCredenciada redeCredenciada ");
	}

}
