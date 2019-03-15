package br.com.ezvida.rst.service;

import static org.junit.Assert.assertEquals;
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
import br.com.ezvida.rst.dao.UatEquipamentoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatEquipamento;
import br.com.ezvida.rst.model.UatEquipamentoArea;
import br.com.ezvida.rst.model.UatEquipamentoTipo;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.dto.UatEquipamentoDTO;
import br.com.ezvida.rst.model.dto.UatEquipamentoGroupedByAreaDTO;
import fw.core.exception.BusinessErrorException;
import fw.security.exception.UnauthorizedException;

@RunWith(MockitoJUnitRunner.class)
public class UatEquipamentoServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoServiceTest.class);

	@InjectMocks
	private UatEquipamentoService uatEquipamentoService;

	@Mock
	private UatEquipamentoDAO uatEquipamentoDAO;

	@Mock
	private ValidationService validationService;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void deveTrazerListaComTodosEquipamentosAgrupadosPorArea() throws Exception {
		LOGGER.info("Testando listar Todos equipamentos agrupados por area");

		List<UatEquipamento> list = createListEquipamentosFake();

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		Mockito.when(uatEquipamentoDAO.listAllUatEquipamentoByIdUatAndAtivo(Mockito.anyLong())).thenReturn(list);
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);

		List<UatEquipamentoGroupedByAreaDTO> retorno = uatEquipamentoService
				.listarTodosEquipamentosPorIdUatAgrupadosPorArea(1L, auditoria, dados);

		assertTrue(CollectionUtils.isNotEmpty(retorno));
		assertTrue(CollectionUtils.isNotEmpty(retorno.get(0).getEquipamentos()));
	}

	@Test
	public void deveRetornarExceptionQuandoTentarListarEquipamentosAgrupadosPorAreaSemPermissao() throws Exception {
		LOGGER.info("Testando listar Todos equipamentos agrupados por area, sem permissão");

		exception.expect(UnauthorizedException.class);
		exception.expectMessage("Usuário não possui acesso autorizado.");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(false);

		uatEquipamentoService.listarTodosEquipamentosPorIdUatAgrupadosPorArea(1L, auditoria, dados);
	}

	@Test
	public void deveDesativarEquipamento() throws Exception {
		LOGGER.info("Testando exlcluir equipamento por id de UAT");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);

		uatEquipamentoService.desativar(1L, 1L, auditoria, dados);

		Mockito.verify(validationService).validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong());
		Mockito.verify(uatEquipamentoDAO).desativar(Mockito.anyLong());
	}

	@Test
	public void deveRetornarExceptionQuandoTentarDesativarEquipamentoPorIdUatSemPermissao() throws Exception {
		LOGGER.info("Testando exlcluir equipamento por id de UAT, sem permissão");

		exception.expect(UnauthorizedException.class);
		exception.expectMessage("Usuário não possui acesso autorizado.");
		
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		DadosFilter dados = new DadosFilter();

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(false);

		uatEquipamentoService.desativar(1L, 1L, auditoria, dados);
		
		Mockito.verify(validationService).validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong());
	}

	@Test
	public void deveRetornarExceptionAoTentarDesativarUatVeiculoComIdEquipamentoNulo() throws Exception {
		LOGGER.info("Testando Excluir Uat Equipamento, com idEquipamento nulo");

		exception.expect(BusinessErrorException.class);
		exception.expectMessage("Parâmetro idEquipamento é obrigatório.");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
		DadosFilter dados = new DadosFilter();

		uatEquipamentoService.desativar(null, 1L, auditoria, dados);
	}

	@Test
	public void deveRetornarExceptionAoTentarDesativarUatVeiculoComIdUatNulo() throws Exception {
		LOGGER.info("Testando Excluir Uat Equipamento, com idUat nulo");

		exception.expect(BusinessErrorException.class);
		exception.expectMessage("Parâmetro idUat é obrigatório.");

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
		DadosFilter dados = new DadosFilter();

		uatEquipamentoService.desativar(1L, null, auditoria, dados);
	}

	@Test
	public void deveSalvarListaDeEquipamentosERetornarEquipamentosSalvos() throws Exception {
		LOGGER.info("Testando salvar lista de Equipamentos");

		UatEquipamentoDTO uatEquipamentoDTO = new UatEquipamentoDTO();
		uatEquipamentoDTO.setIdUat(1L);
		uatEquipamentoDTO.setIdTipo(1L);
		List<UatEquipamentoDTO> listUatEquipamentoDTO = Arrays.asList(uatEquipamentoDTO);

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		Mockito.doNothing().when(uatEquipamentoDAO).salvar(Mockito.any(UatEquipamento.class));
		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatEquipamentoDAO.listAllUatEquipamentoByIdUatAndAtivo(Mockito.anyLong()))
				.thenReturn(new ArrayList<UatEquipamento>());

		List<UatEquipamentoDTO> retorno = uatEquipamentoService.salvar(listUatEquipamentoDTO, auditoria, dados);

		Mockito.verify(validationService).validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong());
		Mockito.verify(uatEquipamentoDAO).salvar(Mockito.any(UatEquipamento.class));
		assertTrue(CollectionUtils.isNotEmpty(retorno));
		assertEquals(listUatEquipamentoDTO.get(0).getId(), retorno.get(0).getId());
	}
	
	@Test
	public void deveRetornarExceptionQuandoTentarSalvarListaDeEquipamentosSemPermissao() throws Exception {
		LOGGER.info("Testando salvar lista de Equipamentos, sem permissão");

		exception.expect(UnauthorizedException.class);
		exception.expectMessage("Usuário não possui acesso autorizado.");
		
		UatEquipamentoDTO uatEquipamentoDTO = new UatEquipamentoDTO();
		uatEquipamentoDTO.setIdUat(1L);
		uatEquipamentoDTO.setIdTipo(1L);
		List<UatEquipamentoDTO> listUatEquipamentoDTO = Arrays.asList(uatEquipamentoDTO);

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(false);

		uatEquipamentoService.salvar(listUatEquipamentoDTO, auditoria, dados);
	}
	
	@Test
	public void deveRetornarExceptionQuandoTentarSalvarListaDeEquipamentosComEquipamentoJaCadastrado() throws Exception {
		LOGGER.info("Testando salvar lista de Equipamentos com equipamento já cadastrado");

		exception.expect(BusinessErrorException.class);
		exception.expectMessage("Já existe cadastrado um equipamento do tipo Ortho Rater.");
		
		UatEquipamentoDTO uatEquipamentoDTO = new UatEquipamentoDTO();
		uatEquipamentoDTO.setIdUat(1L);
		uatEquipamentoDTO.setDescricao("Ortho Rater");
		uatEquipamentoDTO.setIdTipo(1L);
		List<UatEquipamentoDTO> listUatEquipamentoDTO = Arrays.asList(uatEquipamentoDTO);

		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
		DadosFilter dados = new DadosFilter();

		Mockito.when(validationService.validarFiltroDadosGestaoUnidadeSesi(Mockito.any(DadosFilter.class),
				Mockito.anyLong())).thenReturn(true);
		Mockito.when(uatEquipamentoDAO.listAllUatEquipamentoByIdUatAndAtivo(Mockito.anyLong()))
		.thenReturn(createListEquipamentosFake());

		uatEquipamentoService.salvar(listUatEquipamentoDTO, auditoria, dados);
	}

	private List<UatEquipamento> createListEquipamentosFake() {

		UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador = new UnidadeAtendimentoTrabalhador();
		unidadeAtendimentoTrabalhador.setId(1L);

		UatEquipamentoArea uatEquipamentoArea1 = new UatEquipamentoArea();
		uatEquipamentoArea1.setId(1L);
		uatEquipamentoArea1.setDescricao("Saúde");

		UatEquipamentoArea uatEquipamentoArea2 = new UatEquipamentoArea();
		uatEquipamentoArea2.setId(2L);
		uatEquipamentoArea2.setDescricao("Engenharia");

		UatEquipamentoTipo uatEquipamentoTipo1 = new UatEquipamentoTipo();
		uatEquipamentoTipo1.setId(1L);
		uatEquipamentoTipo1.setDescricao("Ortho Rater");
		uatEquipamentoTipo1.setUatEquipamentoArea(uatEquipamentoArea1);

		UatEquipamentoTipo uatEquipamentoTipo2 = new UatEquipamentoTipo();
		uatEquipamentoTipo2.setId(2L);
		uatEquipamentoTipo2.setDescricao("Tabela completa (Snell, Jaeger e Ishihara)");
		uatEquipamentoTipo2.setUatEquipamentoArea(uatEquipamentoArea1);

		UatEquipamentoTipo uatEquipamentoTipo3 = new UatEquipamentoTipo();
		uatEquipamentoTipo3.setId(3L);
		uatEquipamentoTipo3.setDescricao("Dosímetro de ruído com frequência de banda de oitava");
		uatEquipamentoTipo3.setUatEquipamentoArea(uatEquipamentoArea2);

		UatEquipamento uatEquipamento1 = new UatEquipamento();
		uatEquipamento1.setId(1L);
		uatEquipamento1.setUnidadeAtendimentoTrabalhador(unidadeAtendimentoTrabalhador);
		uatEquipamento1.setQuantidade(1);
		uatEquipamento1.setUatEquipamentoTipo(uatEquipamentoTipo1);

		UatEquipamento uatEquipamento2 = new UatEquipamento();
		uatEquipamento2.setId(2L);
		uatEquipamento2.setUnidadeAtendimentoTrabalhador(unidadeAtendimentoTrabalhador);
		uatEquipamento2.setQuantidade(2);
		uatEquipamento2.setUatEquipamentoTipo(uatEquipamentoTipo2);

		UatEquipamento uatEquipamento3 = new UatEquipamento();
		uatEquipamento3.setId(2L);
		uatEquipamento3.setUnidadeAtendimentoTrabalhador(unidadeAtendimentoTrabalhador);
		uatEquipamento3.setQuantidade(2);
		uatEquipamento3.setUatEquipamentoTipo(uatEquipamentoTipo3);

		return Arrays.asList(uatEquipamento1, uatEquipamento2, uatEquipamento3);
	}
}
