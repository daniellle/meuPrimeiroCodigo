package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EnderecoParceiro;
import fw.core.jpa.BaseDAO;

public class EnderecoParceiroDAO extends BaseDAO<EnderecoParceiro, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoParceiroDAO.class);

	@Inject
	public EnderecoParceiroDAO(EntityManager em) {
		super(em, EnderecoParceiro.class);
	}

	public List<EnderecoParceiro> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Pesquisando EnderecoParceiro por idParceiro");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where parceiro.id = :idParceiro and endereco.dataExclusao is null ");
		jpql.append(" order by endereco.descricao ");
		TypedQuery<EnderecoParceiro> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idParceiro", idParceiro);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select distinct enderecoParceiro from EnderecoParceiro enderecoParceiro ");
		jpql.append("left join fetch enderecoParceiro.endereco endereco ");
		jpql.append("left join fetch enderecoParceiro.parceiro parceiro ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
	}

}
