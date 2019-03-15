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
import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import br.com.ezvida.rst.model.dto.UatVeiculoDTO;
import br.com.ezvida.rst.model.dto.UatVeiculoGroupedByTipoDTO;
import fw.core.exception.BusinessErrorException;
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
	public void deveRetornarExcpetionAoTentarListaUatVeiculosSemPermissao() throws Exception {
		LOGGER.info("Testando listar Uat Veiculos, sem permissão");

		exception.expect(UnauthorizedException.class);
		exception.expectMessage("Usuário não possui acesso autorizado.");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(false);

		uatVeiculoService.listAllUatVeiculoGroupedByTipo(1L, auditoria, dados);
	}

	@Test
	public void deveRetornarListaUatVeiculoPasseioGroupedByTipoDTO() throws Exception {
		LOGGER.info("Testando listar Uat Veiculos Passeio");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		UatVeiculo uatVeiculo = new UatVeiculo();
		uatVeiculo.setId(1L);
		uatVeiculo.setQuantidade(1);
		uatVeiculo.setUatVeiculoTipo(new UatVeiculoTipo(2L));

		List<UatVeiculo> listUatVeiculo = Arrays.asList(uatVeiculo);
		List<UatVeiculoTipo> listVeiculoTipo = Arrays.asList(new UatVeiculoTipo(2L));

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong())).thenReturn(listUatVeiculo);
		Mockito.when(uatVeiculoTipoService.listarTodos(auditoria)).thenReturn(listVeiculoTipo);
		List<UatVeiculoGroupedByTipoDTO> retorno = uatVeiculoService.listAllUatVeiculoGroupedByTipo(1L, auditoria,
				dados);

		assertNotNull(retorno);
		assertEquals(listUatVeiculo.get(0).getId(), retorno.get(0).getVeiculos().get(0).getId());
	}

	@Test
	public void deveRetornarListaUatVeiculoUnidadeMovelGroupedByTipoDTO() throws Exception {
		LOGGER.info("Testando listar Uat Veiculos Unidade Movel");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		UatVeiculoTipo uatVeiculoTipo = new UatVeiculoTipo();
		uatVeiculoTipo.setId(1L);

		UatVeiculoTipoAtendimento unidadeVeiculoTipoAtendimento = new UatVeiculoTipoAtendimento();
		unidadeVeiculoTipoAtendimento.setId(1L);
		unidadeVeiculoTipoAtendimento.setDescricao("Consulta");

		UatVeiculo uatVeiculo = new UatVeiculo();
		uatVeiculo.setId(1L);
		uatVeiculo.setQuantidade(1);
		uatVeiculo.setUatVeiculoTipo(uatVeiculoTipo);
		uatVeiculo.setUnidadeVeiculoTipoAtendimento(unidadeVeiculoTipoAtendimento);

		List<UatVeiculo> listUatVeiculo = Arrays.asList(uatVeiculo);
		List<UatVeiculoTipo> listVeiculoTipo = Arrays.asList(uatVeiculoTipo);

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong())).thenReturn(listUatVeiculo);
		Mockito.when(uatVeiculoTipoService.listarTodos(auditoria)).thenReturn(listVeiculoTipo);
		List<UatVeiculoGroupedByTipoDTO> retorno = uatVeiculoService.listAllUatVeiculoGroupedByTipo(1L, auditoria,
				dados);

		assertNotNull(retorno);
		assertEquals(listUatVeiculo.get(0).getId(), retorno.get(0).getVeiculos().get(0).getId());
	}

	@Test
	public void deveRetornarListaVaziaDeUatVeiculoGroupedByTipoDTO_whenUaTipoVeiculoForDiferenteDoVeiculoCadastrado()
			throws Exception {
		LOGGER.info("Testando listar Uat Veiculos com Veicuto Tipo diferente");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		UatVeiculoTipo uatVeiculoTipo = new UatVeiculoTipo();
		uatVeiculoTipo.setId(1L);

		UatVeiculo uatVeiculo = new UatVeiculo();
		uatVeiculo.setId(1L);
		uatVeiculo.setQuantidade(1);
		uatVeiculo.setUatVeiculoTipo(new UatVeiculoTipo(2L));

		List<UatVeiculo> listUatVeiculo = Arrays.asList(uatVeiculo);
		List<UatVeiculoTipo> listVeiculoTipo = Arrays.asList(uatVeiculoTipo);

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong())).thenReturn(listUatVeiculo);
		Mockito.when(uatVeiculoTipoService.listarTodos(auditoria)).thenReturn(listVeiculoTipo);
		List<UatVeiculoGroupedByTipoDTO> retorno = uatVeiculoService.listAllUatVeiculoGroupedByTipo(1L, auditoria,
				dados);

		assertTrue(CollectionUtils.isEmpty(retorno));
	}

	@Test
	public void deveSalvarListaDeVeiculosUnidadeMovelERetornarVeiculosSalvos() throws Exception {
		LOGGER.info("Testando salvar lista de Veiculos Unidade Movel");

		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO();
		uatVeiculoDTO.setIdUat(1L);
		uatVeiculoDTO.setIdVeiculoTipoAtendimento(1L);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		Mockito.doNothing().when(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong()))
				.thenReturn(new ArrayList<UatVeiculo>());

		List<UatVeiculoDTO> retorno = uatVeiculoService.salvar(listVeiculoDTO, auditoria, dados);

		Mockito.verify(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		assertTrue(CollectionUtils.isNotEmpty(retorno));
		assertEquals(listVeiculoDTO.get(0).getId(), retorno.get(0).getId());
	}

	@Test
	public void deveSalvarListaDeVeiculoPasseiolERetornarVeiculosSalvos() throws Exception {
		LOGGER.info("Testando salvar lista de Veiculo Passeio");

		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO();
		uatVeiculoDTO.setIdUat(1L);
		uatVeiculoDTO.setIdTipo(2L);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		Mockito.doNothing().when(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong()))
				.thenReturn(new ArrayList<UatVeiculo>());

		List<UatVeiculoDTO> retorno = uatVeiculoService.salvar(listVeiculoDTO, auditoria, dados);

		Mockito.verify(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		Mockito.verify(validationService).validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong());
		assertTrue(CollectionUtils.isNotEmpty(retorno));
		assertEquals(listVeiculoDTO.get(0).getId(), retorno.get(0).getId());
	}

	@Test
	public void deveRetornaExceptionQuandoTentarSalvarUatVeiculosUnidadeMovelSemPermissao() throws Exception {
		LOGGER.info("Testando salvar lista de Veiculos Unidade Movel, usuário sem permissão");

		exception.expect(UnauthorizedException.class);
		exception.expectMessage("Usuário não possui acesso autorizado.");

		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO();
		uatVeiculoDTO.setIdUat(1L);
		uatVeiculoDTO.setIdVeiculoTipoAtendimento(1L);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);
		ClienteAuditoria auditoria = new ClienteAuditoria();

		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		Mockito.doNothing().when(uatVeiculoDAO).salvar(Mockito.any(UatVeiculo.class));
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(false);

		uatVeiculoService.salvar(listVeiculoDTO, auditoria, dados);
	}

	@Test
	public void deveRetornarExceptionAoTentarCadastrarVeiculoDePasseioDuplicado() throws Exception {
		LOGGER.info("Testando Salvar Uat Veiculo Passeio duplicado");

		exception.expect(BusinessErrorException.class);
		exception.expectMessage("Já existe cadastrado um veículo do tipo Veículo de passeio para atendimento");

		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO();
		uatVeiculoDTO.setIdUat(1L);
		uatVeiculoDTO.setIdTipo(2L);
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		UatVeiculoTipo uatVeiculoTipo = new UatVeiculoTipo();
		uatVeiculoTipo.setId(2L);
		uatVeiculoTipo.setDescricao("Veículo de passeio para atendimento");

		UatVeiculo uatVeiculo = new UatVeiculo();
		uatVeiculo.setId(1L);
		uatVeiculo.setQuantidade(1);
		uatVeiculo.setUatVeiculoTipo(uatVeiculoTipo);
		List<UatVeiculo> listUatVeiculo = Arrays.asList(uatVeiculo);

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong())).thenReturn(listUatVeiculo);

		uatVeiculoService.salvar(listVeiculoDTO, auditoria, dados);
	}

	@Test
	public void deveRetornarExceptionAoTentarCadastrarVeiculoDoTipoUnidadeMovelDuplicado() throws Exception {
		LOGGER.info("Testando Salvar Uat Veiculo Unidade Móvel");

		exception.expect(BusinessErrorException.class);
		exception.expectMessage("Já existe cadastrado um veículo do tipo Consulta");

		UatVeiculoDTO uatVeiculoDTO = new UatVeiculoDTO();
		uatVeiculoDTO.setIdUat(1L);
		uatVeiculoDTO.setIdTipo(1L);
		uatVeiculoDTO.setIdVeiculoTipoAtendimento(1L);
		uatVeiculoDTO.setDescricao("Consulta");
		List<UatVeiculoDTO> listVeiculoDTO = Arrays.asList(uatVeiculoDTO);

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		UatVeiculoTipo uatVeiculoTipo = new UatVeiculoTipo();
		uatVeiculoTipo.setId(1L);

		UatVeiculoTipoAtendimento unidadeVeiculoTipoAtendimento = new UatVeiculoTipoAtendimento();
		unidadeVeiculoTipoAtendimento.setId(1L);

		UatVeiculo uatVeiculo = new UatVeiculo();
		uatVeiculo.setId(1L);
		uatVeiculo.setQuantidade(1);
		uatVeiculo.setUnidadeVeiculoTipoAtendimento(unidadeVeiculoTipoAtendimento);
		uatVeiculo.setUatVeiculoTipo(uatVeiculoTipo);
		List<UatVeiculo> listUatVeiculo = Arrays.asList(uatVeiculo);

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(Mockito.anyLong())).thenReturn(listUatVeiculo);

		uatVeiculoService.salvar(listVeiculoDTO, auditoria, dados);
	}

	@Test
	public void deveDesativarUatVeiculo() throws Exception {
		LOGGER.info("Testando Excluir Uat Veiculos");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);

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

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(false);

		uatVeiculoService.desativar(1L, 1L, auditoria, dados);
	}
	
	@Test
	public void deveRetornarExceptionAoTentarDesativarUatVeiculoComIdVeiculoNulo() throws Exception {
		LOGGER.info("Testando Excluir Uat Veiculos, com idVeiculo nulo");

		exception.expect(BusinessErrorException.class);
		exception.expectMessage("Parâmetro idVeiculo é obrigatório.");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
		DadosFilter dados = new DadosFilter();

		uatVeiculoService.desativar(null, 1L, auditoria, dados);
	}
	
	@Test
	public void deveRetornarExceptionAoTentarDesativarUatVeiculoComIdUatNulo() throws Exception {
		LOGGER.info("Testando Excluir Uat Veiculos, com idUat nulo");

		exception.expect(BusinessErrorException.class);
		exception.expectMessage("Parâmetro idUat é obrigatório.");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
		DadosFilter dados = new DadosFilter();

		uatVeiculoService.desativar(1L, null, auditoria, dados);
	}
}
