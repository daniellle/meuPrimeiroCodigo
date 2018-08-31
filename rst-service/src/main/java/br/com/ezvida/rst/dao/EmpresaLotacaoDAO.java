package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.EmpresaLotacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.EmpresaLotacao;
import fw.core.jpa.DAOUtil;

public class EmpresaLotacaoDAO extends BaseRstDAO<EmpresaLotacao, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaLotacaoDAO.class);

	@Inject
	public EmpresaLotacaoDAO(EntityManager em) {
		super(em, EmpresaLotacao.class);
	}

	public ListaPaginada<EmpresaLotacao> pesquisarPorEmpresa(EmpresaLotacaoFilter empresaLotacaoFilter) {
		LOGGER.debug("Buscando lotacoes associados a empresa... ");

		ListaPaginada<EmpresaLotacao> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		getQueryPaginado(jpql, parametros, empresaLotacaoFilter, false);

		TypedQuery<EmpresaLotacao> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(empresaLotacaoFilter));

		if (empresaLotacaoFilter != null) {
			query.setFirstResult((empresaLotacaoFilter.getPagina() - 1) * empresaLotacaoFilter.getQuantidadeRegistro());
			query.setMaxResults(empresaLotacaoFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public EmpresaLotacao pesquisarEmpresaLotacaoVinculada(EmpresaLotacao empresaLotacao) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryLotacaoExistente(jpql, parametros, empresaLotacao);
		TypedQuery<EmpresaLotacao> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryLotacaoExistente(StringBuilder jpql, Map<String, Object> parametros,
			EmpresaLotacao empresaLotacao) {
		jpql.append(" select empresaLotacao ");
		jpql.append(" from EmpresaLotacao empresaLotacao ");
		jpql.append(" inner join fetch empresaLotacao.empresaSetor empresaSetor ");
		jpql.append(" inner join fetch empresaLotacao.empresaCbo empresaCbo ");
		jpql.append(" inner join fetch empresaLotacao.empresaFuncao empresaFuncao ");
		jpql.append(" inner join fetch empresaLotacao.empresaJornada empresaJornada ");
		jpql.append(" inner join fetch empresaLotacao.unidadeObra unidadeObra ");
		jpql.append(" where  empresaLotacao.dataExclusao is null ");

		boolean idEmpresaSetor = empresaLotacao.getEmpresaSetor().getId() != null;
		boolean idEmpresaCbo = empresaLotacao.getEmpresaCbo().getId() != null;
		boolean idEmpresaFuncao = empresaLotacao.getEmpresaFuncao().getId() != null;
		boolean idEmpresaJornada = empresaLotacao.getEmpresaJornada().getId() != null;
		boolean idUnidadeObra = empresaLotacao.getUnidadeObra().getId() != null;

		if (idEmpresaSetor) {
			jpql.append(" and empresaSetor.id = :idEmpresaSetor ");
			parametros.put("idEmpresaSetor", empresaLotacao.getEmpresaSetor().getId());
		}

		if (idEmpresaCbo) {
			jpql.append(" and empresaCbo.id = :idEmpresaCbo ");
			parametros.put("idEmpresaCbo", empresaLotacao.getEmpresaCbo().getId());
		}

		if (idEmpresaFuncao) {
			jpql.append(" and empresaFuncao.id = :idEmpresaFuncao ");
			parametros.put("idEmpresaFuncao", empresaLotacao.getEmpresaFuncao().getId());
		}

		if (idEmpresaJornada) {
			jpql.append(" and empresaJornada.id = :idEmpresaJornada ");
			parametros.put("idEmpresaJornada", empresaLotacao.getEmpresaJornada().getId());
		}

		if (idUnidadeObra) {
			jpql.append(" and unidadeObra.id = :idUnidadeObra ");
			parametros.put("idUnidadeObra", empresaLotacao.getUnidadeObra().getId());
		}
	}

	private Long getCountQueryPaginado(EmpresaLotacaoFilter empresaLotacaoFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();

		getQueryPaginado(jpql, parametros, empresaLotacaoFilter, true);

		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			EmpresaLotacaoFilter empresaLotacaoFilter, boolean count) {

		if (count) {
			jpql.append(" select count(empresaLotacao.id) ");
			getQueryCount(jpql);
		} else {
			jpql.append(" select empresaLotacao ");
			jpql.append(" from EmpresaLotacao empresaLotacao ");
			jpql.append(" inner join fetch empresaLotacao.empresaFuncao empresaFuncao ");
			jpql.append(" inner join fetch empresaFuncao.funcao funcao ");
			jpql.append(" inner join fetch empresaFuncao.empresa empresaFuncaoEmpresa ");
			jpql.append(" inner join fetch empresaLotacao.empresaSetor empresaSetor ");
			jpql.append(" inner join fetch empresaSetor.setor setor ");
			jpql.append(" inner join fetch empresaSetor.empresa empresaSetorEmpresa ");
			jpql.append(" inner join fetch empresaLotacao.empresaJornada empresaJornada ");
			jpql.append(" inner join fetch empresaJornada.jornada jornada ");
			jpql.append(" inner join fetch empresaJornada.empresa empresaJornadaEmpresa ");
			jpql.append(" inner join fetch empresaLotacao.empresaCbo empresaCbo ");
			jpql.append(" inner join fetch empresaCbo.cbo cbo ");
			jpql.append(" inner join fetch empresaCbo.empresa empresaCboEmpresa ");
			jpql.append(" inner join fetch empresaLotacao.unidadeObra unidadeObra ");
		}

		if (empresaLotacaoFilter != null) {

			jpql.append(" where  empresaLotacao.dataExclusao is null ");

			if (empresaLotacaoFilter.getIdEmpresa() != null) {
				getAndEmpresaLotacao(jpql);
				parametros.put("idEmpresa", empresaLotacaoFilter.getIdEmpresa());
			}

			if (empresaLotacaoFilter.getIdUnidadeObra() != null) {
				jpql.append(" and unidadeObra.id = :idUnidadeObra ");
				parametros.put("idUnidadeObra", empresaLotacaoFilter.getIdUnidadeObra());
			}
			
			if (empresaLotacaoFilter.getIdSetor() != null) {
				jpql.append(" and empresaSetor.id = :idSetor ");
				parametros.put("idSetor", empresaLotacaoFilter.getIdSetor());
			}
			
			if (empresaLotacaoFilter.getIdFuncao() != null) {
				jpql.append(" and empresaFuncao.id = :idFuncao ");
				parametros.put("idFuncao", empresaLotacaoFilter.getIdFuncao());
			}
			
			if (empresaLotacaoFilter.getIdJornada() != null) {
				jpql.append(" and empresaJornada.id = :idJornada ");
				parametros.put("idJornada", empresaLotacaoFilter.getIdJornada());
			}
			
			if (empresaLotacaoFilter.getIdCargo() != null) {
				jpql.append(" and empresaCbo.id = :idCargo ");
				parametros.put("idCargo", empresaLotacaoFilter.getIdCargo());
			}

			if (!count) {
				jpql.append(" order by setor.sigla, cbo.descricao, funcao.descricao, jornada.descricao ");
			}
		}

	}

	private void getQueryCount(StringBuilder jpql) {
		jpql.append(" from EmpresaLotacao empresaLotacao ");
		jpql.append(" inner join empresaLotacao.empresaFuncao empresaFuncao ");
		jpql.append(" inner join empresaFuncao.funcao funcao ");
		jpql.append(" inner join empresaFuncao.empresa empresaFuncaoEmpresa ");
		jpql.append(" inner join empresaLotacao.empresaSetor empresaSetor ");
		jpql.append(" inner join empresaSetor.setor setor ");
		jpql.append(" inner join empresaSetor.empresa empresaSetorEmpresa ");
		jpql.append(" inner join empresaLotacao.empresaJornada empresaJornada ");
		jpql.append(" inner join empresaJornada.jornada jornada ");
		jpql.append(" inner join empresaJornada.empresa empresaJornadaEmpresa ");
		jpql.append(" inner join empresaLotacao.empresaCbo empresaCbo ");
		jpql.append(" inner join empresaCbo.cbo cbo ");
		jpql.append(" inner join empresaCbo.empresa empresaCboEmpresa ");
		jpql.append(" inner join empresaLotacao.unidadeObra unidadeObra ");
	}

	private void getAndEmpresaLotacao(StringBuilder jpql) {
		jpql.append(" and empresaFuncaoEmpresa.id = :idEmpresa ");
		jpql.append(" and empresaSetorEmpresa.id = :idEmpresa ");
		jpql.append(" and empresaJornadaEmpresa.id = :idEmpresa ");
		jpql.append(" and empresaCboEmpresa.id = :idEmpresa ");
	}

	public void desativar(Long idEmpresa, List<Long> ids) {
		StringBuilder jpql = new StringBuilder();

		jpql.append("update ").append(getTipoClasse().getSimpleName()).append(" entity set dataExclusao = :dataAtual ");
		jpql.append(" where entity.id in (").append(getQueryBuscarEmpresaLotacaoPorEmpresa(!ids.isEmpty())).append(")");

		Query query = criarConsulta(jpql.toString());
		query.setParameter("dataAtual", new Date());
		query.setParameter("id", idEmpresa);

		if (!ids.isEmpty()) {
			query.setParameter("ids", ids);
		}

		int quantidade = query.executeUpdate();
		LOGGER.debug("Foi desativado um total de {} em {}", quantidade, getTipoClasse().getSimpleName());
	}

	private StringBuilder getQueryBuscarEmpresaLotacaoPorEmpresa(boolean existeIds) {
		StringBuilder jpql = new StringBuilder();

		jpql.append(" select empresaLotacao.id ");
		getQueryCount(jpql);
		jpql.append(" where empresaLotacao.dataExclusao is null ");
		getAndEmpresaLotacao(jpql);
		if (existeIds) {
			jpql.append(" and entity.id not in (:ids) ");
		}

		return jpql;
	}
	
	public Long getIdEmpresaAssociada(Long idEmpresaLotacao) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select empresaLotacao.empresaFuncao.empresa.id from EmpresaLotacao empresaLotacao ");
		jpql.append(" where empresaLotacao.id = :idEmpresaLotacao  ");
		
		Query query = criarConsulta(jpql.toString());
		query.setParameter("idEmpresaLotacao", idEmpresaLotacao);

		return DAOUtil.getSingleResult(query);
	}
}
