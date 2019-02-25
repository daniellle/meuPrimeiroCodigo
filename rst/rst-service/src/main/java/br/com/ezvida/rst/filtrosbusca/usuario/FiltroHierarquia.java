package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;

public class FiltroHierarquia extends FiltroUsuario {


    @Override
    public Filtro aplica(UsuarioFilter usuarioFilter, DadosFilter dadosFilter, Usuario usuario) {
        if(dadosFilter.isAdministrador() || dadosFilter.contemPapel("ATD")) {
            return this.filtro;
        }

        this.filtro.adicionaRestricao("p_hierarquia is not null");

        if(dadosFilter.contemPapel("GDNA")) {
            this.filtro.adicionaRestricao("p_hierarquia >= :hierarquia", "hierarquia", usuario.getHierarquia());
            return this.filtro;
        }

        this.filtro.adicionaRestricao("p_hierarquia > :hierarquia", "hierarquia", usuario.getHierarquia());

        return this.filtro;
    }
}
