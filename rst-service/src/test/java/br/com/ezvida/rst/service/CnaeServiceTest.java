package br.com.ezvida.rst.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class CnaeServiceTest extends BaseService{

	private static final long serialVersionUID = -3575351022283835059L;
	
	@InjectMocks
	@Spy
	private CnaeService service;
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void pesquisarPaginadoTest() throws Exception {
		boolean mensagemErro = false;
		try {
			service.pesquisarPaginado(null);
		} catch (Exception e) {
			mensagemErro = true;
		}

		Assert.assertEquals(mensagemErro, true);
	}

}
