package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;

public abstract class FiltroUsuario {

    protected final Filtro filtro;

    FiltroUsuario() {
        this.filtro = new Filtro();
    }

    public abstract Filtro aplica(UsuarioFilter usuarioFilter, DadosFilter dados, Usuario usuario);

    public Filtro getFiltro() {
        return filtro;
    }
}
