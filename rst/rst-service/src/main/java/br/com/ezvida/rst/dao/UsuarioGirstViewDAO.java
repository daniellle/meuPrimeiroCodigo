package br.com.ezvida.rst.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.model.UsuarioGirstView;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class UsuarioGirstViewDAO extends BaseDAO<UsuarioGirstView, Long> {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioGirstViewDAO.class);

	private boolean filtroAplicado;

	@Inject
	public UsuarioGirstViewDAO(EntityManager em) {
		super(em, UsuarioGirstView.class);
	}

	@SuppressWarnings("unchecked")
	public ListaPaginada<UsuarioGirstView> pesquisarPorFiltro(UsuarioFilter usuarioFilter, DadosFilter dados) {
		LOGGER.debug("Pesquisando paginado UsuarioGirstView por filtro");

		ListaPaginada<UsuarioGirstView> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

		StringBuilder sql = new StringBuilder();
		Map<String, Object> parametros = Maps.newHashMap();
		getQueryPaginadoNativo(sql, parametros, usuarioFilter, dados, false);
		Query query = criarConsultaNativa(sql.toString(), UsuarioGirstView.class);
		DAOUtil.setParameterMap(query, parametros);

		listaPaginada.setQuantidade(getCountQueryPaginado(usuarioFilter, dados).longValue());

		if (usuarioFilter != null && usuarioFilter.getPagina() != null
				&& usuarioFilter.getQuantidadeRegistro() != null) {
			query.setFirstResult((usuarioFilter.getPagina() - 1) * usuarioFilter.getQuantidadeRegistro());
			query.setMaxResults(usuarioFilter.getQuantidadeRegistro());
		}

		listaPaginada.setList(query.getResultList());

		return listaPaginada;
	}

	public BigInteger getCountQueryPaginado(UsuarioFilter usuarioFilter, DadosFilter dados) {
		Map<String, Object> parametros = Maps.newHashMap();
		StringBuilder jpql = new StringBuilder();
		getQueryPaginadoNativo(jpql, parametros, usuarioFilter, dados, true);
		Query query = criarConsultaNativa(jpql.toString());
		DAOUtil.setParameterMap(query, parametros);
		return DAOUtil.getSingleResult(query);
	}

	private void getQueryPaginadoNativo(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter,
			DadosFilter dados, boolean count) {

		if (count) {
			jpql.append(" select count( DISTINCT usuario.id) from ");
		} else {
			jpql.append(" select DISTINCT usuario.id, usuario.nome, usuario.login, usuario.email from ");
		}

		jpql.append(" (select vw_usuario_entidade.id, vw_usuario_entidade.nome, vw_usuario_entidade.login, ");
		jpql.append(" vw_usuario_entidade.email, vw_usuario_entidade.codigo_perfil  ");
		jpql.append(" from vw_usuario_entidade vw_usuario_entidade  ");
		jpql.append(
				" left join emp_sindicato emp_sindicato  on (vw_usuario_entidade.id_empresa_fk = emp_sindicato.id_empresa_fk) ");
		jpql.append(" left join trabalhador trabalhador  on (vw_usuario_entidade.login = trabalhador.no_cpf) ");
		jpql.append(
				" left join emp_trabalhador emp_trabalhador  on (trabalhador.id_trabalhador = emp_trabalhador.id_trabalhador_fk) ");
		jpql.append(" left join empresa empresa on (vw_usuario_entidade.id_empresa_fk = empresa.id_empresa ");
		jpql.append(
				" or empresa.id_empresa = emp_trabalhador.id_empresa_fk or empresa.id_empresa = emp_sindicato.id_empresa_fk) ");
		jpql.append(" left join empresa_uat empresa_uat on (empresa.id_empresa = empresa_uat.id_empresa_fk) ");
		jpql.append(" left join und_atd_trabalhador und_atd_trabalhador  ");
		jpql.append(" on (empresa_uat.id_und_atd_trabalhador_fk = und_atd_trabalhador.id_und_atd_trabalhador) ");
		jpql.append(" left join departamento_regional departamento_regional ");
		jpql.append(
				" on (und_atd_trabalhador.id_departamento_regional_fk = departamento_regional.id_departamento_regional) ");
		if (usuarioFilter != null) {
			jpql.append(" where (");
		}
		aplicarFiltros(count, jpql, parametros, usuarioFilter);
		aplicarFiltrosDados(jpql, parametros, dados);
		applicarFiltroPerfis(count, usuarioFilter, jpql, parametros, dados);
		jpql.append(" ) usuario ");
		
		
		
		if (!count) {
			jpql.append(" order by usuario.nome ");
		}
	}

	private void applicarFiltroPerfis(boolean count, UsuarioFilter usuarioFilter, StringBuilder jpql,
			Map<String, Object> parametros, DadosFilter dados) {

		if (dados != null && !dados.isAdministrador()){

			if( (dados.isGestorDr() || dados.isDiretoriaDr() || dados.isGestorDn())) {
				if (dados.isGestorDr() || dados.isDiretoriaDr()) {
					if (isFiltroAplicado()) {
						jpql.append(" OR (");
						aplicarFiltros(count, jpql, parametros, usuarioFilter);
						setFiltroAplicado(true);
					}
					jpql.append(
							"  and (codigo_perfil in ('GEEM', 'GEEMM', 'TRA')  and vw_usuario_entidade.id_empresa_fk IS NULL )) )");
					jpql.append(" and codigo_perfil not in ('ADM', 'ATD', 'DIDN', 'DIDR', 'GDNA', 'GDNP', 'GDRM') ");
				}

				if (dados.isGestorDn()) {
					if (isFiltroAplicado()) {
						jpql.append(" and ");
					}
					jpql.append(
							"  codigo_perfil not in ('ADM', 'ATD', 'GDNA') )");
				}
				return;
			}
		}

		jpql.append(")");


	}

	private void aplicarFiltrosDados(StringBuilder jpql, Map<String, Object> parametros, DadosFilter dados) {
		if (dados != null && !dados.isSuperUsuario()) {
			if (dados.temIdsEmpresa()) {

				adicionarAnd(jpql);
				jpql.append(" (vw_usuario_entidade.id_empresa_fk IN (:idsEmpresa) OR ");
				jpql.append(" empresa.id_empresa IN (:idsEmpresa)) ");
				parametros.put("idsEmpresa", dados.getIdsEmpresa());
				setFiltroAplicado(true);
			}
			if (dados.temIdsDepRegional()) {
				String conectorLogico  = dados.temIdsEmpresa() ? " OR " : isFiltroAplicado() ? " AND " : " ";
				jpql.append(conectorLogico);
				jpql.append(" (vw_usuario_entidade.id_departamento_regional_fk IN (:idsDepRegional) OR ");
				jpql.append(" departamento_regional.id_departamento_regional IN (:idsDepRegional)) ");
				parametros.put("idsDepRegional", dados.getIdsDepartamentoRegional());
				setFiltroAplicado(true);
			}
		}
	}

	private void aplicarFiltros(boolean count, StringBuilder jpql, Map<String, Object> parametros,
			UsuarioFilter usuarioFilter) {
		if (usuarioFilter != null) {
			setFiltroAplicado(true);
			if (usuarioFilter.getCodigoPerfil() != null) {
				jpql.append(" vw_usuario_entidade.codigo_perfil = :codigoPerfil ");
				parametros.put("codigoPerfil", usuarioFilter.getCodigoPerfil());
			}

			montarFiltroIds(jpql, parametros, usuarioFilter);

			if (usuarioFilter.getNome() != null) {
				if (usuarioFilter.getCodigoPerfil() != null || usuarioFilter.getIdEmpresa() != null
						|| usuarioFilter.getIdDepartamentoRegional() != null) {
					adicionarAnd(jpql);
				}
				jpql.append(" set_simple_name(UPPER(vw_usuario_entidade.nome)) like set_simple_name(:nome) escape :sc");
				parametros.put("sc", "\\");
				parametros.put("nome", "%" + usuarioFilter.getNome().replaceAll("%", "\\%").toUpperCase().replace(" ", "%") + "%");
				setFiltroAplicado(true);
			}

			montarFiltroLogin(jpql, parametros, usuarioFilter);
			
			adicionarAnd(jpql);
			jpql.append(" vw_usuario_entidade.data_desativacao is null ");

		}

	}

	private void montarFiltroLogin(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter) {
		if (usuarioFilter.getLogin() != null) {
			if (usuarioFilter.getCodigoPerfil() != null || usuarioFilter.getIdEmpresa() != null
					|| usuarioFilter.getIdDepartamentoRegional() != null || usuarioFilter.getNome() != null ) {
				jpql.append("  and ");
			}
			jpql.append("  vw_usuario_entidade.login = :login ");
			parametros.put("login", usuarioFilter.getLogin());
			setFiltroAplicado(true);
		}
	}

	private void montarFiltroIds(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter) {
		if (usuarioFilter.getIdEmpresa() != null) {
			if (usuarioFilter.getCodigoPerfil() != null) {
				adicionarAnd(jpql);
			}

			jpql.append(" (vw_usuario_entidade.id_empresa_fk = :idEmpresa OR ");
			jpql.append(" empresa.id_empresa = :idEmpresa) ");
			parametros.put("idEmpresa", usuarioFilter.getIdEmpresa());
		}

		if (usuarioFilter.getIdDepartamentoRegional() != null) {
			if (usuarioFilter.getCodigoPerfil() != null || usuarioFilter.getIdEmpresa() != null) {
				adicionarAnd(jpql);
			}
			jpql.append(" (vw_usuario_entidade.id_departamento_regional_fk = :idDepartamentoRegional OR ");
			jpql.append(" departamento_regional.id_departamento_regional = :idDepartamentoRegional) ");
			parametros.put("idDepartamentoRegional", usuarioFilter.getIdDepartamentoRegional());
		}
	}

	private boolean isFiltroAplicado() {
		return filtroAplicado;
	}

	private void setFiltroAplicado(boolean filtroAplicado) {
		this.filtroAplicado = filtroAplicado;
	}

	private void adicionarAnd(StringBuilder jpql) {
		if (isFiltroAplicado()) {
			jpql.append(" and ");
			setFiltroAplicado(true);
		}
	}

}
