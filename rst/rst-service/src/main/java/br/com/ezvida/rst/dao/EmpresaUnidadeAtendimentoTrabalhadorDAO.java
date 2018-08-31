package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EmpresaUnidadeAtendimentoTrabalhador;

public class EmpresaUnidadeAtendimentoTrabalhadorDAO extends BaseRstDAO<EmpresaUnidadeAtendimentoTrabalhador, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaUnidadeAtendimentoTrabalhadorDAO.class);
	
	@Inject
	public EmpresaUnidadeAtendimentoTrabalhadorDAO(EntityManager em) {
		super(em, EmpresaUnidadeAtendimentoTrabalhador.class);
	}

	public List<EmpresaUnidadeAtendimentoTrabalhador> buscarPorEmpresa(Long id) {
		LOGGER.debug("Buscando unidades de atendimento ao trabalhador ativos por empresa...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select empresaUats from EmpresaUnidadeAtendimentoTrabalhador empresaUats ");
		sqlBuilder.append(" inner join fetch empresaUats.empresa empresa ");
		sqlBuilder.append(" inner join fetch empresaUats.unidadeAtendimentoTrabalhador uats ");
		sqlBuilder.append(" where empresaUats.dataExclusao is null and empresa.id = :idEmpresa ");
		sqlBuilder.append(" order by uats.cnpj ");
		TypedQuery<EmpresaUnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idEmpresa", id);

		return query.getResultList();
	}
	
	public List<EmpresaUnidadeAtendimentoTrabalhador> buscarPorUat(Long id) {
		LOGGER.debug("Buscando empresa ativos por unidades de atendimento ao trabalhador ...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select empresaUats from EmpresaUnidadeAtendimentoTrabalhador empresaUats ");
		sqlBuilder.append(" inner join fetch empresaUats.empresa empresa ");
		sqlBuilder.append(" inner join fetch empresaUats.unidadeAtendimentoTrabalhador uats ");
		sqlBuilder.append(" where empresaUats.dataExclusao is null and uats.id = :iduats ");
		sqlBuilder.append(" order by uats.cnpj ");
		TypedQuery<EmpresaUnidadeAtendimentoTrabalhador> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("iduats", id);

		return query.getResultList();
	}


}
