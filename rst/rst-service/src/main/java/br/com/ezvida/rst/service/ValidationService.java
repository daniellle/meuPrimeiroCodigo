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
        if (dados.isAdministrador() || dados.isGestorDn()) {
            return true;
        } else if (dados.isGestorDr()) {
            Set<Long> listIdDepartamentosRegionais = dados.getIdsDepartamentoRegional();
            return listIdDepartamentosRegionais != null && !listIdDepartamentosRegionais.isEmpty() && unidadeAtendimentoTrabalhadorService.existsByListDRIdAndIdUnidade(new ArrayList<>(listIdDepartamentosRegionais), idUnidade);
        } else if (dados.isGetorUnidadeSESI()) {
            Set<Long> listIdUnidades = dados.getIdsUnidadeSESI();
            return listIdUnidades != null && !listIdUnidades.isEmpty() && listIdUnidades.contains(idUnidade);
        }

        return false;
    }

    public boolean validarFiltroDadosContrato(DadosFilter dados, Long idContrato) {
        if (dados.isSuperUsuario()) {
            return true;
        } else if (dados.isGestorDr()) {
            Set<Long> listIdDepartamentosRegionais = dados.getIdsDepartamentoRegional();
            return listIdDepartamentosRegionais != null && !listIdDepartamentosRegionais.isEmpty() && unidadeObraContratoUatService.existisByDrs(new ArrayList<>(listIdDepartamentosRegionais), idContrato);
        } else if (dados.isGetorUnidadeSESI()) {
            Set<Long> listIdUnidades = dados.getIdsUnidadeSESI();
            return listIdUnidades != null && !listIdUnidades.isEmpty() && unidadeObraContratoUatService.existisByUnidades(new ArrayList<>(listIdUnidades), idContrato);
        }

        return false;
    }
}
