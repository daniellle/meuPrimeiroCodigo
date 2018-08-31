package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.EnderecoEmpresa;
import fw.core.jpa.BaseDAO;

public class EnderecoEmpresaDAO extends BaseDAO<EnderecoEmpresa, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoEmpresaDAO.class);
	
	@Inject
	public EnderecoEmpresaDAO(EntityManager em) {
		super(em, EnderecoEmpresa.class);
	}

	public List<EnderecoEmpresa> buscarPorEmpresa(Long id) {
		LOGGER.debug("Buscando endereços por empresa...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select enderecoEmpresa from EnderecoEmpresa enderecoEmpresa ");
		sqlBuilder.append("inner join fetch enderecoEmpresa.empresa empresa ");
		sqlBuilder.append("inner join fetch enderecoEmpresa.endereco endereco ");
		sqlBuilder.append("inner join fetch endereco.municipio municipio ");
		sqlBuilder.append("inner join fetch municipio.estado estado ");
		sqlBuilder.append("where empresa.id = :idEmpresa and endereco.dataExclusao is null ");
		sqlBuilder.append(" order by endereco.descricao ");
		TypedQuery<EnderecoEmpresa> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idEmpresa", id);

		return query.getResultList();
	}

}
