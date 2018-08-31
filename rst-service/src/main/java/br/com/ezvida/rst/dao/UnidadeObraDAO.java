package br.com.ezvida.rst.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.UnidadeObra;
import fw.core.jpa.DAOUtil;

public class UnidadeObraDAO extends BaseRstDAO<UnidadeObra, Long> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeObraDAO.class);

	@Inject
	public UnidadeObraDAO(EntityManager em) {
		super(em, UnidadeObra.class, "descricao");
	}
	
	public List<UnidadeObra> buscarPorEmpresa(Long id) {
		LOGGER.debug("Buscando unidades ativos por empresa...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select unidadeObra from UnidadeObra unidadeObra ");
		sqlBuilder.append(" inner join fetch unidadeObra.empresa empresa ");
		sqlBuilder.append(" where unidadeObra.dataExclusao is null and empresa.id = :idEmpresa ");
		sqlBuilder.append(" order by unidadeObra.descricao ");
		TypedQuery<UnidadeObra> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("idEmpresa", id);

		return query.getResultList();
	}
	
	public UnidadeObra buscarPorCei(String cei) {
		LOGGER.debug("Buscando unidade obra ativos por cei...");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select unidadeObra from UnidadeObra unidadeObra ");
		sqlBuilder.append(" where unidadeObra.dataExclusao is null ");
		sqlBuilder.append(" and unidadeObra.cei = :cei ");
		TypedQuery<UnidadeObra> query = criarConsultaPorTipo(sqlBuilder.toString());
		query.setParameter("cei", cei);
		return DAOUtil.getSingleResult(query);
	}
}
