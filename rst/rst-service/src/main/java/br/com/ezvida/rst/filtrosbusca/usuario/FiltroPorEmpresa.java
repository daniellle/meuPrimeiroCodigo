package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;

import java.util.Arrays;

public class FiltroPorEmpresa extends FiltroUsuario {

    @Override
    public Filtro aplica(UsuarioFilter usuarioFilter, DadosFilter dadosFilter, Usuario usuario) {
        if(usuarioFilter.getIdEmpresa() != null) {
            this.filtro.adicionaRestricao("vue.id_empresa_fk = :idEmpresa", "idEmpresa", usuarioFilter.getIdEmpresa());
            return this.filtro;
        }

        if(dadosFilter.isAdministrador() || dadosFilter.contemPapel("ATD") || dadosFilter.isGestorDn()) {
            return this.filtro;
        }

        if(dadosFilter.isGestorDr()) {
            StringBuilder str = new StringBuilder();
            str.append(" vue.id_empresa_fk in ");
            str.append(" (select id_empresa from empresa join und_obra on id_emrpesa = vue.id_empresa_fk ");
            str.append(" join und_obra_contrato_uat where id_und_atd_trabalahdor_fk in ");
            str.append(" (select id_und_atd_trabalhador from und_atd_trabalhador ");
            str.append(" where id_departamento_regional_fk in (:idsDrs))) ");

            if(usuarioFilter.getIdDepartamentoRegional() != null) {
                this.filtro.adicionaRestricao(str.toString(), "idsDrs", Arrays.asList(usuarioFilter.getIdDepartamentoRegional()));
            } else {
                this.filtro.adicionaRestricao(str.toString(), "idsDrs", dadosFilter.getIdsDepartamentoRegional());
            }

            this.filtro.adicionaRestricao("vue.id_departamento_regional_fk in (:idsDrs)", "idsDrs", dadosFilter.getIdsDepartamentoRegional());
        }

        if(dadosFilter.isGetorUnidadeSESI()) {
            StringBuilder str = new StringBuilder();
            str.append(" vue.id_empresa_fk in ");
            str.append(" (select id_empresa from empresa ");
            str.append(" join und_obra on id_empresa = vue.id_empresa_fk ");
            str.append(" join und_obra_contrato_uat where id_und_atd_trabalahdor_fk in (:idsUnidadesSesi))");

            this.filtro.adicionaRestricao(str.toString(), "idsUnidadesSesi", dadosFilter.getIdsUnidadeSESI());
        }

        return this.filtro;
    }

}
