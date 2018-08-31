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

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.ParceiroDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ParceiroFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Parceiro;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class ParceiroServiceTest extends BaseService {

	private static final long serialVersionUID = 55560958399104424L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParceiroServiceTest.class);

	@InjectMocks
	@Spy
	private ParceiroService parceiroService;

	@Mock
	private ParceiroEspecialidadeService parceiroEspecialidadeService;

	@Mock
	private EmailParceiroService emailParceiroService;

	@Mock
	private EnderecoParceiroService enderecoParceiroService;

	@Mock
	private TelefoneParceiroService telefoneParceiroService;

	@Mock
	private ParceiroDAO parceiroDAO;
	
	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.PARCEIRO_CREDENCIADO);
	}

	@Test
	public void pesquisarPorId() throws Exception {
		LOGGER.debug("Testando pesquisar Parceiro Credenciado por id");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			parceiroService.pesquisarPorId(new Long(1), this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void pesquisarPorIdNulo() throws Exception {
		LOGGER.debug("Testando pesquisar Parceiro Credenciado por id nulo");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			parceiroService.pesquisarPorId(null, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

	@Test
	public void pesquisarTodos() throws Exception {
		LOGGER.debug("Testando pesquisar todaos Parceiros Credenciados");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			parceiroService.listarTodos(this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void pesquisarPaginado() throws Exception {
		LOGGER.debug("Testando pesquisar Parceiro Credenciado paginado");
		ParceiroFilter parceiroFilter = new ParceiroFilter();
		parceiroFilter.setCpfCnpj("12345678901234");
		parceiroFilter.setPagina(1);
		parceiroFilter.setQuantidadeRegistro(10);
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			parceiroService.pesquisarPaginado(parceiroFilter, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

//	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar Parceiro Credenciado");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

//	@Test
	public void alterar() throws Exception {
		LOGGER.debug("Testando alterar Parceiro Credenciado");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			parceiro.setId(1L);
			parceiro.setNome("Teste");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

//	@Test
	public void cadastrarNovoNitInvalido() throws Exception {
		LOGGER.debug("Testando salvar Parceiro Credenciado com nit inválido");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			parceiro.setNumeroCnpjCpf("09238588000190");
			parceiro.setNumeroNit("1");
			parceiro.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit")));
	}

//	@Test
	public void cadastrarNovoNitResponsavelInvalido() throws Exception {
		LOGGER.debug("Testando salvar Parceiro Credenciado com nit responsável inválido");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			parceiro.setNumeroCnpjCpf("09238588000190");
			parceiro.setNumeroNitResponsavel("1");
			parceiro.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro,
				getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_responsavel")));
	}

//	@Test
	public void cadastrarNovoNitResponsavelValido() throws Exception {
		LOGGER.debug("Testando salvar Parceiro Credenciado com nit válido");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			parceiro.setNumeroCnpjCpf("09238588000190");
			parceiro.setNumeroNit("45229147003");
			parceiro.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

//	@Test
	public void cadastrarNovoNitValido() throws Exception {
		LOGGER.debug("Testando salvar Parceiro Credenciado com nit válido");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			parceiro.setNumeroCnpjCpf("09238588000190");
			parceiro.setNumeroNitResponsavel("45229147003");
			parceiro.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}
	
	@Test
	public void cadastrarNovoCnpjDuplicado() throws Exception {
		LOGGER.debug("Testando salvar Parceiro com cnpj duplicado");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			parceiro.setId(1L);
			parceiro.setNumeroCnpjCpf("09238588000190");

			Parceiro parceiroCadastrado = new Parceiro();
			parceiroCadastrado.setId(2L);
			parceiroCadastrado.setNumeroCnpjCpf("09238588000190");
			Mockito.doReturn(parceiroCadastrado).when(parceiroDAO)
					.pesquisarPorCNPJ(parceiroCadastrado.getNumeroCnpjCpf());

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_parceiro_credenciado"),
				getMensagem("app_rst_label_cnpj")));
	}
	
	@Test
	public void cadastrarNovoCnpjInvalido() throws Exception {
		LOGGER.debug("Testando salvar Parceiro com cnpj inválido");
		String mensagemErro = "";
		try {
			Parceiro parceiro = new Parceiro();
			parceiro.setNumeroCnpjCpf("11111111111111");
			parceiro.setDataCriacao(new Date());
			
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			parceiroService.salvar(parceiro, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", "CNPJ"));
	}

}
