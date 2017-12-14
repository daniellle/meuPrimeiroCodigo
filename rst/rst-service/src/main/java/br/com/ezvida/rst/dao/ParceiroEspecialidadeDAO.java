package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.ParceiroEspecialidade;

public class ParceiroEspecialidadeDAO extends BaseRstDAO<ParceiroEspecialidade, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroEspecialidadeDAO.class);
	
	@Inject
	public ParceiroEspecialidadeDAO(EntityManager em) {
		super(em, ParceiroEspecialidade.class);
	}
	
	public List<ParceiroEspecialidade> pesquisarPorParceiro(Long idParceiro) {
		LOGGER.debug("Pesquisando ParceiroEspecialidade por idParceiro ");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where parceiro.id = :idParceiro and parceiroEspecialidade.dataExclusao is null ");
		jpql.append(" order by especialidade.descricao ");
		TypedQuery<ParceiroEspecialidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idParceiro", idParceiro);
		return query.getResultList();

	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select parceiroEspecialidade from ParceiroEspecialidade parceiroEspecialidade ");
		jpql.append("inner join fetch parceiroEspecialidade.parceiro parceiro ");
		jpql.append("inner join fetch parceiroEspecialidade.especialidade especialidade ");
	}

}
