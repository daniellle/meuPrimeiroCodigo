package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import br.com.ezvida.rst.model.UsuarioEntidade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.EmpresaTrabalhadorLotacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.model.EmpresaTrabalhadorLotacao;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class EmpresaTrabalhadorLotacaoDAO extends BaseDAO<EmpresaTrabalhadorLotacao, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorLotacaoDAO.class);

	@Inject
	public EmpresaTrabalhadorLotacaoDAO(EntityManager em) {
		super(em, EmpresaTrabalhadorLotacao.class);
	}

	public ListaPaginada<EmpresaTrabalhadorLotacao> pesquisarPaginado(
			EmpresaTrabalhadorLotacaoFilter empresaTrabalhadorLotacaoFilter) {
		LOGGER.debug("Buscando EmpresaTrabalhadorLotacao paginado... ");

		ListaPaginada<EmpresaTrabalhadorLotacao> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		getQueryPaginado(jpql, parametros, empresaTrabalhadorLotacaoFilter, false);

		TypedQuery<EmpresaTrabalhadorLotacao> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);

		Long quantidade = getCountQueryPaginado(empresaTrabalhadorLotacaoFilter);
		listaPaginada.setQuantidade(quantidade != null ? quantidade : 0l);

		if (empresaTrabalhadorLotacaoFilter != null) {
			query.setFirstResult((empresaTrabalhadorLotacaoFilter.getPagina() - 1)
					* empresaTrabalhadorLotacaoFilter.getQuantidadeRegistro());
			query.setMaxResults(empresaTrabalhadorLotacaoFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public Long getCountQueryPaginado(EmpresaTrabalhadorLotacaoFilter empresaTrabalhadorLotacaoFilter) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginado(jpql, parametros, empresaTrabalhadorLotacaoFilter, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginado(StringBuilder jpql, Map<String, Object> parametros,
			EmpresaTrabalhadorLotacaoFilter empresaFilter, boolean count) {

		if (count) {
			jpql.append(" select count(empresaTrabalhadorLotacao.id) ");
			jpql.append(" from EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao ");
			jpql.append(" inner join empresaTrabalhadorLotacao.empresaTrabalhador empresaTrabalhador ");
			jpql.append(" inner join empresaTrabalhadorLotacao.empresaLotacao empresaLotacao ");
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
		} else {
			jpql.append(" select empresaTrabalhadorLotacao ");
			jpql.append(" from EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao ");
			jpql.append(" inner join fetch empresaTrabalhadorLotacao.empresaTrabalhador empresaTrabalhador ");
			jpql.append(" inner join fetch empresaTrabalhadorLotacao.empresaLotacao empresaLotacao ");
			jpql.append(" inner join fetch empresaLotacao.unidadeObra unidadeObra ");
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
		}

		if (empresaFilter != null) {

			jpql.append(" where  empresaTrabalhadorLotacao.dataExclusao is null ");

			if (empresaFilter.getIdEmpresaTrabalhador() != null) {
				jpql.append(" and empresaTrabalhador.id = :idEmpresaTrabalhador ");
				parametros.put("idEmpresaTrabalhador", empresaFilter.getIdEmpresaTrabalhador());
			}

			if (!count) {
				jpql.append(" order by unidadeObra.descricao desc");
			}
		}

	}
	
	
	@SuppressWarnings("unchecked")
	public List<EmpresaTrabalhadorLotacao> buscarEmpresaTrabalhadorLotacaoNoPeriodo(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> parameters = Maps.newHashMap();
		sql.append(" select * from ");
		sql.append("emp_trab_lotacao emp_trab_lotacao ");
		sql.append("inner join EMP_TRABALHADOR empresaTrabalhador on emp_trab_lotacao.id_empr_trabalhador_fk=empresaTrabalhador.ID_EMP_TRABALHADOR ");
		sql.append("inner join emp_lotacao empresaLotacao on emp_trab_lotacao.id_emp_lotacao_fk=empresaLotacao.id_empresa_lotacao ");
		sql.append("where empresaTrabalhador.ID_EMP_TRABALHADOR=:idEmpresaTrabalhador  and  empresaLotacao.dt_exclusao is null and empresaTrabalhador.dt_exclusao is null and emp_trab_lotacao.dt_exclusao is null and ");
		if (empresaTrabalhadorLotacao.getId() != null) {
			sql.append(" emp_trab_lotacao.id_emp_trab_lotacao <> :idemp_trab_lotacao and ");
			parameters.put("idemp_trab_lotacao", empresaTrabalhadorLotacao.getId());
		}
		sql.append(" (date_trunc('day', to_timestamp(:dataAssociacaoAtual, 'YYYY-MM-dd')) ");
		sql.append(" between date_trunc('day', emp_trab_lotacao.dt_associacao) and ");
		sql.append(" date_trunc('day', emp_trab_lotacao.dt_desligamento) or ");

		sql.append(" date_trunc('day', to_timestamp(:dataDesligamentoAtual, 'YYYY-MM-dd')) ");
		sql.append(" between date_trunc('day', emp_trab_lotacao.dt_associacao) and ");
		sql.append(" date_trunc('day', emp_trab_lotacao.dt_desligamento) or ");

		sql.append(" (date_trunc('day', emp_trab_lotacao.dt_associacao) ");
		sql.append(" between date_trunc('day', to_timestamp(:dataAssociacaoAtual, 'YYYY-MM-dd')) and ");
		sql.append(" date_trunc('day', to_timestamp(:dataDesligamentoAtual, 'YYYY-MM-dd')) ");
		sql.append(" and date_trunc('day', emp_trab_lotacao.dt_desligamento) ");
		sql.append(" between date_trunc('day', to_timestamp(:dataAssociacaoAtual, 'YYYY-MM-dd')) and ");
		sql.append(" date_trunc('day', to_timestamp(:dataDesligamentoAtual, 'YYYY-MM-dd')))");
		sql.append(") ");

		Query query = criarConsultaNativa(sql.toString(), EmpresaTrabalhadorLotacao.class);
		DAOUtil.setParameterMap(query, parameters);
		query.setParameter("idEmpresaTrabalhador", empresaTrabalhadorLotacao.getEmpresaTrabalhador().getId());
		query.setParameter("dataAssociacaoAtual", empresaTrabalhadorLotacao.getDataAssociacao());
		query.setParameter("dataDesligamentoAtual", empresaTrabalhadorLotacao.getDataDesligamento() == null ? new Date() : empresaTrabalhadorLotacao.getDataDesligamento());

		return query.getResultList();
	}
	
	/**
	 * verifica se o trabalhador tem alguma lotação com data de desligamento superior a de demissao do trabalhador
	 * @param empresaTrabalhador
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EmpresaTrabalhadorLotacao> verificarDataEmpresaTrabalhadorLotacaoSuperiorDemissao(EmpresaTrabalhador empresaTrabalhador) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ");
		sql.append("emp_trab_lotacao emp_trab_lotacao ");
		sql.append("inner join EMP_TRABALHADOR empresaTrabalhador on emp_trab_lotacao.id_empr_trabalhador_fk = empresaTrabalhador.ID_EMP_TRABALHADOR ");
		sql.append("where  emp_trab_lotacao.dt_exclusao is  null  and ");
		sql.append(" empresaTrabalhador.id_emp_trabalhador = :idEmpresaTrabalhador and ");
		sql.append("( :dataDemissao < emp_trab_lotacao.dt_desligamento or ");
		sql.append(" :dataDemissao  <  emp_trab_lotacao.dt_associacao )");
		Query query = criarConsultaNativa(sql.toString(), EmpresaTrabalhadorLotacao.class);
		query.setParameter("idEmpresaTrabalhador", empresaTrabalhador.getId());
		query.setParameter("dataDemissao", empresaTrabalhador.getDataDemissao());
		return query.getResultList();
	}
	public List<EmpresaTrabalhadorLotacao> buscarEmpresaTrabalhadorLotacaoAtivo(EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select empresaTrabalhadorLotacao ");
		jpql.append(" from EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao ");
		jpql.append(" inner join fetch empresaTrabalhadorLotacao.empresaTrabalhador empresaTrabalhador ");
		jpql.append(" where empresaTrabalhadorLotacao.dataExclusao is null and empresaTrabalhadorLotacao.dataDesligamento is null ");
		jpql.append(" and  empresaTrabalhador.id = :idEmpresaTrabalhador");
		
		if(empresaTrabalhadorLotacao.getId() != null) {
			jpql.append(" and  empresaTrabalhadorLotacao.id != :idEmpresaTrabalhadorLotacao ");
		}
		
		if(empresaTrabalhadorLotacao.getDataDesligamento() != null) {
			jpql.append(" and  empresaTrabalhadorLotacao.dataAssociacao <= :idEmpresaTrabalhadorLotacaodtDes ");
		}
		
		TypedQuery<EmpresaTrabalhadorLotacao> query = criarConsultaPorTipo(jpql.toString(), EmpresaTrabalhadorLotacao.class);
		query.setParameter("idEmpresaTrabalhador", empresaTrabalhadorLotacao.getEmpresaTrabalhador().getId());
		
		if(empresaTrabalhadorLotacao.getId() != null) {
			query.setParameter("idEmpresaTrabalhadorLotacao", empresaTrabalhadorLotacao.getId());
		}
		
		if(empresaTrabalhadorLotacao.getDataDesligamento() != null) {
			query.setParameter("idEmpresaTrabalhadorLotacaodtDes", empresaTrabalhadorLotacao.getDataDesligamento());
		}

		return query.getResultList();
	}

	public boolean validarTrabalhador(String cpf){
		StringBuilder jpql = new StringBuilder();

		jpql.append(" select empresaTrabalhadorLotacao ");
		jpql.append(" from EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao ");
		jpql.append(" inner join fetch empresaTrabalhadorLotacao.empresaTrabalhador empresaTrabalhador ");
		jpql.append(" inner join fetch empresaTrabalhador.trabalhador  trabalhador");
		jpql.append(" inner join fetch empresaTrabalhadorLotacao.empresaLotacao empresaLotacao");
		jpql.append(" inner join fetch empresaLotacao.unidadeObra unidadeObra ");
		jpql.append(" inner join fetch unidadeObra.unidadeObraContratoUats unidadeObraContratoUat ");
		jpql.append(" where trabalhador.cpf = :cpf ");
		jpql.append(" and (unidadeObraContratoUat.dataContratoInicio is not null and unidadeObraContratoUat.dataContratoInicio <= :dataHoje) ");
		jpql.append(" and (unidadeObraContratoUat.dataContratoFim is not null and unidadeObraContratoUat.dataContratoFim > :dataHoje) ");
		jpql.append(" and (unidadeObraContratoUat.dataInativo is null) ");
		jpql.append(" and (empresaTrabalhadorLotacao.dataDesligamento is null) ");
		jpql.append(" and (empresaTrabalhadorLotacao.flagInativo = :flagInativo or empresaTrabalhadorLotacao.flagInativo is null)");

		TypedQuery<EmpresaTrabalhadorLotacao> query = criarConsultaPorTipo(jpql.toString(), EmpresaTrabalhadorLotacao.class);
		query.setParameter("cpf", cpf);
		query.setParameter("dataHoje", new Date(), TemporalType.TIMESTAMP);
		query.setParameter("flagInativo", "N".charAt(0));
		return (query.getResultList().size() != 0);
	}



	public Boolean validarGestor (String cpf){
		StringBuilder sql = new StringBuilder();

		sql.append(" select usuarioEntidade ");
		sql.append(" from UsuarioEntidade usuarioEntidade ");
		sql.append(" inner join fetch usuarioEntidade.empresa empresa ");
		sql.append(" inner join fetch empresa.unidadeObra unidadeObra ");
		sql.append(" inner join fetch unidadeObra.unidadeObraContratoUats unidadeObraContratoUat ");
		sql.append(" where usuarioEntidade.cpf = :cpf ");
		sql.append(" and (unidadeObraContratoUat.dataContratoInicio is not null and unidadeObraContratoUat.dataContratoInicio <= :dataHoje) ");
		sql.append(" and (unidadeObraContratoUat.dataContratoFim is not null and unidadeObraContratoUat.dataContratoFim > :dataHoje) ");
		sql.append(" and (unidadeObraContratoUat.dataInativo is null) ");
		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(sql.toString(), UsuarioEntidade.class);
		query.setParameter("cpf", cpf);
		query.setParameter("dataHoje", new Date(), TemporalType.TIMESTAMP);
		return (query.getResultList().size() != 0);
	}

}
