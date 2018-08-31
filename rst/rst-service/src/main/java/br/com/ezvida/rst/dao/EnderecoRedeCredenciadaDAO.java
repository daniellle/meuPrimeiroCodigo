package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EnderecoRedeCredenciada;
import fw.core.jpa.BaseDAO;

public class EnderecoRedeCredenciadaDAO extends BaseDAO<EnderecoRedeCredenciada, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoRedeCredenciadaDAO.class);

	@Inject
	public EnderecoRedeCredenciadaDAO(EntityManager em) {
		super(em, EnderecoRedeCredenciada.class);
	}

	public List<EnderecoRedeCredenciada> pesquisarPorRedeCredenciada(Long idRedeCredenciada) {
		LOGGER.debug("Pesquisando EnderecoRedeCredenciada por idRedeCredenciada");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where redeCredenciada.id = :idRedeCredenciada and endereco.dataExclusao is null ");
		jpql.append(" order by endereco.descricao ");
		TypedQuery<EnderecoRedeCredenciada> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idRedeCredenciada", idRedeCredenciada);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select distinct enderecoRedeCredenciada from EnderecoRedeCredenciada enderecoRedeCredenciada ");
		jpql.append("left join fetch enderecoRedeCredenciada.endereco endereco ");
		jpql.append("left join fetch enderecoRedeCredenciada.redeCredenciada redeCredenciada ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
	}

}
