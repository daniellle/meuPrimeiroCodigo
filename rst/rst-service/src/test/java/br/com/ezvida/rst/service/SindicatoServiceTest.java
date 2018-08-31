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

import com.google.gson.Gson;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.SindicatoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.SindicatoFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Sindicato;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class SindicatoServiceTest extends BaseService {

	private static final long serialVersionUID = 8921554780216183257L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SindicatoServiceTest.class);

	@InjectMocks
	@Spy
	private SindicatoService sindicatoService;

	@Mock
	private SindicatoDAO sindicatoDAO;

	@Mock
	private EmailSindicatoService emailSindicatoService;

	@Mock
	private EnderecoSindicatoService enderecoSindicatoService;

	@Mock
	private TelefoneSindicatoService telefoneSindicatoService;
	
	ClienteAuditoria auditoria = new ClienteAuditoria();
	Gson gson = new Gson();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_sindicato");
		auditoria.setUsuario("usuario_sindicato");	
		auditoria.setFuncionalidade(Funcionalidade.SINDICATO);
		MDC.put("FUNCIONALIDADE","sindicato_teste");
	}

	@Test
	public void pesquisarTodosAtivos() throws Exception {
		LOGGER.debug("Testando pesquisar todos Sindicatos");
		String mensagemErro = "";
		try {
			sindicatoService.listarTodos();
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void pesquisarPorId() throws Exception {
		LOGGER.debug("Testando pesquisar Sindicato por id");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			sindicatoService.pesquisarPorId(new Long(1),auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void pesquisarPorIdNulo() throws Exception {
		LOGGER.debug("Testando pesquisar Sindicato por id nulo");
		String mensagemErro = "";
		try {
			sindicatoService.pesquisarPorId(null,auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

	@Test
	public void pesquisarPaginado() throws Exception {
		LOGGER.debug("Testando pesquisar Sindicato paginado");
		SindicatoFilter sindicatoFilter = new SindicatoFilter();
		sindicatoFilter.setCnpj("12345678901234");
		sindicatoFilter.setPagina(1);
		sindicatoFilter.setQuantidadeRegistro(10);
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			sindicatoService.pesquisarPaginado(sindicatoFilter, auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar Sindicato");
		String mensagemErro = "";
		try {
			Sindicato sindicato = new Sindicato();
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			sindicatoService.salvar(sindicato,auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void alterar() throws Exception {
		LOGGER.debug("Testando alterar Sindicato");
		String mensagemErro = "";
		try {
			Sindicato sindicato = new Sindicato();
			sindicato.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			sindicatoService.salvar(sindicato,auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void cadastrarNovoCnpjInvalido() throws Exception {
		LOGGER.debug("Testando salvar Sindicato com cnpj inválido");
		String mensagemErro = "";
		try {
			Sindicato sindicato = new Sindicato();
			sindicato.setCnpj("11111111111111");
			sindicato.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			sindicatoService.salvar(sindicato,auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
	}

	@Test
	public void cadastrarNovoCnpjDuplicado() throws Exception {
		LOGGER.debug("Testando salvar Sindicato com cnpj duplicado");
		String mensagemErro = "";
		try {
			Sindicato sindicato = new Sindicato();
			sindicato.setId(1L);
			sindicato.setCnpj("09238588000190");

			Sindicato sindicatoCadastrado = new Sindicato();
			sindicatoCadastrado.setId(2L);
			sindicatoCadastrado.setCnpj("09238588000190");
			Mockito.doReturn(sindicatoCadastrado).when(sindicatoDAO).pesquisarPorCNPJ(sindicatoCadastrado.getCnpj());
			
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			sindicatoService.salvar(sindicato,auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado",
				getMensagem("app_rst_label_sindicato"), getMensagem("app_rst_label_cnpj")));
	}

}
