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

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UnidadeAtendimentoTrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UnidAtendTrabalhadorFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class UnidAtendTrabalhadorServiceTest extends BaseService {

	private static final long serialVersionUID = 5453500840244658586L;

	@InjectMocks
	@Spy
	private UnidadeAtendimentoTrabalhadorService unidAtendTrabalhadorService;

	@Mock
	private EmailUnidadeAtendimentoTrabalhadorService emailUnidadeAtendimentoTrabalhadorService;

	@Mock
	private EnderecoUnidadeAtendimentoTrabalhadorService enderecoUnidadeAtendimentoTrabalhadorService;

	@Mock
	private TelefoneUnidadeAtendimentoTrabalhadorService telefoneUnidadeAtendimentoTrabalhadorService;

	@Mock
	private UnidadeAtendimentoTrabalhadorDAO unidadeAtendimentoTrabalhadorDAO;

	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.CAT);
	}

	@Test
	public void pesquisarSemFiltro() throws Exception {
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			unidAtendTrabalhadorService.pesquisarTodos(this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void pesquisarComfiltroCnpjIncompleto() throws Exception {
		UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter = new UnidAtendTrabalhadorFilter();
		unidAtendTrabalhadorFilter.setCnpj("1234");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			unidAtendTrabalhadorService.pesquisaPaginada(unidAtendTrabalhadorFilter, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_cnpj_nao_esta_completo"));
	}

	@Test
	public void pesquisarComfiltroCnpjCompleto() throws Exception {
		UnidAtendTrabalhadorFilter unidAtendTrabalhadorFilter = new UnidAtendTrabalhadorFilter();
		unidAtendTrabalhadorFilter.setCnpj("12345678901234");
		unidAtendTrabalhadorFilter.setPagina(1);
		unidAtendTrabalhadorFilter.setQuantidadeRegistro(10);
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			unidAtendTrabalhadorService.pesquisaPaginada(unidAtendTrabalhadorFilter, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void buscarPorIdNulo() throws Exception {
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			unidAtendTrabalhadorService.pesquisarPorId(null, this.auditoria, null);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

	@Test
	public void buscarPorId() throws Exception {
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			unidAtendTrabalhadorService.pesquisarPorId(new Long(1), this.auditoria, null);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void cadastrarNovo() throws Exception {
		String mensagemErro = "";
		try {
			UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador = new UnidadeAtendimentoTrabalhador();
			unidadeAtendimentoTrabalhador.setCnpj("09238588000190");

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			unidAtendTrabalhadorService.salvar(unidadeAtendimentoTrabalhador, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void cadastrarNovoCnpjInvalido() throws Exception {
		String mensagemErro = "";
		try {
			UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador = new UnidadeAtendimentoTrabalhador();
			unidadeAtendimentoTrabalhador.setCnpj("11111111111111");
			unidadeAtendimentoTrabalhador.setDataCriacao(new Date());

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			unidAtendTrabalhadorService.salvar(unidadeAtendimentoTrabalhador, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
	}

	@Test
	public void alterar() throws Exception {
		String mensagemErro = "";
		try {
			UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador = new UnidadeAtendimentoTrabalhador();
			unidadeAtendimentoTrabalhador.setId(1212L);

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			unidAtendTrabalhadorService.salvar(unidadeAtendimentoTrabalhador, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

}
