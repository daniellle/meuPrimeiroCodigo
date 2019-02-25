package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;

public class FiltroDrs extends FiltroUsuario {

    @Override
    public Filtro aplica(UsuarioFilter usuarioFilter, DadosFilter dadosFilter, Usuario usuario) {
        if(usuarioFilter.getIdDepartamentoRegional() != null) {
            this.filtro.adicionaRestricao("id_departamento_regional_fk = :idDr", "idDr", usuarioFilter.getIdDepartamentoRegional());
        }

        if(dadosFilter.isAdministrador() || dadosFilter.contemPapel("ATD") || dadosFilter.isGestorDn()) {
            return this.filtro;
        }

        if(dadosFilter.isGestorDr()) {
            this.filtro.adicionaRestricao("id_departamento_regional_fk in (:idsDrs)", "idsDrs", dadosFilter.getIdsDepartamentoRegional());
        }

        return this.filtro;
    }

}
