package br.com.ezvida.rst.service;

import java.util.ArrayList;
import java.util.List;

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
import br.com.ezvida.rst.dao.PerguntaQuestionarioDAO;
import br.com.ezvida.rst.dao.QuestionarioDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Questionario;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class QuestionarioServiceTest extends BaseService {

	private static final long serialVersionUID = -2514741736844833451L;

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionarioServiceTest.class);

	@InjectMocks
	@Spy
	private QuestionarioService questionarioService;

	@Mock
	private QuestionarioDAO questionarioDAO;

	@Mock
	private PerguntaQuestionarioDAO perguntaQuestionarioDAO;

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
		LOGGER.debug("Testando salvar Questionario");
		String mensagemErro = "";
		try {
			Questionario questionario = new Questionario();
			List<Questionario> q = new ArrayList<Questionario>();
			q.add(questionario);
			Mockito.doReturn(q).when(perguntaQuestionarioDAO)
					.buscarPerguntasQuestionarioPorIdQuestionario(questionario.getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			questionarioService.salvar(questionario, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void publicarPrimeiroQuestionario() throws Exception {
		LOGGER.debug("Testando publicar primeiro Questionario com pergunta vazia");
		String mensagemErro = "";
		try {
			Questionario questionario = new Questionario();
			List<Questionario> q = new ArrayList<Questionario>();
			q.add(questionario);
			Mockito.doReturn(q).when(perguntaQuestionarioDAO)
					.buscarPerguntasQuestionarioPorIdQuestionario(questionario.getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			questionarioService.publicar(questionario, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void publicarQuestionario() throws Exception {
		LOGGER.debug("Testando publicar Questionario com pergunta vazia");
		String mensagemErro = "";
		try {
			Questionario questionario = new Questionario();
			questionario.setVersao(0);
			List<Questionario> q = new ArrayList<Questionario>();
			q.add(questionario);
			Mockito.doReturn(q).when(perguntaQuestionarioDAO)
					.buscarPerguntasQuestionarioPorIdQuestionario(questionario.getId());
			Mockito.doReturn(questionario).when(questionarioDAO)
					.pesquisarPorPublicado(questionario.getTipoQuestionario());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			questionarioService.publicar(questionario, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void publicarPerguntaVazia() throws Exception {
		LOGGER.debug("Testando publicar Questionario com pergunta vazia");
		String mensagemErro = "";
		try {
			Questionario questionario = new Questionario();
			List<Questionario> q = new ArrayList<Questionario>();
			q.add(questionario);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			questionarioService.publicar(questionario, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_questionario_sem_pergunta_resposta"));
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Questionario");
		String mensagemErro = "";
		try {
			Questionario questionario = new Questionario();
			questionario.setId(1L);
			Mockito.doReturn(questionario).when(questionarioService).pesquisarPorId(questionario.getId(), auditoria);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			questionarioService.desativar(questionario, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

}
