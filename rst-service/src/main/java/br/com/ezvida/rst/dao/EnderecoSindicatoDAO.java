package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.EnderecoSindicato;
import fw.core.jpa.BaseDAO;

public class EnderecoSindicatoDAO extends BaseDAO<EnderecoSindicato, Long>{

	@Inject
	public EnderecoSindicatoDAO(EntityManager em) {
		super(em, EnderecoSindicato.class);
	}

	public List<EnderecoSindicato> pesquisarPorIdSindicato(Long id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select endSind from EnderecoSindicato endSind ");
		sqlBuilder.append(" inner join fetch endSind.sindicato sindicato ");
		sqlBuilder.append(" inner join fetch endSind.endereco endereco ");
		sqlBuilder.append(" inner join fetch endereco.municipio municipio ");
		sqlBuilder.append(" inner join fetch municipio.estado estado ");
		sqlBuilder.append(" where sindicato.id = :idSindicato and endereco.dataExclusao is null ");
		sqlBuilder.append(" order by endereco.descricao ");
		TypedQuery<EnderecoSindicato> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idSindicato", id);

		return query.getResultList();
	}

}
