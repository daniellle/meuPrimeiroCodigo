package br.com.ezvida.rst.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.UatEquipamentoTipoDAO;
import br.com.ezvida.rst.model.UatEquipamentoTipo;

@RunWith(MockitoJUnitRunner.class)
public class UatEquipamentoTipoServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoTipoServiceTest.class);

	@InjectMocks
	private UatEquipamentoTipoService uatEquipamentoTipoService;
	
	@Mock
	private UatEquipamentoTipoDAO uatEquipamentoTipoDAO;

	@Test
	public void deveTrazerListaComTodosEquipamentoTipo() throws Exception {
		LOGGER.info("Testando listar Todos");
		
		List<UatEquipamentoTipo> list = Arrays.asList(Mockito.mock(UatEquipamentoTipo.class));
		
		Mockito.when(uatEquipamentoTipoDAO.pesquisarTodos()).thenReturn(list);
		
		List<UatEquipamentoTipo> retorno = uatEquipamentoTipoService.listarTodos();
		
		assertEquals(list, retorno);
	}
	

}
