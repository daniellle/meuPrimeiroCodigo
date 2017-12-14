package br.com.ezvida.rst.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import fw.core.service.BaseService;

public class EmailEmpresaServiceTest extends BaseService{

	private static final long serialVersionUID = -829623154644692875L;
	
	@InjectMocks
	@Spy
	private  EmailEmpresaService service;
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testBusarPorIdNull() {
		String msg = "";
		try {
			service.pesquisarPorId(null);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		Assert.assertEquals(msg, "Id de consulta está nulo.");
	}

}
