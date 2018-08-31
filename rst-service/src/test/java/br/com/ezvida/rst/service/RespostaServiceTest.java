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
import br.com.ezvida.rst.dao.RespostaDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Resposta;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class RespostaServiceTest extends BaseService {

	private static final long serialVersionUID = -563801010393347980L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RespostaServiceTest.class);

	@InjectMocks
	@Spy
	private RespostaService respostaService;

	@Mock
	private RespostaDAO respostaDAO;

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
		LOGGER.debug("Testando salvar Resposta");
		String mensagemErro = "";
		try {
			Resposta resposta = new Resposta();
			resposta.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			respostaService.salvar(resposta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void salvarNulo() throws Exception {
		LOGGER.debug("Testando salvar Resposta");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			respostaService.salvar(null, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_parametro_nulo"));
	}

	@Test
	public void salvarJaCadastrado() throws Exception {
		LOGGER.debug("Testando salvar Resposta já cadastrada");
		String mensagemErro = "";
		try {
			Resposta resposta = new Resposta();
			resposta.setId(1L);
			resposta.setDescricao("");
			Resposta respostaCadastrada = new Resposta();
			respostaCadastrada.setId(2L);
			respostaCadastrada.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			Mockito.doReturn(respostaCadastrada).when(respostaService).buscarPorDescricao(resposta.getDescricao());
			respostaService.salvar(resposta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_label_resposta_ja_cadastrada"));
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Resposta");
		String mensagemErro = "";
		try {
			Resposta resposta = new Resposta();
			resposta.setId(1L);
			Mockito.doReturn(resposta).when(respostaService).pesquisarPorId(resposta.getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			respostaService.desativarResposta(resposta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void desativarPerguntaEmUso() throws Exception {
		LOGGER.debug("Testando desativar Resposta em uso");
		String mensagemErro = "";
		try {
			Resposta resposta = new Resposta();
			resposta.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			Mockito.doReturn(resposta).when(respostaService).pesquisarPorId(resposta.getId());
			Mockito.doReturn(true).when(respostaDAO).respostaEmUso(resposta.getId());
			respostaService.desativarResposta(resposta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_erro_resposta_associada"));
	}

}
