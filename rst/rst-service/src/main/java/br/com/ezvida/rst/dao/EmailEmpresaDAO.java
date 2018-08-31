package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EmailEmpresa;
import fw.core.jpa.BaseDAO;

public class EmailEmpresaDAO extends BaseDAO<EmailEmpresa, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailEmpresaDAO.class);
	
	@Inject
	public EmailEmpresaDAO(EntityManager em) {
		super(em, EmailEmpresa.class);
	}

	public List<EmailEmpresa> buscarPorEmpresa(Long id) {
		LOGGER.debug("Buscando email por empresa...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select emailEmpresa from EmailEmpresa emailEmpresa ");
		sqlBuilder.append("inner join fetch emailEmpresa.empresa empresa ");
		sqlBuilder.append("inner join fetch emailEmpresa.email email ");
		sqlBuilder.append("where email.dataExclusao is null and empresa.id = :idEmpresa ");
		sqlBuilder.append(" order by email.descricao ");
		TypedQuery<EmailEmpresa> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idEmpresa", id);

		return query.getResultList();
	}

}
