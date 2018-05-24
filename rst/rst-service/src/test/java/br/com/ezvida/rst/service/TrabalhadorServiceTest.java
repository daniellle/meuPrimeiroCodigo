package br.com.ezvida.rst.service;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Dependente;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.TrabalhadorDependente;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class TrabalhadorServiceTest extends BaseService {

	private static final long serialVersionUID = 695750076436145730L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorServiceTest.class);

	@InjectMocks
	private TrabalhadorService trabalhadorService;

	@Mock
	private TrabalhadorDAO trabalhadorDAO;

	@Mock
	private TrabalhadorDependenteService trabalhadorDependenteService;

	@Mock
	private TelefoneTrabalhadorService telefoneTrabalhadorService;

	@Mock
	private EmailTrabalhadorService emailTrabalhadorService;

	@Mock
	private EmpresaTrabalhadorService empresaTrabalhadorService;

	@Mock
	private EnderecoTrabalhadorService enderecoTrabalhadorService;

	@Mock
	private UsuarioService usuarioService;
	
	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.TRABALHADOR);
	}

	@Test
	public void pesquisar() throws Exception {
		LOGGER.debug("Testando pesquisar Trabalhador");
		TrabalhadorFilter filtro = new TrabalhadorFilter();
		filtro.setCpf("00000000000");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			trabalhadorService.pesquisarPaginado(filtro, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");

	}

	@Test
	public void buscarComId() throws Exception {
		LOGGER.debug("Testando buscar Trabalhador");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			TrabalhadorFilter filtro = new TrabalhadorFilter();
			filtro.setId(new Long(1));
			filtro.setAplicarDadosFilter(true);
			trabalhadorService.buscarPorId(filtro, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void buscarComIdNull() throws Exception {
		LOGGER.debug("Testando buscar Trabalhador");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			trabalhadorService.buscarPorId(null, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

	@Test
	public void salvarTrabalhadorComCpfInvalido() throws Exception {
		LOGGER.debug("Testando salvar Trabalhador com cpf inválido");
		Trabalhador trabalhador = new Trabalhador();
		trabalhador.setCpf("12345678901");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			trabalhadorService.salvar(trabalhador,this.auditoria);
		} catch (BusinessException e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cpf")));
	}

	@Test
	public void salvarTrabalhadorComNitInvalido() throws Exception {
		LOGGER.debug("Testando salvar Trabalhador com nit inválido");
		Trabalhador trabalhador = new Trabalhador();
		trabalhador.setNit("12345678901");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			trabalhadorService.salvar(trabalhador,this.auditoria);
		} catch (BusinessException e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit")));
	}

	@Test
	public void cadastrarNovoDataFutura() throws Exception {
		LOGGER.debug("Testando salvar Trabalhador com data nascimento futura");
		String mensagemErro = "";
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date dataFutura = calendar.getTime();

			Trabalhador trabalhador = new Trabalhador();
			trabalhador.setId(1L);
			trabalhador.setDataNascimento(dataFutura);

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			trabalhadorService.salvar(trabalhador,this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro,
				getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_label_data_nascimento")));
	}

//	@Test
	public void cadastrarNovoCnpjDependente() throws Exception {
		LOGGER.debug("Testando salvar novo dependente");
		String mensagemErro = "";
		try {
			Trabalhador trabalhador = new Trabalhador();
			trabalhador.setId(1L);
			trabalhador.setCpf("59941882630");

			Dependente dependente = new Dependente();
			dependente.setId(1L);
			dependente.setNome("Teste");
			dependente.setCpf("59941882630");

			TrabalhadorDependente trabalhadorDependente = new TrabalhadorDependente();
			trabalhadorDependente.setId(1L);
			trabalhadorDependente.setDependente(dependente);

			Mockito.doReturn(trabalhadorDependente).when(trabalhadorDependenteService)
					.pesquisarDependentePorCPF(trabalhador.getCpf(),this.auditoria);

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			trabalhadorService.salvar(trabalhador,this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "Esse CPF é dependente do trabalhador 092.385.880-00190 - Teste. "
				+ "Para ser cadastrado como trabalhador, deverá primeiro ser encerrada a dependência");
	}
	
	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar Trabalhador");
		String mensagemErro = "";
		try {
			
			Trabalhador trabalhador = new Trabalhador();
			trabalhador.setId(1L);
			
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			trabalhadorService.salvar(trabalhador,this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro,"");
				
	}
}
