package br.com.ezvida.rst.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.UatVeiculoTipoDAO;
import br.com.ezvida.rst.model.UatVeiculoTipo;

@RunWith(MockitoJUnitRunner.class)
public class UatVeiculoTipoServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoTipoServiceTest.class);

	@InjectMocks
	private UatVeiculoTipoService uatVeiculoTipoService;

	@Mock
	private UatVeiculoTipoDAO uatVeiculoTipoDAO;

	@Before
	public void inicializar() throws Exception {
	}

	@Test
	public void deveTrazerListaComTodosTiposVeiculos() throws Exception {
		LOGGER.info("Testando listarTodos");
		List<UatVeiculoTipo> list = Arrays.asList(Mockito.mock(UatVeiculoTipo.class));
		
		Mockito.doReturn(list).when(uatVeiculoTipoDAO).pesquisarTodos();
		
		List<UatVeiculoTipo> retorno = uatVeiculoTipoService.listarTodos();
		
		assertEquals(list, retorno);
	}
	
	@Test
	public void deveRetornarUmUatVeiculoTipo() throws Exception {
		LOGGER.info("Testando buscar Uat Veiculo Tipo por ID...");
		UatVeiculoTipo uatVeiculoTipo = Mockito.mock(UatVeiculoTipo.class);
		
		Mockito.doReturn(uatVeiculoTipo).when(uatVeiculoTipoDAO).pesquisarPorId(Mockito.anyLong());
		
		UatVeiculoTipo retorno = uatVeiculoTipoService.findById(Mockito.anyLong());
		
		assertEquals(uatVeiculoTipo, retorno);
	}

}
