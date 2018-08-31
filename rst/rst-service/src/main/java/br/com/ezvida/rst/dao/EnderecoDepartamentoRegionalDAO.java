package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EnderecoDepartamentoRegional;
import fw.core.jpa.BaseDAO;

public class EnderecoDepartamentoRegionalDAO extends BaseDAO<EnderecoDepartamentoRegional, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoDepartamentoRegionalDAO.class);

	@Inject
	public EnderecoDepartamentoRegionalDAO(EntityManager em) {
		super(em, EnderecoDepartamentoRegional.class);
	}

	public List<EnderecoDepartamentoRegional> pesquisarPorDepartamentoRegional(Long idDepartamentoRegional) {
		LOGGER.debug("Pesquisando EnderecoDepartamentoRegional por idDepartamentoRegional");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where departamentoRegional.id = :idDepartamentoRegional and endereco.dataExclusao is null ");
		jpql.append(" order by endereco.descricao ");
		TypedQuery<EnderecoDepartamentoRegional> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idDepartamentoRegional", idDepartamentoRegional);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append(
				"select distinct enderecoDepartamentoRegional from EnderecoDepartamentoRegional enderecoDepartamentoRegional ");
		jpql.append("left join fetch enderecoDepartamentoRegional.endereco endereco ");
		jpql.append("left join fetch enderecoDepartamentoRegional.departamentoRegional departamentoRegional ");
		jpql.append("left join fetch endereco.municipio municipio ");
		jpql.append("left join fetch municipio.estado estado ");
	}

}
