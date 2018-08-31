package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.TelefoneProfissional;
import fw.core.jpa.BaseDAO;

public class TelefoneProfissionalDAO extends BaseDAO<TelefoneProfissional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneProfissionalDAO.class);

	@Inject
	public TelefoneProfissionalDAO(EntityManager em) {
		super(em, TelefoneProfissional.class);
	}

	public List<TelefoneProfissional> pesquisarPorProfissional(Long id) {
		LOGGER.debug("Buscando telefones ativos por profissional...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select telefoneProfissional from TelefoneProfissional telefoneProfissional ");
		sqlBuilder.append(" inner join fetch telefoneProfissional.profissional profissional ");
		sqlBuilder.append(" inner join fetch telefoneProfissional.telefone telefone ");
		sqlBuilder.append(" where telefone.dataExclusao is null and profissional.id = :idProfissional ");
		sqlBuilder.append(" order by telefone.numero ");
		TypedQuery<TelefoneProfissional> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idProfissional", id);

		return query.getResultList();
	}

}
