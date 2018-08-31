package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.ProfissionalEspecialidade;

public class ProfissionalEspecialidadeDAO extends BaseRstDAO<ProfissionalEspecialidade, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfissionalEspecialidadeDAO.class);

	@Inject
	public ProfissionalEspecialidadeDAO(EntityManager em) {
		super(em, ProfissionalEspecialidade.class);
	}

	public List<ProfissionalEspecialidade> pesquisarPorProfissional(Long id) {
		LOGGER.debug("Buscando Especialidade ativas por profissional...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select profissionalEspecialidade from ProfissionalEspecialidade profissionalEspecialidade ");
		sqlBuilder.append(" inner join fetch profissionalEspecialidade.profissional profissional ");
		sqlBuilder.append(" inner join fetch profissionalEspecialidade.especialidade especialidade ");
		sqlBuilder.append(" where profissionalEspecialidade.dataExclusao is null and profissional.id = :idProfissional ");
		sqlBuilder.append(" order by especialidade.descricao ");
		TypedQuery<ProfissionalEspecialidade> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idProfissional", id);

		return query.getResultList();
	}

}
