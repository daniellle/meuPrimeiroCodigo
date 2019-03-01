package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.UatVeiculo;
import fw.core.jpa.BaseDAO;

public class UatVeiculoDAO extends BaseDAO<UatVeiculo, Long> {

	@Inject
	public UatVeiculoDAO(EntityManager em) {
		super(em, UatVeiculo.class);
	}

	public List<UatVeiculo> listAllUatVeiculosByIdUat(Long idUat) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT uv FROM UatVeiculo uv ");
		jpql.append(" JOIN uv.unidadeAtendimentoTrabalhador uat ");
		jpql.append(" JOIN FETCH uv.uatVeiculoTipo uvt ");
		jpql.append(" LEFT JOIN FETCH uv.unidadeVeiculoTipoAtendimento uvtp ");
		jpql.append(" WHERE uat.id = :idUat");
		TypedQuery<UatVeiculo> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idUat", idUat);        
		return query.getResultList();
	}
}
