package br.com.ezvida.rst.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.RedeCredenciadaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.RedeCredenciadaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.RedeCredenciada;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class RedeCredenciadaServiceTest extends BaseService {

	private static final long serialVersionUID = 8837848478016555179L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedeCredenciadaServiceTest.class);

	@InjectMocks
	@Spy
	private RedeCredenciadaService redeCredenciadaService;

	@Mock
	private TelefoneRedeCredenciadaService telefoneRedeCredenciadaService;

	@Mock
	private EnderecoRedeCredenciadaService enderecoRedeCredenciadaService;

	@Mock
	private EmailRedeCredenciadaService emailRedeCredenciadaService;

	@Mock
	private RedeCredenciadaDAO redeCredenciadaDAO;
	
	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		auditoria.setNavegador("navegador_teste_novo");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.REDE_CREDENCIADA);
		
		MDC.put("USER",auditoria.getUsuario());
		MDC.put("FUNCIONALIDADE","rede_credenciada_teste");		
		MDC.put("NAVEGADOR", auditoria.getNavegador());
	}

	@Test
	public void pesquisarPorId() throws Exception {
		LOGGER.debug("Testando pesquisar Rede Credenciada por id");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			redeCredenciadaService.pesquisarPorId(new Long(1), this.auditoria, null);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void pesquisarPorIdNulo() throws Exception {
		LOGGER.debug("Testando pesquisar Rede Credenciada por id nulo");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			redeCredenciadaService.pesquisarPorId(null, this.auditoria, null);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

	@Test
	public void pesquisarTodos() throws Exception {
		LOGGER.debug("Testando pesquisar todas Redes Credenciadas");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			redeCredenciadaService.pesquisarTodos(this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void pesquisarPaginado() throws Exception {
		LOGGER.debug("Testando pesquisar Redes Credenciadas paginado");
		RedeCredenciadaFilter redeCredenciadaFilter = new RedeCredenciadaFilter();
		redeCredenciadaFilter.setCnpj("12345678901234");
		redeCredenciadaFilter.setPagina(1);
		redeCredenciadaFilter.setQuantidadeRegistro(10);
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			redeCredenciadaService.pesquisarPaginado(redeCredenciadaFilter, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}
	
	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar Rede Credenciada");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setNumeroCnpj("69327226000106");
			redeCredenciada.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}
	
	@Test
	public void cadastrarNovoCnpjInvalido() throws Exception {
		LOGGER.debug("Testando salvar Rede Credenciada com cnpj inválido");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setNumeroCnpj("11111111111111");
			redeCredenciada.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
	}

	@Test
	public void cadastrarNovoCnpjDuplicado() throws Exception {
		LOGGER.debug("Testando salvar Rede Credenciada com cnpj duplicado");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setId(1L);
			redeCredenciada.setNumeroCnpj("09238588000190");

			RedeCredenciada redeCredenciadaCadastrada = new RedeCredenciada();
			redeCredenciadaCadastrada.setId(2L);
			redeCredenciadaCadastrada.setNumeroCnpj("09238588000190");
			Mockito.doReturn(redeCredenciadaCadastrada).when(redeCredenciadaDAO)
					.pesquisarPorCNPJ(redeCredenciada.getNumeroCnpj());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_rede_Credenciada"),
				getMensagem("app_rst_label_cnpj")));
	}
	
	@Test
	public void cadastrarNovoNitInvalido() throws Exception {
		LOGGER.debug("Testando salvar Rede Credenciada com nit responsável inválido");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setNumeroCnpj("09238588000190");
			redeCredenciada.setNumeroNitResponsavel("1");
			redeCredenciada.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_responsavel")));
	}
	
	@Test
	public void cadastrarNovoNitValido() throws Exception {
		LOGGER.debug("Testando salvar Rede Credenciada com nit responsável válido");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setNumeroCnpj("09238588000190");
			redeCredenciada.setNumeroNitResponsavel("45229147003");
			redeCredenciada.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}
	
	@Test
	public void cadastrarNovoEmailInvalido() throws Exception {
		LOGGER.debug("Testando salvar Rede Credenciada com email responsável inválido");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setNumeroCnpj("09238588000190");
			redeCredenciada.setNumeroNitResponsavel("45229147003");
			redeCredenciada.setEmailResponsavel("teste");
			redeCredenciada.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_email")));
	}
	
	@Test
	public void cadastrarNovoEmailValido() throws Exception {
		LOGGER.debug("Testando salvar Rede Credenciada com email responsável válido");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setNumeroCnpj("09238588000190");
			redeCredenciada.setNumeroNitResponsavel("45229147003");
			redeCredenciada.setEmailResponsavel("teste@teste.com");
			redeCredenciada.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void alterar() throws Exception {
		LOGGER.debug("Testando alterar Rede Credenciada");
		String mensagemErro = "";
		try {
			RedeCredenciada redeCredenciada = new RedeCredenciada();
			redeCredenciada.setId(1212L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			redeCredenciadaService.salvar(redeCredenciada, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

}
