package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UsuarioEntidadeFilter;
import br.com.ezvida.rst.model.UsuarioEntidade;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class UsuarioEntidadeDAO extends BaseDAO<UsuarioEntidade, Long> {

	@Inject
	public UsuarioEntidadeDAO(EntityManager em) {
		super(em, UsuarioEntidade.class);
	}

	public List<UsuarioEntidade> pesquisarPorCPF(String cpf, boolean doFetch) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("select usuarioEntidade from UsuarioEntidade usuarioEntidade ");

		if (doFetch) {
			jpql.append(" left join fetch usuarioEntidade.empresa empresa ");
			jpql.append(" left join fetch usuarioEntidade.empresaProfissionalSaude empresaProfissionalSaude ");
			jpql.append(" left join fetch usuarioEntidade.departamentoRegional departamentoRegional ");
			jpql.append(" left join fetch usuarioEntidade.parceiro parceiro ");
			jpql.append(" left join fetch usuarioEntidade.redeCredenciada redeCredenciada ");
			jpql.append(" left join fetch usuarioEntidade.sindicato sindicato ");
		}

		jpql.append(" where usuarioEntidade.cpf = :cpf and usuarioEntidade.dataExclusao is null ");
		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return query.getResultList();
	}

	public ListaPaginada<UsuarioEntidade> pesquisarEmpresa(UsuarioEntidadeFilter filtro) {

		ListaPaginada<UsuarioEntidade> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		queryPAginadoEmpresa(jpql, parametros, filtro, false);
		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		listaPaginada.setQuantidade(getCountQueryPaginadoEmpresa(filtro));
		query.setFirstResult((filtro.getPagina() - 1) * filtro.getQuantidadeRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistro());

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public List<UsuarioEntidade> pesquisarTodasEmpresasAssociadas(String cpf) {

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		jpql.append("select distinct usuarioEntidade from UsuarioEntidade usuarioEntidade ");
		jpql.append("left join fetch usuarioEntidade.empresa empresa ");
		jpql.append("left join fetch usuarioEntidade.empresaProfissionalSaude empresaProfissionalSaude ");
		jpql.append(" where usuarioEntidade.dataExclusao is null ");

		if (cpf != null) {
			jpql.append(" and usuarioEntidade.cpf = :cpf");
			parametros.put("cpf", cpf);
		}

		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return query.getResultList();
	}

	public List<UsuarioEntidade> pesquisarTodosDepartamentosAssociadas(String cpf) {

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();

		jpql.append("select distinct usuarioEntidade from UsuarioEntidade usuarioEntidade ");
		jpql.append("inner join fetch usuarioEntidade.departamentoRegional departamento ");
		jpql.append(" where usuarioEntidade.dataExclusao is null ");

		if (cpf != null) {
			jpql.append(" and usuarioEntidade.cpf = :cpf");
			parametros.put("cpf", cpf);
		}

		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		return query.getResultList();
	}

	private Long getCountQueryPaginadoEmpresa(UsuarioEntidadeFilter filtro) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		queryPAginadoEmpresa(jpql, parametros, filtro, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<UsuarioEntidade> pesquisarSindicato(UsuarioEntidadeFilter filtro) {

		ListaPaginada<UsuarioEntidade> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		queryPAginadoSindicato(jpql, parametros, filtro, false);
		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		listaPaginada.setQuantidade(getCountQueryPaginadoSindicato(filtro));
		query.setFirstResult((filtro.getPagina() - 1) * filtro.getQuantidadeRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistro());

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private Long getCountQueryPaginadoSindicato(UsuarioEntidadeFilter filtro) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		queryPAginadoSindicato(jpql, parametros, filtro, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	public ListaPaginada<UsuarioEntidade> pesquisarDepartamentoRegional(UsuarioEntidadeFilter filtro) {

		ListaPaginada<UsuarioEntidade> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		queryPAginadoDepartamentoRegional(jpql, parametros, filtro, false);
		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		listaPaginada.setQuantidade(getCountQueryPaginadoDepartamentoRegional(filtro));
		query.setFirstResult((filtro.getPagina() - 1) * filtro.getQuantidadeRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistro());

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	private Long getCountQueryPaginadoDepartamentoRegional(UsuarioEntidadeFilter filtro) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		queryPAginadoDepartamentoRegional(jpql, parametros, filtro, true);
		Query query = criarConsulta(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void queryPAginadoEmpresa(StringBuilder jpql, Map<String, Object> parametros, UsuarioEntidadeFilter filtro,
			boolean count) {

		boolean cpf = StringUtils.isNotBlank(filtro.getCpf());
		boolean razaoSocial = StringUtils.isNotBlank(filtro.getRazaoSocial());
		boolean nomeFantasia = StringUtils.isNotBlank(filtro.getNomeFantasia());
		boolean cnpj = StringUtils.isNotBlank(filtro.getCnpj());

		if (count) {
			jpql.append("select  count(distinct usuarioEntidade.id)  from UsuarioEntidade usuarioEntidade ");
			jpql.append("left join  usuarioEntidade.empresa empresa ");
			jpql.append("left join  usuarioEntidade.empresaProfissionalSaude empresaProfissionalSaude ");
		} else {
			jpql.append("select distinct usuarioEntidade from UsuarioEntidade usuarioEntidade ");
			jpql.append("left join fetch usuarioEntidade.empresa empresa ");
			jpql.append("left join fetch usuarioEntidade.empresaProfissionalSaude empresaProfissionalSaude ");
		}

		jpql.append(" where ");
		jpql.append(" usuarioEntidade.dataExclusao is  null ");

		if (cpf) {
			jpql.append(" and usuarioEntidade.cpf = :cpf");
			parametros.put("cpf", filtro.getCpf());
		}

		if (razaoSocial) {

			jpql.append(" and UPPER(empresa.razaoSocial) like :razaoSocial escape :sc ");
			jpql.append(" and UPPER(empresaProfissionalSaude.razaoSocial) like :razaoSocial escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("razaoSocial", "%" + filtro.getRazaoSocial().replace("%", "\\%").toUpperCase() + "%");
		}

		if (nomeFantasia) {

			jpql.append(" and UPPER(empresa.nomeFantasia) like :nomeFantasia escape :sc ");
			jpql.append(" and UPPER(empresaProfissionalSaude.nomeFantasia) like :nomeFantasia escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("nomeFantasia", "%" + filtro.getNomeFantasia().replace("%", "\\%").toUpperCase() + "%");
		}

		if (cnpj) {
			jpql.append(" and empresa.cnpj = :cnpj");
			jpql.append(" and empresaProfissionalSaude.cnpj = :cnpj");
			parametros.put("cnpj", filtro.getCnpj());
		}

		if (!count) {
			jpql.append(" order by empresa.razaoSocial, empresaProfissionalSaude.razaoSocial");
		}

	}

	private void queryPAginadoSindicato(StringBuilder jpql, Map<String, Object> parametros,
			UsuarioEntidadeFilter filtro, boolean count) {

		boolean cpf = StringUtils.isNotBlank(filtro.getCpf());
		boolean razaoSocial = StringUtils.isNotBlank(filtro.getRazaoSocial());
		boolean nomeFantasia = StringUtils.isNotBlank(filtro.getNomeFantasia());
		boolean cnpj = StringUtils.isNotBlank(filtro.getCnpj());

		if (count) {
			jpql.append("select  count(distinct usuarioEntidade.id)  from UsuarioEntidade usuarioEntidade ");
			jpql.append(" inner join  usuarioEntidade.sindicato sindicato ");
		} else {
			jpql.append("select distinct usuarioEntidade from UsuarioEntidade usuarioEntidade ");
			jpql.append(" inner join fetch usuarioEntidade.sindicato sindicato ");
		}

		jpql.append(" where ");
		jpql.append(" usuarioEntidade.dataExclusao is  null ");

		if (cpf) {
			jpql.append(" and usuarioEntidade.cpf = :cpf ");
			parametros.put("cpf", filtro.getCpf());
		}

		if (razaoSocial) {
			jpql.append(" and UPPER(sindicato.razaoSocial) like :razaoSocial escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("razaoSocial", "%" + filtro.getRazaoSocial().replace("%", "\\%").toUpperCase() + "%");
		}

		if (nomeFantasia) {

			jpql.append(" and UPPER(sindicato.nomeFantasia) like :nomeFantasia escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("nomeFantasia", "%" + filtro.getNomeFantasia().replace("%", "\\%").toUpperCase() + "%");
		}

		if (cnpj) {
			jpql.append(" and  sindicato.cnpj = :cnpj");
			parametros.put("cnpj", filtro.getCnpj().concat("%"));
		}

		if (!count) {
			jpql.append(" order by sindicato.razaoSocial ");
		}
	}

	private void queryPAginadoDepartamentoRegional(StringBuilder jpql, Map<String, Object> parametros,
			UsuarioEntidadeFilter filtro, boolean count) {

		boolean cpf = StringUtils.isNotBlank(filtro.getCpf());
		boolean razaoSocial = StringUtils.isNotBlank(filtro.getRazaoSocial());
		boolean cnpj = StringUtils.isNotBlank(filtro.getCnpj());
		boolean estado = filtro.getIdEstado() != null && filtro.getIdEstado() != 0;

		if (count) {
			jpql.append("select  count(distinct usuarioEntidade.id)  from UsuarioEntidade usuarioEntidade ");
			jpql.append(" inner join  usuarioEntidade.departamentoRegional departamentoRegional ");
			jpql.append("left join  departamentoRegional.listaEndDepRegional listaEnd ");
			jpql.append("left join  listaEnd.endereco endereco ");
			jpql.append("left join  endereco.municipio municipio ");
			jpql.append("left join  municipio.estado estado ");
		} else {
			jpql.append("select distinct usuarioEntidade from UsuarioEntidade usuarioEntidade ");
			jpql.append(" inner join fetch  usuarioEntidade.departamentoRegional departamentoRegional ");
			jpql.append("left join fetch departamentoRegional.listaEndDepRegional listaEnd ");
			jpql.append("left join fetch listaEnd.endereco endereco ");
			jpql.append("left join fetch endereco.municipio municipio ");
			jpql.append("left join fetch municipio.estado estado ");
		}

		jpql.append(" where ");
		jpql.append("  usuarioEntidade.dataExclusao is  null  ");

		if (cpf) {
			jpql.append(" and usuarioEntidade.cpf = :cpf ");
			parametros.put("cpf", filtro.getCpf());
		}

		if (razaoSocial) {
			jpql.append(" and UPPER(departamentoRegional.razaoSocial) like :razaoSocial escape :sc ");
			parametros.put("sc", "\\");
			parametros.put("razaoSocial", "%" + filtro.getRazaoSocial().replace("%", "\\%").toUpperCase() + "%");
		}

		if (cnpj) {
			jpql.append(" and  departamentoRegional.cnpj = :cnpj");
			parametros.put("cnpj", filtro.getCnpj().concat("%"));
		}

		if (estado) {
			jpql.append(" and estado.id = :idEstado");
			parametros.put("idEstado", filtro.getIdEstado());
		}

		if (!count) {
			jpql.append(" order by departamentoRegional.razaoSocial ");
		}
	}

	private StringBuilder consultaExistenciaUsuarioEntidadeEmpresa(StringBuilder jpql) {
		jpql.append("select empresa.id from UsuarioEntidade usuarioEntidade ");
		jpql.append(" left join usuarioEntidade.empresa empresa ");
		jpql.append(" where usuarioEntidade.dataExclusao is null ");
		jpql.append(" and usuarioEntidade.cpf = :cpf  ");
		jpql.append(" and empresa.id = :idEmpresa  ");
		return jpql;
	}

	private StringBuilder consultaExistenciaUsuarioEntidadeEmpresaProfissionalSaude(StringBuilder jpql) {
		jpql.append("select empresaProfissionalSaude.id from UsuarioEntidade usuarioEntidade ");
		jpql.append(" left join usuarioEntidade.empresaProfissionalSaude empresaProfissionalSaude ");
		jpql.append(" where usuarioEntidade.dataExclusao is null ");
		jpql.append(" and usuarioEntidade.cpf = :cpf  ");
		jpql.append(" and empresaProfissionalSaude.id = :idEmpresa  ");
		return jpql;
	}

	private StringBuilder consultaExistenciaUsuarioEntidadeSindicato(StringBuilder jpql) {
		jpql.append("select sindicato.id from UsuarioEntidade usuarioEntidade ");
		jpql.append(" left join usuarioEntidade.sindicato sindicato ");
		jpql.append(" where usuarioEntidade.dataExclusao is null ");
		jpql.append(" and usuarioEntidade.cpf = :cpf  ");
		jpql.append(" and sindicato.id = :idSindicato  ");
		return jpql;
	}

	private StringBuilder consultaExistenciaUsuarioEntidadeDepartamento(StringBuilder jpql) {
		jpql.append("select departamento.id from UsuarioEntidade usuarioEntidade ");
		jpql.append(" left join usuarioEntidade.departamentoRegional departamento ");
		jpql.append(" where usuarioEntidade.dataExclusao is null ");
		jpql.append(" and usuarioEntidade.cpf = :cpf  ");
		jpql.append(" and departamento.id = :idDepartamento  ");
		return jpql;
	}

	public Long verificandoExistenciaUsuarioEntidade(UsuarioEntidade usuarioEntidade) {
		StringBuilder jpql = new StringBuilder();
		if (usuarioEntidade.getEmpresa() != null && usuarioEntidade.getEmpresa().getId() != null) {
			consultaExistenciaUsuarioEntidadeEmpresa(jpql);
		}

		if (usuarioEntidade.getEmpresaProfissionalSaude() != null && usuarioEntidade.getEmpresaProfissionalSaude().getId() != null) {
			consultaExistenciaUsuarioEntidadeEmpresaProfissionalSaude(jpql);
		}

		if (usuarioEntidade.getSindicato() != null && usuarioEntidade.getSindicato().getId() != null) {
			consultaExistenciaUsuarioEntidadeSindicato(jpql);
		}

		if (usuarioEntidade.getDepartamentoRegional() != null
				&& usuarioEntidade.getDepartamentoRegional().getId() != null) {
			consultaExistenciaUsuarioEntidadeDepartamento(jpql);
		}

		TypedQuery<Long> query = getEm().createQuery(jpql.toString(), Long.class);

		query.setParameter("cpf", usuarioEntidade.getCpf());

		addFiltroIds(usuarioEntidade, query);

		return DAOUtil.getSingleResult(query);
	}

	private void addFiltroIds(UsuarioEntidade usuarioEntidade, TypedQuery<Long> query) {
		if (usuarioEntidade.getEmpresa() != null && usuarioEntidade.getEmpresa().getId() != null) {
			query.setParameter("idEmpresa", usuarioEntidade.getEmpresa().getId());
		}

		if (usuarioEntidade.getEmpresaProfissionalSaude() != null && usuarioEntidade.getEmpresaProfissionalSaude().getId() != null) {
			query.setParameter("idEmpresa", usuarioEntidade.getEmpresaProfissionalSaude().getId());
		}

		if (usuarioEntidade.getSindicato() != null && usuarioEntidade.getSindicato().getId() != null) {
			query.setParameter("idSindicato", usuarioEntidade.getSindicato().getId());
		}

		if (usuarioEntidade.getDepartamentoRegional() != null
				&& usuarioEntidade.getDepartamentoRegional().getId() != null) {
			query.setParameter("idDepartamento", usuarioEntidade.getDepartamentoRegional().getId());
		}
	}

	public List<UsuarioEntidade> pesquisarPorDepartRegionalEmEmpresa(String cpf, DadosFilter dadosFilter) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select distinct ue from UsuarioEntidade ue ");
		jpql.append(" inner join ue.empresa empresa ");
		jpql.append(" inner join empresa.empresaUats empresaUats ");
		jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador uat ");
		jpql.append(" inner join uat.departamentoRegional dr ");
		jpql.append(" where ue.cpf = :cpf and dr.id in (:idDepartamentos) ");
		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);
		query.setParameter("idDepartamentos", dadosFilter.getIdsDepartamentoRegional());

		return query.getResultList();
	}

	public List<UsuarioEntidade> pesquisarPorDepartRegionalEmSindicato(String cpf, DadosFilter dadosFilter) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select distinct ue from UsuarioEntidade ue ");
		jpql.append(" inner join ue.sindicato sindicato ");
		jpql.append(" inner join sindicato.empresaSindicato empresaSindicato ");
		jpql.append(" inner join empresaSindicato.empresa empresa ");

		if (dadosFilter.isGestorDr() || dadosFilter.isDiretoriaDr()) {
			jpql.append(" inner join empresa.empresaUats empresaUats ");
			jpql.append(" inner join empresaUats.unidadeAtendimentoTrabalhador uat ");
			jpql.append(" inner join uat.departamentoRegional dr ");
		}

		jpql.append(" where ue.cpf = :cpf ");

		if (dadosFilter.isGestorDr() || dadosFilter.isDiretoriaDr()) {
			jpql.append(" and dr.id in (:idDepartamentos) ");
		} else if (dadosFilter.isGestorEmpresa()) {
			jpql.append(" and empresa.id in (:idEmpresa) ");
		}

		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("cpf", cpf);

		if (dadosFilter.isGestorDr() || dadosFilter.isDiretoriaDr()) {
			query.setParameter("idDepartamentos", dadosFilter.getIdsDepartamentoRegional());
		} else if (dadosFilter.isGestorEmpresa()) {
			query.setParameter("idEmpresa", dadosFilter.getIdsEmpresa());
		}

		return query.getResultList();
	}

	public List<UsuarioEntidade> pesquisarPorDepartRegional(String cpf, DadosFilter dadosFilter) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select distinct ue from UsuarioEntidade ue ");
		jpql.append(" inner join ue.departamentoRegional dr ");
		jpql.append(" where ue.cpf = :cpf and dr.id in (:idDepartamentos) ");
		TypedQuery<UsuarioEntidade> query = criarConsultaPorTipo(jpql.toString());
		query.setParameter("idDepartamentos", dadosFilter.getIdsDepartamentoRegional());
		query.setParameter("cpf", cpf);
		return query.getResultList();
	}
}
