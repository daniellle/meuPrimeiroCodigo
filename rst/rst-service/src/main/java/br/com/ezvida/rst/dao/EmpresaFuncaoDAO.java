package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.FuncaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.EmpresaFuncao;
import fw.core.jpa.DAOUtil;

public class EmpresaFuncaoDAO extends BaseRstDAO<EmpresaFuncao, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaFuncaoDAO.class);

	@Inject
	public EmpresaFuncaoDAO(EntityManager em) {
		super(em, EmpresaFuncao.class);
	}

	public ListaPaginada<EmpresaFuncao> pesquisarPorPaginado(FuncaoFilter funcaoFilter) {
		LOGGER.debug("Buscando funcoes associados a empresa... ");

		ListaPaginada<EmpresaFuncao> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		getQueryPaginado(jpql, parametros, funcaoFilter, false);

		TypedQuery<EmpresaFuncao> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(funcaoFilter));

		if (funcaoFilter != null) {
			query.setFirstResult((funcaoFilter.getPagina() - 1) * funcaoFilter.getQuantidadeRegistro());
			query.setMaxResults(funcaoFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private Long getCountQueryPaginado(FuncaoFilter funcaoFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, funcaoFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros, FuncaoFilter funcaoFilter, boolean count) {

		if (count) {
			jpql.append(" select count(empresaFuncao.id) ");
			jpql.append(" from EmpresaFuncao empresaFuncao ");
			jpql.append(" inner join empresaFuncao.empresa empresa ");
			jpql.append(" inner join empresaFuncao.funcao funcao ");
		} else {
			jpql.append(" select empresaFuncao ");
			jpql.append(" from EmpresaFuncao empresaFuncao ");
			jpql.append(" inner join fetch empresaFuncao.empresa empresa ");
			jpql.append(" inner join fetch empresaFuncao.funcao funcao ");
		}


		jpql.append(" where  empresaFuncao.dataExclusao is null ");

		if (funcaoFilter != null) {

			boolean codigo = StringUtils.isNotBlank(funcaoFilter.getCodigo());
			boolean descricao = StringUtils.isNotBlank(funcaoFilter.getDescricao());
			boolean id = funcaoFilter.getIdEmpresa() != null;

			if (id) {
				jpql.append(" and empresa.id = :idEmpresa ");
				parametros.put("idEmpresa", funcaoFilter.getIdEmpresa());
			}

			if (descricao) {
				jpql.append(" and upper(funcao.descricao) like :descricao escape :sc ");
				parametros.put("sc", "\\");
				parametros.put("descricao", "%" + funcaoFilter.getDescricao().replace("%", "\\%").toUpperCase() + "%");
			}

			if (codigo) {
				jpql.append(" and upper(funcao.codigo) like :codigo escape :sc ");
				funcaoFilter.setCodigo(funcaoFilter.getCodigo().replace("%", "\\%").toUpperCase());
				parametros.put("sc", "\\");
				parametros.put("codigo", "%" + funcaoFilter.getCodigo().concat("%").toUpperCase());
			}

			if (!count) {
				jpql.append(" order by funcao.descricao ");
			}
		}

	}
	
	public Long verificandoExistenciaFuncao(Long idEmpresa, Long idFuncao) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select funcao.id from EmpresaFuncao empresaFuncao ");
		jpql.append(" left join  empresaFuncao.empresa empresa ");
		jpql.append(" left join  empresaFuncao.funcao funcao ");

		jpql.append(" where empresaFuncao.dataExclusao is null ");
		jpql.append(" and funcao.id = :idFuncao  ");
		jpql.append(" and empresa.id = :id  ");
		
		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);
		query.setParameter("id", idEmpresa);
		query.setParameter("idFuncao", idFuncao);

		return DAOUtil.getSingleResult(query);
	}
	
	public Long getIdEmpresaAssociada(Long idEmpresaFuncao) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select empresa.id from EmpresaFuncao empresaFuncao ");
		jpql.append(" left join  empresaFuncao.empresa empresa ");
		jpql.append(" where empresaFuncao.id = :idEmpresaFuncao  ");
		
		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);
		query.setParameter("idEmpresaFuncao", idEmpresaFuncao);

		return DAOUtil.getSingleResult(query);
	}
	
	public Long getIdFuncaoAssociada(Long idEmpresaFuncao) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select funcao.id from EmpresaFuncao empresaFuncao ");
		jpql.append(" left join  empresaFuncao.funcao funcao ");
		jpql.append(" where empresaFuncao.id = :idEmpresaFuncao  ");
		
		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);
		query.setParameter("idEmpresaFuncao", idEmpresaFuncao);

		return DAOUtil.getSingleResult(query);
	}
}
