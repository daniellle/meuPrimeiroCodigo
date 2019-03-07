package br.com.ezvida.rst.dao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.UatVeiculo;
import fw.core.jpa.BaseDAO;

public class UatVeiculoDAO extends BaseDAO<UatVeiculo, Long> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoDAO.class);

	@Inject
	public UatVeiculoDAO(EntityManager em) {
		super(em, UatVeiculo.class);
	}

	public List<UatVeiculo> listAllUatVeiculosByIdUatAndAtivo(Long idUat) {
		LOGGER.debug("Listando todos os veiculos da uat de ID {}", idUat);
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT uv FROM UatVeiculo uv ");
		jpql.append(" JOIN uv.unidadeAtendimentoTrabalhador uat ");
		jpql.append(" JOIN FETCH uv.uatVeiculoTipo uvt ");
		jpql.append(" LEFT JOIN FETCH uv.unidadeVeiculoTipoAtendimento uvtp ");
		jpql.append(" WHERE uat.id = :idUat ");
		jpql.append(" AND uv.dataExclusao IS NULL ");
		TypedQuery<UatVeiculo> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idUat", idUat);        
		return query.getResultList();
	}
	
	public void desativar(Long id) {
		LOGGER.debug("Desativando UAT Veículo de ID {}", id);
		
		Query query = getEm().createQuery("UPDATE UatVeiculo SET dataExclusao = :data "
	              + "WHERE id = :id");
        query.setParameter("id", id);
        query.setParameter("data", new Date());
        query.executeUpdate();
    }
}
