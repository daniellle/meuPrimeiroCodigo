package br.com.ezvida.rst.dao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.UatEquipamento;
import fw.core.jpa.BaseDAO;

public class UatEquipamentoDAO extends BaseDAO<UatEquipamento, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoDAO.class);

	@Inject
	public UatEquipamentoDAO(EntityManager em) {
		super(em, UatEquipamento.class);
	}

	public List<UatEquipamento> listAllUatEquipamentoByIdUatAndAtivo(Long idUat) {
		LOGGER.debug("Listando todos os equipamentos da uat de ID {}", idUat);

		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT ue FROM UatEquipamento ue ");
		jpql.append(" JOIN ue.unidadeAtendimentoTrabalhador uat ");
		jpql.append(" JOIN FETCH ue.uatEquipamentoTipo uet ");
		jpql.append(" LEFT JOIN FETCH uet.uatEquipamentoArea uea ");
		jpql.append(" WHERE uat.id = :idUat ");
		jpql.append(" AND ue.dataExclusao IS NULL ");
		TypedQuery<UatEquipamento> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idUat", idUat);
		return query.getResultList();
	}

	public void desativar(Long id) {
		LOGGER.debug("Desativando UAT Equipamento de ID {}", id);
		
		Query query = getEm().createQuery("UPDATE UatEquipamento SET dataExclusao = :data "
	              + "WHERE id = :id");
        query.setParameter("id", id);
        query.setParameter("data", new Date());
        query.executeUpdate();
    }
}
