package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.EnderecoProfissional;
import fw.core.jpa.BaseDAO;

public class EnderecoProfissionalDAO extends BaseDAO<EnderecoProfissional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoProfissionalDAO.class);

	@Inject
	public EnderecoProfissionalDAO(EntityManager em) {
		super(em, EnderecoProfissional.class);
	}

	public Set<EnderecoProfissional> pesquisarPorProfissional(Long id) {
		LOGGER.debug("Buscando endereços por profissional...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select enderecoProfissional from EnderecoProfissional enderecoProfissional ");
		sqlBuilder.append("inner join fetch enderecoProfissional.profissional profissional ");
		sqlBuilder.append("inner join fetch enderecoProfissional.endereco endereco ");
		sqlBuilder.append("inner join fetch endereco.municipio municipio ");
		sqlBuilder.append("inner join fetch municipio.estado estado ");
		sqlBuilder.append("where profissional.id = :idProfissional and endereco.dataExclusao is null ");
		sqlBuilder.append(" order by endereco.descricao ");
		TypedQuery<EnderecoProfissional> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idProfissional", id);

		return Sets.newHashSet(query.getResultList());
	}

}
