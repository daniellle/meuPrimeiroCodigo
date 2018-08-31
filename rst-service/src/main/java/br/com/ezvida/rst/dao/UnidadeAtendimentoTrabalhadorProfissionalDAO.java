package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhadorProfissional;

public class UnidadeAtendimentoTrabalhadorProfissionalDAO extends BaseRstDAO<UnidadeAtendimentoTrabalhadorProfissional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeAtendimentoTrabalhadorProfissionalDAO.class);

	@Inject
	public UnidadeAtendimentoTrabalhadorProfissionalDAO(EntityManager em) {
		super(em, UnidadeAtendimentoTrabalhadorProfissional.class);
	}

	public Set<UnidadeAtendimentoTrabalhadorProfissional> pesquisarPorProfissional(Long id) {
		LOGGER.debug("Buscando unidadeAtendimentoTrabalhador ativos por profissional...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				" select uatProfissional from UnidadeAtendimentoTrabalhadorProfissional uatProfissional ");
		sqlBuilder.append(" inner join fetch uatProfissional.profissional profissional ");
		sqlBuilder.append(" inner join fetch uatProfissional.unidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador ");
		sqlBuilder.append(" where uatProfissional.dataExclusao is null and profissional.id = :idProfissional ");
		sqlBuilder.append(" order by unidadeAtendimentoTrabalhador.razaoSocial ");
		TypedQuery<UnidadeAtendimentoTrabalhadorProfissional> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idProfissional", id);

		return Sets.newHashSet(query.getResultList());
	}
}
