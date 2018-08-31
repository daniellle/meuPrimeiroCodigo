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
import br.com.ezvida.rst.dao.PerguntaDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Pergunta;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class PerguntaServiceTest extends BaseService {

	private static final long serialVersionUID = -6815241368973380583L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PerguntaServiceTest.class);

	@InjectMocks
	@Spy
	private PerguntaService perguntaService;

	@Mock
	private PerguntaDAO perguntaDAO;

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
		LOGGER.debug("Testando salvar Pergunta");
		String mensagemErro = "";
		try {
			Pergunta pergunta = new Pergunta();
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			perguntaService.salvar(pergunta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void salvarJaCadastrado() throws Exception {
		LOGGER.debug("Testando salvar Pergunta já cadastrada");
		String mensagemErro = "";
		try {
			Pergunta pergunta = new Pergunta();
			pergunta.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			Mockito.doReturn(new Pergunta()).when(perguntaDAO).pesquisarPorDescricao(pergunta);
			perguntaService.salvar(pergunta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_label_pergunta_ja_cadastrada"));
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Pergunta");
		String mensagemErro = "";
		try {
			Pergunta pergunta = new Pergunta();
			pergunta.setId(1L);
			Mockito.doReturn(pergunta).when(perguntaService).pesquisarPorId(pergunta.getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			perguntaService.desativarPergunta(pergunta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	 @Test
	public void desativarPerguntaEmUso() throws Exception {
		LOGGER.debug("Testando desativar Pergunta em uso");
		String mensagemErro = "";
		try {
			Pergunta pergunta = new Pergunta();
			pergunta.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			Mockito.doReturn(pergunta).when(perguntaService).pesquisarPorId(pergunta.getId());
			Mockito.doReturn(true).when(perguntaDAO).perguntaEmUso(pergunta.getId());
			perguntaService.desativarPergunta(pergunta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_erro_pergunta_associada"));
	}

}
