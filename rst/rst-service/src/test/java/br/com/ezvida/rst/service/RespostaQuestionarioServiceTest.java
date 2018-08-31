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
import br.com.ezvida.rst.dao.QuestionarioDAO;
import br.com.ezvida.rst.dao.RespostaQuestionarioDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.RespostaQuestionario;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class RespostaQuestionarioServiceTest extends BaseService {

	private static final long serialVersionUID = 8358515496749852881L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RespostaQuestionarioServiceTest.class);

	@InjectMocks
	@Spy
	private RespostaQuestionarioService respostaQuestionarioService;

	@Mock
	private RespostaQuestionarioDAO respostaQuestionarioDAO;

	@Mock
	private QuestionarioDAO questionarioDAO;

	@Mock
	private GrupoPerguntaQuestionarioDAO grupoPerguntaQuestionarioDAO;

	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.QUESTIONARIOS);
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Resposta Questionario");
		String mensagemErro = "";
		try {
			RespostaQuestionario resposta = new RespostaQuestionario();
			resposta.setId(1L);
			Mockito.doReturn(resposta).when(respostaQuestionarioService).pesquisarPorId(resposta.getId(),
					this.auditoria);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			respostaQuestionarioService.desativarRespostaQuestionario(resposta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

}
