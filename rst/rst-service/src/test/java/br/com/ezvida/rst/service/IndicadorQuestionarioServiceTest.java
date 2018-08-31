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
import br.com.ezvida.rst.dao.IndicadorQuestionarioDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.IndicadorQuestionario;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class IndicadorQuestionarioServiceTest extends BaseService {

	private static final long serialVersionUID = 6700383924112478161L;

	private static final Logger LOGGER = LoggerFactory.getLogger(IndicadorQuestionarioServiceTest.class);

	@InjectMocks
	@Spy
	private IndicadorQuestionarioService indicadorQuestionarioService;

	@Mock
	private IndicadorQuestionarioDAO indicadorQuestionarioDAO;

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
			IndicadorQuestionario indicador = new IndicadorQuestionario();
			indicador.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			indicadorQuestionarioService.salvar(indicador, this.auditoria);
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
			indicadorQuestionarioService.salvar(null, this.auditoria);
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
			IndicadorQuestionario indicador = new IndicadorQuestionario();
			indicador.setId(1L);
			indicador.setDescricao("");
			IndicadorQuestionario indicadorQuestionarioCadastrado = new IndicadorQuestionario();
			indicadorQuestionarioCadastrado.setId(2L);
			indicadorQuestionarioCadastrado.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			Mockito.doReturn(indicadorQuestionarioCadastrado).when(indicadorQuestionarioService).buscarPorDescricao(indicador.getDescricao());
			indicadorQuestionarioService.salvar(indicador, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado",
				getMensagem("app_rst_label_indicador_questionario"), getMensagem("app_rst_label_descricao")));
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Resposta");
		String mensagemErro = "";
		try {
			IndicadorQuestionario indicador = new IndicadorQuestionario();
			indicador.setId(1L);
			Mockito.doReturn(indicador).when(indicadorQuestionarioService).pesquisarPorId(indicador.getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			indicadorQuestionarioService.desativar(indicador, this.auditoria);
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
			IndicadorQuestionario indicador = new IndicadorQuestionario();
			indicador.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			Mockito.doReturn(indicador).when(indicadorQuestionarioService).pesquisarPorId(indicador.getId());
			Mockito.doReturn(true).when(indicadorQuestionarioDAO).emUso(indicador.getId());
			indicadorQuestionarioService.desativar(indicador, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_erro_indicadorQuestionario_associada"));
	}

}