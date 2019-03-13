package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;
import com.google.common.base.Strings;

public class FiltroPesquisa extends FiltroUsuario {

    @Override
    public Filtro aplica(UsuarioFilter usuarioFilter, DadosFilter dados, Usuario usuario) {
        if(!Strings.isNullOrEmpty(usuarioFilter.getNome())) {
            this.filtro.adicionaRestricao("vue.nome ilike concat('%', :nome, '%')", "nome", usuarioFilter.getNome());
        }
        if(!Strings.isNullOrEmpty(usuarioFilter.getLogin())) {
            this.filtro.adicionaRestricao("vue.login = :login", "login", usuarioFilter.getLogin());
        }
        if(!Strings.isNullOrEmpty(usuarioFilter.getCodigoPerfil())) {
            if(usuarioFilter.getCodigoPerfil().equalsIgnoreCase("SP")) {
                this.filtro.adicionaRestricao("vue.codigo_perfil is null");
            } else {
                this.filtro.adicionaRestricao("vue.codigo_perfil = :codigoPerfil", "codigoPerfil", usuarioFilter.getCodigoPerfil());
            }
        }

        return this.filtro;
    }

}
