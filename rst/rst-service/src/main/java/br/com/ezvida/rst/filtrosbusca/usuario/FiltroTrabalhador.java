package br.com.ezvida.rst.filtrosbusca.usuario;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.filtrosbusca.Filtro;
import br.com.ezvida.rst.model.Usuario;

import java.util.Arrays;

public class FiltroTrabalhador extends FiltroUsuario {

    @Override
    public Filtro aplica(UsuarioFilter usuarioFilter, DadosFilter dadosFilter, Usuario usuario) {

        if(usuarioFilter.getIdEmpresa() != null) {
            this.filtro.adicionaRestricao("vue.id_empresa_fk = :idEmpresa", "idEmpresa", usuarioFilter.getIdEmpresa());
        }

        if(dadosFilter.isAdministrador() || dadosFilter.contemPapel("ATD") || dadosFilter.isGestorDn()) {
            return this.filtro;
        }

//        if(dadosFilter.isGestorDr()) {
//            StringBuilder str = new StringBuilder();
//            if(usuarioFilter.getIdEmpresa() != null) {
//                str.append(" and ");
//            }
//            str.append(" vue.login ");
//            str.append(" in (select no_cpf from trabalhador join emp_trabalhador on id_trabalhador_fk = id_trabalhador where id_empresa_fk ");
//            str.append(" in (select id_empresa from empresa join und_obra on id_empresa = id_empresa_fk ");
//            str.append(" join und_obra_contrato_uat on id_und_obra_contrato_uat = id_und_obra_fk where id_und_atd_trabalhador_fk ");
//            str.append(" in (select id_und_atd_trabalhador from und_atd_trabalhador where id_departamento_regional_fk in (:idsDrs)))) ");
//
//            if(usuarioFilter.getIdDepartamentoRegional() != null) {
//                this.filtro.adicionaRestricao(str.toString(), "idsDrs", Arrays.asList(usuarioFilter.getIdDepartamentoRegional()));
//            } else {
//                this.filtro.adicionaRestricao(str.toString(), "idsDrs", dadosFilter.getIdsDepartamentoRegional());
//            }
//        }

        if(dadosFilter.isGetorUnidadeSESI()) {
            StringBuilder str = new StringBuilder();
            if(usuarioFilter.getIdEmpresa() != null) {
                str.append(" and ");
            }
            str.append(" vue.login in ");
            str.append(" (select no_cpf from trabalhador join emp_trabalhador on id_trabalhador_fk = id_trabalhador where id_empresa_fk ");
            str.append(" in (select id_empresa from empresa join und_obra on id_empresa = id_empresa_fk ");
            str.append(" join und_obra_contrato_uat on id_und_obra_contrato_uat = id_und_obra_fk ");
            str.append(" where id_und_atd_trabalhador_fk in (:idsUnidadesSesi))) ");

            this.filtro.adicionaRestricao(str.toString(), "idsUnidadesSesi", dadosFilter.getIdsUnidadeSESI());
        }

        if(dadosFilter.isGestorEmpresa()) {
            StringBuilder str = new StringBuilder();
            if(usuarioFilter.getIdEmpresa() != null) {
                str.append(" and ");
            }
            str.append(" vue.login in  ");
            str.append(" (select no_cpf from trabalhador join emp_trabalhador on id_trabalhador_fk = id_trabalhador where vue.id_empresa_fk in (:idsEmpresas)) ");

            this.filtro.adicionaRestricao(str.toString(), "idsEmpresas", dadosFilter.getIdsEmpresa());
        }

        return this.filtro;
    }

}
