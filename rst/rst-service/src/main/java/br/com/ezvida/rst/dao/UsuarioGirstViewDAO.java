package br.com.ezvida.rst.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
import br.com.ezvida.rst.model.dto.PerfilUsuarioDTO;
import fw.core.jpa.BaseDAO;
import fw.core.jpa.DAOUtil;

public class UsuarioGirstViewDAO extends BaseDAO<UsuarioGirstView, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioGirstViewDAO.class);

    @Inject
    public UsuarioGirstViewDAO(EntityManager em) {
        super(em, UsuarioGirstView.class);
    }

    @SuppressWarnings("unchecked")
    public ListaPaginada<UsuarioGirstView> pesquisarPorFiltro(UsuarioFilter usuarioFilter, DadosFilter dados, List<String> listaDeLogins) { LOGGER.debug("Pesquisando paginado UsuarioGirstView por filtro");

        ListaPaginada<UsuarioGirstView> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();
        getQueryPaginadoNativo(sql, parametros, usuarioFilter, dados, false, listaDeLogins, false);
        Query query = criarConsultaNativa(sql.toString(), UsuarioGirstView.class);
        DAOUtil.setParameterMap(query, parametros);

        listaPaginada.setQuantidade(getCountQueryPaginado(usuarioFilter, dados, listaDeLogins).longValue());

        if (usuarioFilter != null && usuarioFilter.getPagina() != null
                && usuarioFilter.getQuantidadeRegistro() != null) {
            query.setFirstResult((usuarioFilter.getPagina() - 1) * usuarioFilter.getQuantidadeRegistro());
            query.setMaxResults(usuarioFilter.getQuantidadeRegistro());
        }

        listaPaginada.setList(query.getResultList());
        return listaPaginada;
    }

    @SuppressWarnings("unchecked")
	public ListaPaginada<PerfilUsuarioDTO> pesquisarPerfilUsuarioFiltro(UsuarioFilter usuarioFilter, DadosFilter dados, List<String> listaDeLogins) {
        LOGGER.debug("Pesquisando PerfilUsuarioDTO por filtro para geração de lista paginada");
        ListaPaginada<PerfilUsuarioDTO> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();
        getQueryPaginadoNativoPerfilUsuario(sql, parametros, usuarioFilter, dados, false, listaDeLogins);
        Query query = criarConsultaNativa(sql.toString());
        DAOUtil.setParameterMap(query, parametros);
        listaPaginada.setQuantidade(getCountQueryPaginado(usuarioFilter, dados, listaDeLogins).longValue());

        if (usuarioFilter != null && usuarioFilter.getPagina() != null
                && usuarioFilter.getQuantidadeRegistro() != null) {
            query.setFirstResult((usuarioFilter.getPagina() - 1) * usuarioFilter.getQuantidadeRegistro());
            query.setMaxResults(usuarioFilter.getQuantidadeRegistro());
        }

        listaPaginada.setList(preencherLista(query.getResultList()));
        return listaPaginada;
    }

    public List<PerfilUsuarioDTO> pesquisarRelatorioFiltro(UsuarioFilter usuarioFilter, DadosFilter dados, List<String> listaDeLogin) {
        LOGGER.debug("Pesquisando PerfilUsuario por filtro para geração de PDF");

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();
        getQueryPaginadoNativoPerfilUsuario(sql, parametros, usuarioFilter, dados, false, listaDeLogin);
        Query query = criarConsultaNativa(sql.toString());
        DAOUtil.setParameterMap(query, parametros);

        @SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
        return preencherLista(list);
    }


    public BigInteger getCountQueryPaginado(UsuarioFilter usuarioFilter, DadosFilter dados, List<String> listaDeLogins) {
        Map<String, Object> parametros = Maps.newHashMap();
        StringBuilder jpql = new StringBuilder();
        getQueryPaginadoNativoPerfilUsuario(jpql, parametros, usuarioFilter, dados, true, listaDeLogins);
        Query query = criarConsultaNativa(jpql.toString());
        DAOUtil.setParameterMap(query, parametros);
        return DAOUtil.getSingleResult(query);
    }

    private void getQueryPaginadoNativo(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter,
                                        DadosFilter dados, boolean count, List<String> listaDeLogin, boolean isRelatorio) {


        if (count) {
            jpql.append(" select count( DISTINCT usuario.id) from ");

        } else {
            jpql.append(" select DISTINCT id, nome, login, email from ");
        }


        jpql.append(" (select vue.id, vue.nome, vue.login, ");
        jpql.append(" vue.email, vue.codigo_perfil  ");
        jpql.append(" from vw_usuario_entidade vue  ");
        if (!dados.isAdministrador() && !dados.isGestorDn()) {
            aplicarSubselectJoins(jpql, dados, parametros);
        } else {
            jpql.append(" WHERE vue.id is not null ");
        }
        if (!listaDeLogin.isEmpty()) {
            jpql.append(" AND vue.login NOT IN (:listaLogin) ");
            parametros.put("listaLogin", listaDeLogin);
        }

        aplicarFiltros(count, jpql, parametros, usuarioFilter);
        //aplicarFiltrosDados(jpql, parametros, dados);
        applicarFiltroPerfis(count, usuarioFilter, jpql, parametros, dados);
        jpql.append(" ) as usuario ");

        if (!count) {
            jpql.append(" order by usuario.nome ");
        }
    }

    private void getQueryPaginadoNativoPerfilUsuario(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter, DadosFilter dados, boolean count, List<String> listaDeLogins) {

        if (count) {
            jpql.append( "select count (id) ");
        }else {

            jpql.append(" select id, nome, login, email,");
            jpql.append(" nome_perfil, e.nm_fantasia,");
            jpql.append(" dr.ds_nome_fantasia, uat.ds_razao_social");
        }
        jpql.append(" from (select distinct vue.id, vue.nome,");
        jpql.append(" vue.login, vue.email, vue.nome_perfil, vue.id_empresa_fk,");
        jpql.append(" vue.id_und_atd_trab_fk, vue.id_departamento_regional_fk");
        jpql.append(" from vw_usuario_entidade vue");

        if (!dados.isAdministrador() && !dados.isGestorDn()) {
            aplicarSubselectJoins(jpql, dados, parametros);
        } else {
            jpql.append(" WHERE vue.id is not null");
        }

        if (!listaDeLogins.isEmpty()) {
            jpql.append(" AND vue.login NOT IN (:listaDeLogins) ");
            parametros.put("listaDeLogins", listaDeLogins);
        }

        aplicarFiltros(count, jpql, parametros, usuarioFilter);
        applicarFiltroPerfis(count, usuarioFilter, jpql, parametros, dados);
        jpql.append(" ) as usuario ");
        jpql.append(" left join empresa e ");
        jpql.append(" on e.id_empresa = usuario.id_empresa_fk");
        jpql.append(" left join departamento_regional dr ");
        jpql.append(" on dr.id_departamento_regional = usuario.id_departamento_regional_fk");
        jpql.append(" left join und_atd_trabalhador uat ");
        jpql.append("  	on uat.id_und_atd_trabalhador = usuario.id_und_atd_trab_fk");

        if (!count) {
            jpql.append(" order by usuario.nome ");
        }
    }

    private void aplicarSubselectJoins(StringBuilder jpql, DadosFilter dados, Map<String, Object> parametros) {
        if (dados.isGestorDr() || dados.isDiretoriaDr()) {
            jpql.append(" WHERE (vue.id_departamento_regional_fk IN (:idsDepRegional) ")
                    .append(" OR vue.id_und_atd_trab_fk IN (SELECT id_und_atd_trabalhador")
                    .append(" FROM departamento_regional d")
                    .append(" JOIN und_atd_trabalhador ON id_departamento_regional = id_departamento_regional_fk")
                    .append(" WHERE id_departamento_regional IN (vue.id_departamento_regional_fk))");
            jpql.append(" OR vue.id_empresa_fk IN (SELECT id_empresa")
                    .append(" FROM departamento_regional d")
                    .append(" JOIN und_atd_trabalhador u ON id_departamento_regional = id_departamento_regional_fk")
                    .append(" JOIN und_obra_contrato_uat c ON id_und_atd_trabalhador = c.id_und_atd_trabalhador_fk")
                    .append(" JOIN und_obra o ON id_und_obra = c.id_und_obra_fk")
                    .append(" JOIN empresa e ON id_empresa = o.id_empresa_fk")
                    .append(" WHERE id_departamento_regional IN (vue.id_departamento_regional_fk))")
                    .append(" OR id_departamento_regional_fk is null ");
            parametros.put("idsDepRegional", dados.getIdsDepartamentoRegional());

        } else if (dados.isGetorUnidadeSESI()) {
            jpql.append(" WHERE (vue.id_und_atd_trab_fk IN (:idsUnidadeSESI)");
            jpql.append(" OR vue.id_empresa_fk IN (SELECT id_empresa")
                    .append(" FROM und_atd_trabalhador u")
                    .append(" JOIN und_obra_contrato_uat c ON id_und_atd_trabalhador = c.id_und_atd_trabalhador_fk")
                    .append(" JOIN und_obra o ON id_und_obra = c.id_und_obra_fk")
                    .append(" JOIN empresa e ON id_empresa = o.id_empresa_fk")
                    .append(" WHERE id_departamento_regional IN (vue.id_departamento_regional_fk)) ");
            parametros.put("idsUnidadeSESI", dados.getIdsUnidadeSESI());

        } else if (dados.isGestorEmpresa()) {
            jpql.append(" WHERE (vue.id_empresa_fk IN (:idsEmpresa)");
            parametros.put("idsEmpresa", dados.getIdsEmpresa());

        }
        jpql.append(" ) ");
//        jpql.append(" OR (id_departamento_regional_fk is null AND id_und_atd_trab_fk is null AND id_empresa_fk is null))");
        return;
    }

    private void applicarFiltroPerfis(boolean count, UsuarioFilter usuarioFilter, StringBuilder jpql,
                                      Map<String, Object> parametros, DadosFilter dados) {

        if (dados != null && !dados.isAdministrador()) {

            if (dados.isGestorDn() || dados.isDiretoriaDn()) {
                jpql.append(" AND vue.codigo_perfil not in ('ADM', 'ATD', 'GDNA', 'MTSDN') ");
                return;
            } else if (dados.getPapeis().contains("SUDR")) {
                jpql.append(" AND vue.codigo_perfil not in ('ADM', 'DIDN', 'GDNA', 'GDNP', 'MTSDN', 'GCDN', 'GCODN', 'ATD') ");
                jpql.append(" AND vue.codigo_perfil is not null ");
                return;
            } else if (dados.getPapeis().contains("GDRM")) {
                jpql.append(" AND vue.codigo_perfil not in ('ADM', 'DIDN', 'GDNA', 'GDNP', 'MTSDN', 'GCDN', 'GCODN', 'ATD', 'SUDR') ");
                jpql.append(" AND vue.codigo_perfil is not null ");
                return;
            } else if (dados.getPapeis().contains("GDRA")) {
                jpql.append(" AND vue.codigo_perfil not in ('ADM', 'DIDN', 'GDNA', 'GDNP', 'MTSDN', 'GCDN', 'GCODN', 'ATD', 'SUDR', 'GDRM')");
                jpql.append(" AND vue.codigo_perfil is not null ");
                return;
            } else if (dados.getPapeis().contains("GUS")) {
                jpql.append(" AND vue.codigo_perfil not in ('ADM', 'DIDN', 'GDNA', 'GDNP', 'MTSDN', 'GCDN', 'GCODN', 'ATD', 'SUDR', 'GDRM', 'GDRA')");
                jpql.append(" AND vue.codigo_perfil is not null ");
                return;
            } else if (dados.getPapeis().contains("GEEMM")) {
                jpql.append(" AND vue.codigo_perfil not in ('ADM', 'DIDN', 'GDNA', 'GDNP', 'MTSDN', 'GCDN', 'GCODN', 'ATD', 'SUDR', 'GDRM', 'GDRA', 'MTSDR', 'GUS')");
                jpql.append(" AND vue.codigo_perfil is not null ");
                return;
            } else if (dados.getPapeis().contains("GEEM")) {
                jpql.append(" AND vue.codigo_perfil not in ('ADM', 'DIDN', 'GDNA', 'GDNP', 'MTSDN', 'GCDN', 'GCODN', 'ATD', 'SUDR', 'GDRM', 'GDRA', 'MTSDR', 'GUS', 'GEEMM')");
                jpql.append(" AND vue.codigo_perfil is not null ");
                return;
            }


           /* if ((dados.isGestorDr() || dados.isDiretoriaDr() || dados.isGestorDn())) {
                if (dados.isGestorDr() || dados.isDiretoriaDr()) {
                    jpql.append(" AND codigo_perfil not in ('ADM', 'ATD', 'DIDN', 'DIDR', 'GDNA', 'MTSDN', 'GDNP') ");
                }

                if (dados.isGestorDn()) {
                    jpql.append(
                            "  AND codigo_perfil not in ('ADM', 'ATD', 'GDNA', 'MTSDN') ");
                }
                return;
            }*/
        }
    }


    private void aplicarFiltros(boolean count, StringBuilder jpql, Map<String, Object> parametros,
                                UsuarioFilter usuarioFilter) {
        if (usuarioFilter != null) {
            if (usuarioFilter.getCodigoPerfil() != null) {
                if (usuarioFilter.getCodigoPerfil().equals("SP")) {
                    jpql.append(" AND vue.codigo_perfil is null and vue.origemdados is not null");
                } else {
                    jpql.append(" AND vue.codigo_perfil = :codigoPerfil ");
                    parametros.put("codigoPerfil", usuarioFilter.getCodigoPerfil());
                }
            }

            montarFiltroIds(jpql, parametros, usuarioFilter);

            if (usuarioFilter.getNome() != null) {
                jpql.append(" AND set_simple_name(UPPER(vue.nome)) like set_simple_name(:nome) escape :sc");
                parametros.put("sc", "\\");
                parametros.put("nome", "%" + usuarioFilter.getNome().replaceAll("%", "\\%").toUpperCase().replace(" ", "%") + "%");
            }

            montarFiltroLogin(jpql, parametros, usuarioFilter);
            jpql.append(" AND vue.data_desativacao is null ");

        }

    }

    private void montarFiltroLogin(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter) {
        if (usuarioFilter.getLogin() != null) {
            jpql.append(" AND vue.login = :login ");
            parametros.put("login", usuarioFilter.getLogin());
        }
    }

    private void montarFiltroIds(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter) {
        if (usuarioFilter.getIdEmpresa() != null) {

            jpql.append(" AND (vue.id_empresa_fk = :idEmpresa) ");
            parametros.put("idEmpresa", usuarioFilter.getIdEmpresa());
        }

        if (usuarioFilter.getIdDepartamentoRegional() != null) {
            jpql.append(" AND (vue.id_departamento_regional_fk = :idDepartamentoRegional ");
            jpql.append(" and vue.id_departamento_regional_fk is not null)");
            parametros.put("idDepartamentoRegional", usuarioFilter.getIdDepartamentoRegional());
        }

        if (usuarioFilter.getIdUnidadeSesi() != null) {

            jpql.append(" AND (vue.id_und_atd_trab_fk = :idUnidadeSesi) ");
            parametros.put("idUnidadeSesi", usuarioFilter.getIdUnidadeSesi());
        }
    }

    private List<PerfilUsuarioDTO> preencherLista(List<Object[]> list) {

        List<PerfilUsuarioDTO> perfis = new ArrayList<>();

        for (Object[] objeto : list) {
            PerfilUsuarioDTO pu = new PerfilUsuarioDTO();
            if (objeto[1] != null) {
                pu.setNome(objeto[1].toString());
            }
            if (objeto[2] != null) {
                pu.setLogin(objeto[2].toString());
            }
            if (objeto[4] != null) {
                pu.setPerfil(objeto[4].toString());
            }
            if (objeto[5] != null) {
                pu.setEmpresa(objeto[5].toString());
            }
            if (objeto[6] != null) {
                pu.setDepartamento(objeto[6].toString());
            }
            if (objeto[7] != null) {
                pu.setUnidade(objeto[7].toString());
            }
            perfis.add(pu);
        }
        return perfis;
    }

}
