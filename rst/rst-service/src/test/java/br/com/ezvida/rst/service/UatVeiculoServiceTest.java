package br.com.ezvida.rst.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatVeiculoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatVeiculo;
import br.com.ezvida.rst.model.UatVeiculoTipo;
import br.com.ezvida.rst.model.dto.UatVeiculoDTO;
import br.com.ezvida.rst.model.dto.UatVeiculoGroupedByTipoDTO;
import fw.security.exception.UnauthorizedException;

@RunWith(MockitoJUnitRunner.class)
public class UatVeiculoServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoServiceTest.class);

	@InjectMocks
	private UatVeiculoService uatVeiculoService;

	@Mock
	private UatVeiculoDAO uatVeiculoDAO;

	@Mock
	private ValidationService validationService;
	
	@Mock
	private UatVeiculoTipoService uatVeiculoTipoService;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void deveTrazerListaComTodosVeiculos() throws Exception {
		LOGGER.info("Testando listar Todos");
		List<UatVeiculo> list = Arrays.asList(Mockito.mock(UatVeiculo.class));
		Mockito.doReturn(list).when(uatVeiculoDAO).pesquisarTodos();
		List<UatVeiculo> retorno = uatVeiculoService.listarTodos();
		assertEquals(list, retorno);
	}
	
	@Test
	public void deveRetornarExcpetionAoTentarListaUatVeiculosSemPermissao() throws Exception {
		LOGGER.info("Testando listar Uat Veiculos, sem permissão");
		
		exception.expect(UnauthorizedException.class);
	    exception.expectMessage("Usuário não possui acesso autorizado.");
	    
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();
		
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class), Mockito.anyLong()))
		.thenReturn(false);
		
		uatVeiculoService.listAllUatVeiculoGroupedByTipo(1L, auditoria, dados);
	}
	
	@Test
	public void deveRetornarListaUatVeiculoGroupedByTipoDTO() throws Exception {
		LOGGER.info("Testando listar Uat Veiculos, sem permissão");
		
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();
		UatVeiculo uatVeiculo = new UatVeiculo();
		uatVeiculo.setId(1L);
		uatVeiculo.setQuantidade(1);
		uatVeiculo.setUatVeiculoTipo(new UatVeiculoTipo(2L));
		List<UatVeiculo> listUatVeiculo = Arrays.asList(uatVeiculo );
		List<UatVeiculoTipo> listVeiculoTipo = Arrays.asList(new UatVeiculoTipo(2L));
		
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class), Mockito.anyLong()))
		.thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong())).thenReturn(listUatVeiculo);
		Mockito.when(uatVeiculoTipoService.listarTodos()).thenReturn(listVeiculoTipo);
		List<UatVeiculoGroupedByTipoDTO> retorno = uatVeiculoService.listAllUatVeiculoGroupedByTipo(1L, auditoria, dados);
		
		assertNotNull(retorno);
		assertEquals(listUatVeiculo.get(0).getId(), retorno.get(0).getVeiculos().get(0).getId());
	}

	@Test
	public void deveSalvarListaDeVeiculosUnidadeMovelERetornarVeiculosSalvos_userWithPermissao() throws Exception {
		LOGGER.info("Testando salvar lista de Veiculos Unidade Movel, usuário com permissão");
		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO();
		uatVeiculoDTO.setIdVeiculoTipoAtendimento(1L);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();
		
		Mockito.doNothing().when(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class), Mockito.anyLong()))
			.thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong())).thenReturn(new ArrayList<UatVeiculo>());

		List<UatVeiculoDTO> retorno = uatVeiculoService.salvar(listVeiculoDTO, auditoria, dados);

		Mockito.verify(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		assertTrue(CollectionUtils.isNotEmpty(retorno));
	}
	
	@Test
	public void retornaExceptionAoTentarSalvarUatVeiculosSemPermissao() throws Exception  {
		LOGGER.info("Testando salvar lista de Veiculos Unidade Movel, usuário sem permissão");
		
		exception.expect(UnauthorizedException.class);
	    exception.expectMessage("Usuário não possui acesso autorizado.");
	    
		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO();
		uatVeiculoDTO.setIdVeiculoTipoAtendimento(1L);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();
		
		Mockito.doNothing().when(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class), Mockito.anyLong()))
			.thenReturn(false);

		uatVeiculoService.salvar(listVeiculoDTO, auditoria, dados);
	}
	
	@Test
	public void deveDesativarUatVeiculo() throws Exception {
		LOGGER.info("Testando Excluir Uat Veiculos");
		
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
		
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class), Mockito.anyLong()))
		.thenReturn(true);
		
		uatVeiculoService.desativar(1L, 1L, auditoria, new DadosFilter());
		
		Mockito.verify(uatVeiculoDAO).desativar((Mockito.anyLong()));
	}
	
	@Test
	public void deveRetornarExceptionAoTentarDesativarUatVeiculoSemPermissao() throws Exception {
		LOGGER.info("Testando Excluir Uat Veiculos, sem permissão");
		
		exception.expect(UnauthorizedException.class);
	    exception.expectMessage("Usuário não possui acesso autorizado.");
	    
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
		DadosFilter dados = new DadosFilter();
		
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class), Mockito.anyLong()))
		.thenReturn(false);
		
		uatVeiculoService.desativar(1L, 1L, auditoria, dados);
	}
	
}
