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

    private final Filtro filtroPesquisa;
    private final Filtro filtroHierarquia;
    private final Filtro filtroDrs;
    private final Filtro filtroUnidadeSesi;
    private final Filtro filtroTrabalhador;
    private final StringBuilder query;
    private final StringBuilder queryPorPermissao;
    private final Map<String, Object> parametros;

    public FiltroUsuarioBuilder(UsuarioFilter usuarioFilter, DadosFilter dados, Usuario usuario) {
        this.filtroPesquisa = new FiltroPesquisa().aplica(usuarioFilter, dados, usuario);
        this.filtroHierarquia = new FiltroHierarquia().aplica(usuarioFilter, dados, usuario);
        this.filtroDrs = new FiltroDrs().aplica(usuarioFilter, dados, usuario);
        this.filtroUnidadeSesi = new FiltroUnidadeSesi().aplica(usuarioFilter, dados, usuario);
        this.filtroTrabalhador = new FiltroTrabalhador().aplica(usuarioFilter, dados, usuario);
        this.query = new StringBuilder();
        this.queryPorPermissao = new StringBuilder();
        this.parametros = new HashMap<>();
    }

    public FiltroUsuarioBuilder buildPesquisaUsuario() {
        return this.addFiltroDePesquisa()
            .addFiltroDeHierarquia();
//            .addFiltroTrabalhador();
//            .addFiltroPorDrs()
//            .addFiltroPorUnidadeSesi();
    }

    private FiltroUsuarioBuilder addFiltroDePesquisa() {
        if(!Strings.isNullOrEmpty(filtroPesquisa.getQuery())) {
            this.query.append(" where ");
            this.query.append(filtroPesquisa.getQuery());
            this.parametros.putAll(filtroPesquisa.getParametros());
        }

        return this;
    }

    private FiltroUsuarioBuilder addFiltroDeHierarquia() {
        if(!Strings.isNullOrEmpty(filtroHierarquia.getQuery())) {
            if(!Strings.isNullOrEmpty(filtroPesquisa.getQuery())) {
                this.query.append(" and ");
            }
            else {
                this.query.append(" where ");
            }

            this.query.append(filtroHierarquia.getQuery());
            this.parametros.putAll(filtroHierarquia.getParametros());
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

    private FiltroUsuarioBuilder addFiltroTrabalhador() {
        if(!Strings.isNullOrEmpty(filtroTrabalhador.getQuery())) {
            this.queryPorPermissao.append(filtroTrabalhador.getQuery());
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
        return RESULT_BUSCA_USUARIO  + query.toString() + getQueryPorPermissao() + " order by vue.nome asc";
    }

    public String getQueryCountPesquisaUsuario() {
        return COUNT_BUSCA_USUARIO + query.toString() + getQueryPorPermissao();
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
