package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.TelefoneEmpresa;
import fw.core.jpa.BaseDAO;

public class TelefoneEmpresaDAO extends BaseDAO<TelefoneEmpresa, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneEmpresaDAO.class);
	
	@Inject
	public TelefoneEmpresaDAO(EntityManager em) {
		super(em, TelefoneEmpresa.class);
	}

	public List<TelefoneEmpresa> buscarPorEmpresa(Long id) {
		LOGGER.debug("Buscando telefones ativos por empresa...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select telefoneEmpresa from TelefoneEmpresa telefoneEmpresa ");
		sqlBuilder.append(" inner join fetch telefoneEmpresa.empresa empresa ");
		sqlBuilder.append(" inner join fetch telefoneEmpresa.telefone telefone ");
		sqlBuilder.append(" where telefone.dataExclusao is null and empresa.id = :idEmpresa ");
		sqlBuilder.append(" order by telefone.numero ");
		TypedQuery<TelefoneEmpresa> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idEmpresa", id);

		return query.getResultList();
	}

}
