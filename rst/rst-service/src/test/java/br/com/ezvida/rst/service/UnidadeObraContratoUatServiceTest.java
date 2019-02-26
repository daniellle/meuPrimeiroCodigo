package br.com.ezvida.rst.service;

import fw.core.service.BaseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class UnidadeObraContratoUatServiceTest extends BaseService {

	private static final long serialVersionUID = 1L;
	
	@InjectMocks
    @Spy
    private UnidadeObraContratoUatService service;

    @Before
    public void inicializar() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validarComCnpjNull(){
        String msg = "";
        try{
            service.validarPorEmpresa(null);
        }catch (Exception e){
            msg = getMensagem("app_rst_unidade_invalida",
                    getMensagem("app_rst_label_unidade_obra"));
        }
        Assert.assertEquals(msg, getMensagem("app_rst_unidade_invalida", getMensagem("app_rst_label_unidade_obra")));
    }

    @Test
    public void validarComCnpjInvalido(){
        String msg = "";
        try{
            service.validarPorEmpresa("12345678901234");
        }catch (Exception e){
            msg = getMensagem("app_rst_unidade_invalida",
                    getMensagem("app_rst_label_unidade_obra"));
        }
        Assert.assertEquals(msg, getMensagem("app_rst_unidade_invalida", getMensagem("app_rst_label_unidade_obra")));
    }

}
