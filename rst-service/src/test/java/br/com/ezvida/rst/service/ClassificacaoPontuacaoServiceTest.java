package br.com.ezvida.rst.service;

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
import br.com.ezvida.rst.dao.ClassificacaoPontuacaoDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.ClassificacaoPontuacao;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class ClassificacaoPontuacaoServiceTest extends BaseService {

	private static final long serialVersionUID = 8748943227024774059L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassificacaoPontuacaoServiceTest.class);

	@InjectMocks
	@Spy
	private ClassificacaoPontuacaoService classificacaoPontuacaoService;

	@Mock
	private ClassificacaoPontuacaoDAO classificacaoPontuacaoDAO;

	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.QUESTIONARIOS);
	}

	@Test
	public void salvar() throws Exception {
		LOGGER.debug("Testando salvar Classificacao Pontuacao");
		String mensagemErro = "";
		try {
			ClassificacaoPontuacao classificacaoPontuacao = new ClassificacaoPontuacao();
			classificacaoPontuacao.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			classificacaoPontuacaoService.salvar(classificacaoPontuacao, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void salvarNulo() throws Exception {
		LOGGER.debug("Testando salvar Classificacao Pontuacao");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			classificacaoPontuacaoService.salvar(null, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_parametro_nulo"));
	}

	@Test
	public void salvarJaCadastrado() throws Exception {
		LOGGER.debug("Testando salvar Classificacao Pontuacao já cadastrada");
		String mensagemErro = "";
		try {
			ClassificacaoPontuacao classificacaoPontuacao = new ClassificacaoPontuacao();
			classificacaoPontuacao.setId(1L);
			classificacaoPontuacao.setDescricao("");
			ClassificacaoPontuacao classificacaoPontuacaoCadastrado = new ClassificacaoPontuacao();
			classificacaoPontuacaoCadastrado.setId(2L);
			classificacaoPontuacaoCadastrado.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			Mockito.doReturn(classificacaoPontuacaoCadastrado).when(classificacaoPontuacaoService)
					.buscarPorDescricao(classificacaoPontuacao.getDescricao());
			classificacaoPontuacaoService.salvar(classificacaoPontuacao, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_erro_classificacao_ja_cadastrada"));
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Classificacao Pontuacao");
		String mensagemErro = "";
		try {
			ClassificacaoPontuacao classificacaoPontuacao = new ClassificacaoPontuacao();
			classificacaoPontuacao.setId(1L);
			Mockito.doReturn(classificacaoPontuacao).when(classificacaoPontuacaoService)
					.buscarPorId(classificacaoPontuacao.getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			classificacaoPontuacaoService.desativar(classificacaoPontuacao, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void desativarPerguntaEmUso() throws Exception {
		LOGGER.debug("Testando desativar Classificacao Pontuacao em uso");
		String mensagemErro = "";
		try {
			ClassificacaoPontuacao classificacaoPontuacao = new ClassificacaoPontuacao();
			classificacaoPontuacao.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			Mockito.doReturn(classificacaoPontuacao).when(classificacaoPontuacaoService)
					.buscarPorId(classificacaoPontuacao.getId());
			Mockito.doReturn(true).when(classificacaoPontuacaoDAO)
					.classificacaoPontuacaoEmUso(classificacaoPontuacao.getId());
			classificacaoPontuacaoService.desativar(classificacaoPontuacao, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_erro_classificacao_associada"));
	}

}
