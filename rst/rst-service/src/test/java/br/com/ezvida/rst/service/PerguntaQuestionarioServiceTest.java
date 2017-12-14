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
import br.com.ezvida.rst.dao.GrupoPerguntaQuestionarioDAO;
import br.com.ezvida.rst.dao.PerguntaQuestionarioDAO;
import br.com.ezvida.rst.dao.QuestionarioDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.StatusQuestionario;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.PerguntaQuestionario;
import br.com.ezvida.rst.model.Questionario;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class PerguntaQuestionarioServiceTest extends BaseService {

	private static final long serialVersionUID = -2813125156828368690L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PerguntaQuestionarioServiceTest.class);

	@InjectMocks
	@Spy
	private PerguntaQuestionarioService perguntaQuestionarioService;

	@Mock
	private PerguntaQuestionarioDAO perguntaQuestionarioDAO;

	@Mock
	private QuestionarioDAO questionarioDAO;

	@Mock
	private GrupoPerguntaQuestionarioDAO grupoPerguntaQuestionarioDAO;

	@Mock
	private RespostaQuestionarioService respostaQuestionarioService;

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
		LOGGER.debug("Testando salvar Pergunta Questionario");
		String mensagemErro = "";
		try {
			PerguntaQuestionario perguntaQuestionario = new PerguntaQuestionario();
			perguntaQuestionario.setQuestionario(new Questionario());
			perguntaQuestionario.getQuestionario().setId(1L);
			perguntaQuestionario.getQuestionario().setStatus(StatusQuestionario.EDICAO);
			Mockito.doReturn(perguntaQuestionario.getQuestionario()).when(questionarioDAO)
					.pesquisarPorId(perguntaQuestionario.getQuestionario().getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			perguntaQuestionarioService.associarPerguntaQuestionario(perguntaQuestionario, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Pergunta Questionario");
		String mensagemErro = "";
		try {
			PerguntaQuestionario perguntaQuestionario = new PerguntaQuestionario();
			perguntaQuestionario.setQuestionario(new Questionario());
			perguntaQuestionario.getQuestionario().setId(1L);
			perguntaQuestionario.getQuestionario().setStatus(StatusQuestionario.EDICAO);
			Mockito.doReturn(perguntaQuestionario.getQuestionario()).when(questionarioDAO)
					.pesquisarPorId(perguntaQuestionario.getQuestionario().getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			perguntaQuestionarioService.desativarPerguntaQuestionario(perguntaQuestionario, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

}
