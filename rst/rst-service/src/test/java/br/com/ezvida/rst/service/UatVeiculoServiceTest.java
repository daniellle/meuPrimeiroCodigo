package br.com.ezvida.rst.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.UatVeiculoDAO;
import br.com.ezvida.rst.model.UatVeiculo;
import br.com.ezvida.rst.model.dto.UatVeiculoDTO;

@RunWith(MockitoJUnitRunner.class)
public class UatVeiculoServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoServiceTest.class);

	@InjectMocks
	private UatVeiculoService uatVeiculoService;

	@Mock
	private UatVeiculoDAO uatVeiculoDAO;

	@Before
	public void inicializar() throws Exception {
	}

	@Test
	public void deveTrazerListaComTodosVeiculos() throws Exception {
		LOGGER.info("Testando listar Todos");
		List<UatVeiculo> list = Arrays.asList(Mockito.mock(UatVeiculo.class));
		Mockito.doReturn(list).when(uatVeiculoDAO).pesquisarTodos();
		List<UatVeiculo> retorno = uatVeiculoService.listarTodos();
		assertEquals(list, retorno);
	}

	@Test
	public void deveSalvarListaDeVeiculosUnidadeMovelERetornarVeiculosSalvos() throws Exception {
		LOGGER.info("Testando salvar lista de Veiculos Unidade Movel");
		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO(null, 1, 1L, 1L);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);
		
		Mockito.doNothing().when(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));

		List<UatVeiculoDTO> retorno = uatVeiculoService.salvar(listVeiculoDTO);

		Mockito.verify(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		assertTrue(CollectionUtils.isNotEmpty(retorno));
	}
	
	@Test
	public void deveSalvarListaDeVeiculosPasseioERetornarVeiculosSalvos() throws Exception {
		LOGGER.info("Testando salvar lista de Veiculos Passeio");
		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO(null, 1, 1L, null);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);
		
		Mockito.doNothing().when(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));

		List<UatVeiculoDTO> retorno = uatVeiculoService.salvar(listVeiculoDTO);

		Mockito.verify(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		assertTrue(CollectionUtils.isNotEmpty(retorno));
	}

}
