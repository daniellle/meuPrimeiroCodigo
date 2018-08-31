package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.EnderecoUnidadeAtendimentoTrabalhador;
import fw.core.jpa.BaseDAO;

public class EnderecoUnidadeAtendimentoTrabalhadorDAO extends BaseDAO<EnderecoUnidadeAtendimentoTrabalhador, Long> {

	@Inject
	public EnderecoUnidadeAtendimentoTrabalhadorDAO(EntityManager em) {
		super(em, EnderecoUnidadeAtendimentoTrabalhador.class);
	}

	public List<EnderecoUnidadeAtendimentoTrabalhador> pesquisarPorIdUat(Long id) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select distinct enderecoUat from EnderecoUnidadeAtendimentoTrabalhador enderecoUat ");
		jpql.append("left join fetch enderecoUat.endereco endereco ");
		jpql.append("left join fetch enderecoUat.unidadeAtendimentoTrabalhador unidAtendTrabalhador ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
		jpql.append(" where unidAtendTrabalhador.id = :idUat and endereco.dataExclusao is null ");
		jpql.append(" order by endereco.descricao ");
		TypedQuery<EnderecoUnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idUat", id);
		return query.getResultList();
	}
}
