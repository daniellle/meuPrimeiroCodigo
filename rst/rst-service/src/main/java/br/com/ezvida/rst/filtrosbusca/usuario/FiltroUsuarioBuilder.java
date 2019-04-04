package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public class FiltroUsuarioBuilder {

    private static final String RESULT_BUSCA_USUARIO = "select distinct(vue.id), vue.login, vue.nome, vue.email from vw_usuario_entidade vue ";
    private static final String COUNT_BUSCA_USUARIO = "select count(distinct vue.id) from vw_usuario_entidade vue ";
    private static final String JOINS_RELATORIO_USUARIO = "left join departamento_regional dr on dr.id_departamento_regional = vue.id_departamento_regional_fk " +
            "left join empresa e on id_empresa =  vue.id_empresa_fk " +
            "left join und_atd_trabalhador uat on id_und_atd_trabalhador = vue.id_und_atd_trab_fk ";
    private static final String RESULT_BUSCA_USUARIO_RELATORIO = "select distinct vue.nome, vue.login, vue.nome_perfil, dr.ds_nome_fantasia, uat.nm_fantasia, e.nm_fantasia as nome_empresa from " +
            "vw_usuario_entidade vue " +
           JOINS_RELATORIO_USUARIO;
    private static final String COUNT_BUSCA_USUARIO_RELATORIO = "select count(1) from (select distinct vue.nome, vue.login, vue.nome_perfil, dr.ds_nome_fantasia, " +
            "uat.nm_fantasia, e.nm_fantasia as nome_empresa from vw_usuario_entidade vue " + JOINS_RELATORIO_USUARIO;

    private final Filtro filtroPesquisa;
    private final Filtro filtroHierarquiaBuscaUsuario;
    private final Filtro filtroHierarquiaRelatorioUsuario;
    private final Filtro filtroDrs;
    private final Filtro filtroUnidadeSesi;
    private final Filtro filtroEmpresa;
    private final Filtro filtroTrabalhador;
    private final StringBuilder query;
    private final StringBuilder queryPorPermissao;
    private final Map<String, Object> parametros;

    public FiltroUsuarioBuilder(UsuarioFilter usuarioFilter, DadosFilter dados, Usuario usuario) {
        this.filtroPesquisa = new FiltroPesquisa().aplica(usuarioFilter, dados, usuario);
        this.filtroHierarquiaBuscaUsuario = new FiltroHierarquiaBuscaUsuario().aplica(usuarioFilter, dados, usuario);
        this.filtroHierarquiaRelatorioUsuario = new FiltroHierarquiaRelatorioUsuario().aplica(usuarioFilter, dados, usuario);
        this.filtroDrs = new FiltroDrs().aplica(usuarioFilter, dados, usuario);
        this.filtroUnidadeSesi = new FiltroUnidadeSesi().aplica(usuarioFilter, dados, usuario);
        this.filtroEmpresa = new FiltroPorEmpresa().aplica(usuarioFilter, dados, usuario);
        this.filtroTrabalhador = new FiltroTrabalhador().aplica(usuarioFilter, dados, usuario);
        this.query = new StringBuilder();
        this.queryPorPermissao = new StringBuilder();
        this.parametros = new HashMap<>();
    }

    public FiltroUsuarioBuilder buildPesquisaUsuario() {
        return this.addFiltroDePesquisa()
            .addFiltroDeHierarquiaPesquisaUsuarios()
            .addFiltroPorDrs()
            .addFiltroPorUnidadeSesi()
            .addFiltroEmpresas()
            .addFiltroTrabalhador();
    }

    public FiltroUsuarioBuilder buildRelatorioUsuario() {
        return this.addFiltroDePesquisa()
                .addFiltroDeHierarquiaRelatorioUsuarios()
                .addFiltroPorDrs()
                .addFiltroPorUnidadeSesi()
                .addFiltroEmpresas()
                .addFiltroTrabalhador();
    }

    private FiltroUsuarioBuilder addFiltroDePesquisa() {
        if(!Strings.isNullOrEmpty(filtroPesquisa.getQuery())) {
            this.query.append(" where ");
            this.query.append(filtroPesquisa.getQuery());
            this.parametros.putAll(filtroPesquisa.getParametros());
        }

        return this;
    }

    private FiltroUsuarioBuilder addFiltroDeHierarquiaPesquisaUsuarios() {
        if(!Strings.isNullOrEmpty(filtroHierarquiaBuscaUsuario.getQuery())) {
            if(!Strings.isNullOrEmpty(filtroPesquisa.getQuery())) {
                this.query.append(" and ");
            }
            else {
                this.query.append(" where ");
            }

            this.query.append(filtroHierarquiaBuscaUsuario.getQuery());
            this.parametros.putAll(filtroHierarquiaBuscaUsuario.getParametros());
        }

        return this;
    }

    private FiltroUsuarioBuilder addFiltroDeHierarquiaRelatorioUsuarios() {
        if(!Strings.isNullOrEmpty(filtroHierarquiaRelatorioUsuario.getQuery())) {
            if(!Strings.isNullOrEmpty(filtroPesquisa.getQuery())) {
                this.query.append(" and ");
            }
            else {
                this.query.append(" where ");
            }

            this.query.append(filtroHierarquiaRelatorioUsuario.getQuery());
            this.parametros.putAll(filtroHierarquiaRelatorioUsuario.getParametros());
        }

        return this;
    }

    private FiltroUsuarioBuilder addFiltroPorDrs() {
        if(!Strings.isNullOrEmpty(filtroDrs.getQuery())) {
            this.queryPorPermissao.append(filtroDrs.getQuery());
            this.parametros.putAll(filtroDrs.getParametros());
        }

        return this;
    }

    private FiltroUsuarioBuilder addFiltroPorUnidadeSesi() {
        if(!Strings.isNullOrEmpty(filtroUnidadeSesi.getQuery())) {
            addOrNaQueryPorPermissao(this.queryPorPermissao);
            String queryUnidadeSesi = "("+filtroUnidadeSesi.getQuery()+")";
            this.queryPorPermissao.append(queryUnidadeSesi);
            this.parametros.putAll(filtroUnidadeSesi.getParametros());
        }

        return this;
    }

    private FiltroUsuarioBuilder addFiltroEmpresas() {
        if(!Strings.isNullOrEmpty(filtroEmpresa.getQuery())) {
            addOrNaQueryPorPermissao(this.queryPorPermissao);
            String queryEmpresas = "("+filtroEmpresa.getQuery()+")";
            this.queryPorPermissao.append(queryEmpresas);
            this.parametros.putAll(filtroEmpresa.getParametros());
        }

        return this;
    }

    private FiltroUsuarioBuilder addFiltroTrabalhador() {
        if(!Strings.isNullOrEmpty(filtroTrabalhador.getQuery())) {
            addOrNaQueryPorPermissao(this.queryPorPermissao);
            String queryTrabalhador = "("+filtroTrabalhador.getQuery()+")";
            this.queryPorPermissao.append(queryTrabalhador);
            this.parametros.putAll(filtroTrabalhador.getParametros());
        }

        return this;
    }

    private void addOrNaQueryPorPermissao(StringBuilder str) {
        if(!Strings.isNullOrEmpty(this.queryPorPermissao.toString())) {
            str.append(" or ");
        }
    }

    public String getQueryPesquisaUsuario() {
        return RESULT_BUSCA_USUARIO  + query.toString() + getQueryPorPermissao() + " and dt_exclusao is null order by vue.nome asc";
    }

    public String getQueryCountPesquisaUsuario() {
        return COUNT_BUSCA_USUARIO + query.toString() + getQueryPorPermissao() + " and dt_exclusao is null";
    }

    public String getQueryRelatorioUsuario() {
        return RESULT_BUSCA_USUARIO_RELATORIO  + query.toString() + getQueryPorPermissao() + " and vue.dt_exclusao is null order by vue.nome asc";
    }

    public String getQueryCountRelatorioUsuario() {
        return COUNT_BUSCA_USUARIO_RELATORIO + query.toString() + getQueryPorPermissao() +" and vue.dt_exclusao is null) as usuario_perfil";
    }

    private String getQueryPorPermissao() {
        StringBuilder queryFormatada = new StringBuilder();
        if(!Strings.isNullOrEmpty(this.queryPorPermissao.toString())) {
            queryFormatada.append(this.queryPorPermissao.toString());
            queryFormatada.insert(0, "(");
            queryFormatada.append(")");
            queryFormatada.insert(0, " and ");
        }
        return queryFormatada.toString();
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }
}
