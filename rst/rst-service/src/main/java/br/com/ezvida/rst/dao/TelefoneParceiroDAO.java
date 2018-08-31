package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.TelefoneParceiro;
import fw.core.jpa.BaseDAO;

public class TelefoneParceiroDAO extends BaseDAO<TelefoneParceiro, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneParceiroDAO.class);

	@Inject
	public TelefoneParceiroDAO(EntityManager em) {
		super(em, TelefoneParceiro.class);
	}

	public List<TelefoneParceiro> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Pesquisando TelefoneParceiro por idParceiro");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where parceiro.id = :idParceiro and telefone.dataExclusao is null ");
		jpql.append(" order by telefone.numero ");
		TypedQuery<TelefoneParceiro> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idParceiro", idParceiro);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select telefoneParceiro from TelefoneParceiro telefoneParceiro ");
		jpql.append("left join fetch telefoneParceiro.telefone telefone ");
		jpql.append("left join fetch telefoneParceiro.parceiro parceiro ");
	}

}
