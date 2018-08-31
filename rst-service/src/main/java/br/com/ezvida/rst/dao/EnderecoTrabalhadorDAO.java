package br.com.ezvida.rst.dao;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.EnderecoTrabalhador;
import fw.core.jpa.BaseDAO;

public class EnderecoTrabalhadorDAO extends BaseDAO<EnderecoTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoTrabalhadorDAO.class);

	@Inject
	public EnderecoTrabalhadorDAO(EntityManager em) {
		super(em, EnderecoTrabalhador.class);
	}

	public Set<EnderecoTrabalhador> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Pesquisando EnderecoTrabalhador por idTrabalhador");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where trabalhador.id = :idTrabalhador and endereco.dataExclusao is null ");
		jpql.append(" order by endereco.descricao ");
		TypedQuery<EnderecoTrabalhador> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);
		return Sets.newHashSet(query.getResultList());

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select enderecoTrabalhador from EnderecoTrabalhador enderecoTrabalhador ");
		jpql.append("left join fetch enderecoTrabalhador.endereco endereco ");
		jpql.append("left join fetch enderecoTrabalhador.trabalhador trabalhador ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
	}

}
