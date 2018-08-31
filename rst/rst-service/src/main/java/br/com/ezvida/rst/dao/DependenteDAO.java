package br.com.ezvida.rst.dao;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import br.com.ezvida.rst.model.Dependente;
import fw.core.jpa.DAOUtil;

public class DependenteDAO extends BaseRstDAO<Dependente, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DependenteDAO.class);

	@Inject
	public DependenteDAO(EntityManager em) {
		super(em, Dependente.class, "nome");
	}

	public Set<Dependente> pesquisarPorTrabalhador(Long idTrabalhador) {
		LOGGER.debug("Pesquisando Dependente por idTrabalhador");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where trabalhador.id = :idTrabalhador and dependente.dataExclusao is null");
		jpql.append(" order by dependente.nome ");
		TypedQuery<Dependente> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idTrabalhador", idTrabalhador);
		return Sets.newHashSet(query.getResultList());

	}

	public Dependente pesquisarPorCPF(String cpf) {
		LOGGER.debug("Pesquisando Dependente por CPF");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where dependente.cpf = :cpf");
		TypedQuery<Dependente> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);
		return DAOUtil.getSingleResult(query);
	}

	public List<Dependente> pesquisarPorCPF(Long idTrabalhador, List<String> cpfs) {
		LOGGER.debug("Pesquisando Dependente por CPF");
		StringBuilder jpql = new StringBuilder();

		jpql.append("select dependente from Dependente dependente ");
		jpql.append("left join fetch dependente.trabalhador trabalhador ");
		jpql.append(" where dependente.cpf in (:cpf) and trabalhador.id <> :idTrabalhador ");
		jpql.append(" order by dependente.nome ");

		TypedQuery<Dependente> query = criarConsultaPorTipo(jpql.toString());

		query.setParameter("cpf", cpfs);
		query.setParameter("idTrabalhador", idTrabalhador);

		return query.getResultList();
	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select dependente from Dependente dependente ");
	}
}
