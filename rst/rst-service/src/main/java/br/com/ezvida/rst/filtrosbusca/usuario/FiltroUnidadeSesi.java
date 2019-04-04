package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;

import java.util.Arrays;

public class FiltroUnidadeSesi extends FiltroUsuario {

    @Override
    public Filtro aplica(UsuarioFilter usuarioFilter, DadosFilter dadosFilter, Usuario usuario) {

        if(usuarioFilter.getIdDepartamentoRegional() != null) {
            this.filtro.adicionaRestricao(
                    "vue.id_und_atd_trab_fk in (select id_und_atd_trabalhador from und_atd_trabalhador where id_departamento_regional_fk in (:idsDrs))",
                    "idsDrs", Arrays.asList(usuarioFilter.getIdDepartamentoRegional()));

            return this.filtro;
        }

        if(dadosFilter.isAdministrador() || dadosFilter.contemPapel("ATD") || dadosFilter.isGestorDn()) {
            return this.filtro;
        }

        if(dadosFilter.isGestorDr()) {
            this.filtro.adicionaRestricao(" vue.id_und_atd_trab_fk in (select id_und_atd_trabalhador from und_atd_trabalhador where id_departamento_regional_fk in (:idsDrs)) ",
                    "idsDrs", dadosFilter.getIdsDepartamentoRegional());
        }


        return this.filtro;
    }

}
