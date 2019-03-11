package br.com.ezvida.rst.service;

import br.com.ezvida.rst.dao.filter.DadosFilter;
import fw.core.service.BaseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceTest extends BaseService {

    @InjectMocks
    private ValidationService validationService;

    @Mock
    private UnidadeAtendimentoTrabalhadorService unidadeAtendimentoTrabalhadorService;

    private static final String ADM_PERFIL_SIGLA = "ADM";

    public static final String GESTOR_DR_SIGLA = "GDRA";

    public static final String GESTOR_UNIDADE_SESI_SIGLA = "GUS";

    private DadosFilter dadosFilter;


    private Long idUnidade;

    @Before
    public void setup() {
        this.idUnidade = 1L;
    }

    @Test
    public void testValidationADM() {
        this.dadosFilter = new DadosFilter(
                new HashSet<>(Arrays.asList(ADM_PERFIL_SIGLA)), null, null, null,
                null, null, null, null);
        Assert.assertTrue(this.validationService.validarFiltroDadosGestaoUnidadeSesi(this.dadosFilter, this.idUnidade));
    }

    @Test
    public void testValidationGDRValid() {
        List<Long> listIdDR = Arrays.asList(1L);
        Mockito.when(this.unidadeAtendimentoTrabalhadorService.
                existsByListDRIdAndIdUnidade(listIdDR, this.idUnidade)).thenReturn(true);
        this.dadosFilter = new DadosFilter(
                new HashSet<>(Arrays.asList(GESTOR_DR_SIGLA)), new HashSet<>(listIdDR), null, null,
                null, null, null, null);
        Assert.assertTrue(this.validationService.validarFiltroDadosGestaoUnidadeSesi(dadosFilter, this.idUnidade));
    }

    @Test
    public void testValidationGUSValid() {
        List<Long> listIdUnidades = Arrays.asList(this.idUnidade);
        this.dadosFilter = new DadosFilter(
                new HashSet<>(Arrays.asList(GESTOR_UNIDADE_SESI_SIGLA)), null, null, null,
                null, null, null, new HashSet<>(listIdUnidades));
        Assert.assertTrue(this.validationService.validarFiltroDadosGestaoUnidadeSesi(dadosFilter, this.idUnidade));
    }

    @Test
    public void testValidationGUSInvalid() {
        List<Long> listIdUnidades = Arrays.asList(2L);
        this.dadosFilter = new DadosFilter(
                new HashSet<>(Arrays.asList(GESTOR_UNIDADE_SESI_SIGLA)), null, null, null,
                null, null, null, new HashSet<>(listIdUnidades));
        Assert.assertFalse(this.validationService.validarFiltroDadosGestaoUnidadeSesi(dadosFilter, this.idUnidade));
    }


}
