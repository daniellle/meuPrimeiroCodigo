package br.com.ezvida.rst.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ezvida.rst.model.dto.PerfilUsuarioDTO;
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

    public ListaPaginada<PerfilUsuarioDTO> pesquisarPerfilUsuarioFiltro(UsuarioFilter usuarioFilter, DadosFilter dados) {
        LOGGER.debug("Pesquisando PerfilUsuarioDTO por filtro para geração de lista paginada");
        ListaPaginada<PerfilUsuarioDTO> listaPaginada = new ListaPaginada<>(0L, new ArrayList<>());

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();
        getQueryPaginadoNativoPerfilUsuario(sql, parametros, usuarioFilter, dados, false);
        Query query = criarConsultaNativa(sql.toString());
        DAOUtil.setParameterMap(query, parametros);
        listaPaginada.setQuantidade(getCountQueryPaginado(usuarioFilter, dados).longValue());

        if (usuarioFilter != null && usuarioFilter.getPagina() != null
                && usuarioFilter.getQuantidadeRegistro() != null) {
            query.setFirstResult((usuarioFilter.getPagina() - 1) * usuarioFilter.getQuantidadeRegistro());
            query.setMaxResults(usuarioFilter.getQuantidadeRegistro());
        }

        listaPaginada.setList(preencherLista(query.getResultList()));
        return listaPaginada;
    }

    public List<PerfilUsuarioDTO> pesquisarRelatorioFiltro(UsuarioFilter usuarioFilter, DadosFilter dados) {
        LOGGER.debug("Pesquisando PerfilUsuario por filtro para geração de PDF");

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametros = Maps.newHashMap();
        getQueryPaginadoNativoPerfilUsuario(sql, parametros, usuarioFilter, dados, false);
        Query query = criarConsultaNativa(sql.toString());
        DAOUtil.setParameterMap(query, parametros);

        List<Object[]> list = query.getResultList();

        return preencherLista(list);
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
            jpql.append(" select DISTINCT id, nome, login, email from ");
        }


        jpql.append(" (select vue.id, vue.nome, vue.login, ");
        jpql.append(" vue.email, vue.codigo_perfil  ");
        jpql.append(" from vw_usuario_entidade vue  ");
        if (!dados.isAdministrador() && !dados.isGestorDn()) {
            aplicarSubselectJoins(jpql, dados, parametros);
        } else {
            jpql.append(" WHERE vue.id is not null");
        }

        aplicarFiltros(count, jpql, parametros, usuarioFilter);
        //aplicarFiltrosDados(jpql, parametros, dados);
        applicarFiltroPerfis(count, usuarioFilter, jpql, parametros, dados);
        jpql.append(" ) as usuario ");


        if (!count) {
            jpql.append(" order by usuario.nome ");
        }
    }

    private void getQueryPaginadoNativoPerfilUsuario(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter,
                                                     DadosFilter dados, boolean count) {
        jpql.append(" select id, nome, login, email,");
        jpql.append(" nome_perfil, e.nm_fantasia,");
        jpql.append(" dr.ds_nome_fantasia, uat.ds_razao_social");
        jpql.append(" from (select distinct vue.id, vue.nome,");
        jpql.append(" vue.login, vue.email, vue.nome_perfil, vue.id_empresa_fk,");
        jpql.append(" vue.id_und_atd_trab_fk, vue.id_departamento_regional_fk");
        jpql.append(" from vw_usuario_entidade vue");

        if (!dados.isAdministrador() && !dados.isGestorDn()) {
            aplicarSubselectJoins(jpql, dados, parametros);
        } else {
            jpql.append(" WHERE vue.id is not null");
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
            jpql.append(" WHERE vue.id_departamento_regional_fk IN (:idsDepRegional) ")
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
                    .append(" WHERE id_departamento_regional IN (vue.id_departamento_regional_fk))");
            parametros.put("idsDepRegional", dados.getIdsDepartamentoRegional());
            setFiltroAplicado(true);

        } else if (dados.isGetorUnidadeSESI()) {
            jpql.append(" WHERE vue.id_und_atd_trab_fk IN (:idsUnidadeSESI)");
            jpql.append(" OR vue.id_empresa_fk IN (SELECT id_empresa")
                    .append(" FROM und_atd_trabalhador u")
                    .append(" JOIN und_obra_contrato_uat c ON id_und_atd_trabalhador = c.id_und_atd_trabalhador_fk")
                    .append(" JOIN und_obra o ON id_und_obra = c.id_und_obra_fk")
                    .append(" JOIN empresa e ON id_empresa = o.id_empresa_fk")
                    .append(" WHERE id_departamento_regional IN (vue.id_departamento_regional_fk)) ");
            parametros.put("idsUnidadeSESI", dados.getIdsUnidadeSESI());
            setFiltroAplicado(true);

        } else if (dados.isGestorEmpresa()) {
            jpql.append(" WHERE vue.id_empresa_fk IN (:idsEmpresa)");
            parametros.put("idsEmpresa", dados.getIdsEmpresa());
            setFiltroAplicado(true);

        }

        jpql.append(" OR (id_departamento_regional_fk is null AND id_und_atd_trab_fk is null AND id_empresa_fk is null)");
        return;
    }

    private void applicarFiltroPerfis(boolean count, UsuarioFilter usuarioFilter, StringBuilder jpql,
                                      Map<String, Object> parametros, DadosFilter dados) {

        if (dados != null && !dados.isAdministrador()) {

            if ((dados.isGestorDr() || dados.isDiretoriaDr() || dados.isGestorDn())) {
                if (dados.isGestorDr() || dados.isDiretoriaDr()) {
                    jpql.append(" AND codigo_perfil not in ('ADM', 'ATD', 'DIDN', 'DIDR', 'GDNA', 'MTSDN', 'GDNP') ");
                }

                if (dados.isGestorDn()) {
                    jpql.append(
                            "  AND codigo_perfil not in ('ADM', 'ATD', 'GDNA', 'MTSDN') )");
                }
                return;
            }
        }

        //jpql.append(")");


    }

    private void aplicarFiltrosDados(StringBuilder jpql, Map<String, Object> parametros, DadosFilter dados) {
        if (dados != null && !dados.isSuperUsuario()) {
            if (dados.temIdsEmpresa()) {

                adicionarAnd(jpql);
                jpql.append(" (vue.id_empresa_fk IN (:idsEmpresa) OR ");
                jpql.append(" e.id_empresa IN (:idsEmpresa)) ");
                parametros.put("idsEmpresa", dados.getIdsEmpresa());
                setFiltroAplicado(true);
            }
            if (dados.temIdsDepRegional()) {
                String conectorLogico = dados.temIdsEmpresa() ? " OR " : isFiltroAplicado() ? " AND " : " ";
                jpql.append(conectorLogico);
                jpql.append(" (vw_usuario_entidade.id_departamento_regional_fk IN (:idsDepRegional) OR ");
                jpql.append(" departamento_regional.id_departamento_regional IN (:idsDepRegional)) ");
                jpql.append("             and vw_usuario_entidade.id_departamento_regional_fk is not null");
                parametros.put("idsDepRegional", dados.getIdsDepartamentoRegional());
                setFiltroAplicado(true);
            }

            if (dados.temIdsUnidadeSESI()) {
                String conectorLogico = dados.temIdsEmpresa() || dados.temIdsDepRegional() ? " OR " : isFiltroAplicado() ? " AND " : " ";
                jpql.append(conectorLogico);
                jpql.append(" (und_atd_trabalhador.id_und_atd_trabalhador IN (:idsUnidadeSESI)) ");
                parametros.put("idsUnidadeSESI", dados.getIdsUnidadeSESI());
                setFiltroAplicado(true);
            }
        }
    }

    private void aplicarFiltros(boolean count, StringBuilder jpql, Map<String, Object> parametros,
                                UsuarioFilter usuarioFilter) {
        if (usuarioFilter != null) {
            setFiltroAplicado(true);
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
                setFiltroAplicado(true);
            }

            montarFiltroLogin(jpql, parametros, usuarioFilter);
            jpql.append(" AND vue.data_desativacao is null ");

        }

    }

    private void montarFiltroLogin(StringBuilder jpql, Map<String, Object> parametros, UsuarioFilter usuarioFilter) {
        if (usuarioFilter.getLogin() != null) {
            jpql.append(" AND vue.login = :login ");
            parametros.put("login", usuarioFilter.getLogin());
            setFiltroAplicado(true);
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

        if(usuarioFilter.getIdUnidadeSesi() != null){

            jpql.append(" AND (vue.id_und_atd_trab_fk = :idUnidadeSesi) ");
            parametros.put("idUnidadeSesi", usuarioFilter.getIdUnidadeSesi());
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

    private List<PerfilUsuarioDTO> preencherLista(List<Object[]> list) {

        List<PerfilUsuarioDTO> perfis = new ArrayList<>();

        for (Object[] objeto : list) {
            PerfilUsuarioDTO pu = new PerfilUsuarioDTO();
            pu.setNome(objeto[1].toString());
            pu.setLogin(objeto[2].toString());
            pu.setPerfil(objeto[4].toString());
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

    private List<PerfilUsuarioDTO> tratarPerfisDuplicados(List<PerfilUsuarioDTO> lista) {

        for (int i = 0; i < lista.size(); i++) {
            for (int j = 0; j < lista.size(); j++) {
                if (lista.get(i).getLogin().equals(lista.get(j).getLogin())) {
                    lista.get(i).setPerfil(lista.get(j).getPerfil() + ", " + lista.get(i).getPerfil());
                    lista.remove(j);

                }
            }
        }
        return lista;
    }

}
