package br.com.ezvida.rst.service;

import br.com.ezvida.rst.dao.filter.DadosFilter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Set;

@Stateless
public class ValidationService {

    @Inject
    private UnidadeAtendimentoTrabalhadorService unidadeAtendimentoTrabalhadorService;

    @Inject
    private UnidadeObraContratoUatService unidadeObraContratoUatService;

    public boolean validarFiltroDadosGestaoUnidadeSesi(DadosFilter dados, Long idUnidade) {
        boolean next = false;
        if (dados.isAdministrador() || dados.isGestorDn()) {
            next = true;
        } else if (dados.isGestorDr()) {
            Set<Long> listIdDepartamentosRegionais = dados.getIdsDepartamentoRegional();
            if (listIdDepartamentosRegionais != null && !listIdDepartamentosRegionais.isEmpty() && unidadeAtendimentoTrabalhadorService.existsByListDRIdAndIdUnidade(new ArrayList<>(listIdDepartamentosRegionais), idUnidade)) {
                next = true;
            }
        } else if (dados.isGetorUnidadeSESI()) {
            Set<Long> listIdUnidades = dados.getIdsUnidadeSESI();
            if (listIdUnidades != null && !listIdUnidades.isEmpty() && listIdUnidades.contains(idUnidade)) {
                next = true;
            }
        }

        return next;
    }

    public boolean validarFiltroDadosContrato(DadosFilter dados, Long idContrato) {
        boolean next = false;
        if (dados.isSuperUsuario()) {
            next = true;
        } else if (dados.isGestorDr()) {
            Set<Long> listIdDepartamentosRegionais = dados.getIdsDepartamentoRegional();
            if (listIdDepartamentosRegionais != null && !listIdDepartamentosRegionais.isEmpty() && unidadeObraContratoUatService.existisByDrs(new ArrayList<>(listIdDepartamentosRegionais), idContrato)) {
                next = true;
            }
        } else if (dados.isGetorUnidadeSESI()) {
            Set<Long> listIdUnidades = dados.getIdsUnidadeSESI();
            if (listIdUnidades != null && !listIdUnidades.isEmpty() && unidadeObraContratoUatService.existisByUnidades(new ArrayList<>(listIdUnidades), idContrato)) {
                next = true;
            }
        }

        return next;
    }
}
