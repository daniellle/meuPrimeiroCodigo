package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.EmpresaCnae;
import fw.core.jpa.DAOUtil;

public class EmpresaCnaeDAO extends BaseRstDAO<EmpresaCnae, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaCnaeDAO.class);

	@Inject
	public EmpresaCnaeDAO(EntityManager em) {
		super(em, EmpresaCnae.class);
	}

	public List<EmpresaCnae> buscarPorEmpresa(Long idEmpresa) {
		LOGGER.debug("Pesquisando EmpresaCnae por idEmpresa");
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where empresa.id = :idEmpresa and empresaCnae.dataExclusao is null ");
		jpql.append(" order by cnae.descricao ");
		TypedQuery<EmpresaCnae> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idEmpresa", idEmpresa);
		return query.getResultList();
	}

	private void montarQuery(StringBuilder jpql) {
		jpql.append("select empresaCnae from EmpresaCnae empresaCnae ");
		jpql.append("left join fetch empresaCnae.empresa empresa ");
		jpql.append("left join fetch empresaCnae.cnae cnae ");
	}

	public EmpresaCnae buscarPrincipal(Long idEmpresa) {
		StringBuilder jpql = new StringBuilder();
		montarQuery(jpql);
		jpql.append(" where empresa.id = :idEmpresa and empresaCnae.dataExclusao is null and empresaCnae.principal = :principal ");
		TypedQuery<EmpresaCnae> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idEmpresa", idEmpresa);
		query.setParameter("principal", SimNao.SIM);
		return DAOUtil.getSingleResult(query);
	}
}
