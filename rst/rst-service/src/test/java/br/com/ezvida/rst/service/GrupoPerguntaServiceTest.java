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
import br.com.ezvida.rst.dao.GrupoPerguntaDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.GrupoPergunta;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class GrupoPerguntaServiceTest extends BaseService {

	private static final long serialVersionUID = 6377438538351344351L;

	private static final Logger LOGGER = LoggerFactory.getLogger(GrupoPerguntaServiceTest.class);

	@InjectMocks
	@Spy
	private GrupoPerguntaService grupoPerguntaService;

	@Mock
	private GrupoPerguntaDAO grupoPerguntaDAO;

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
			GrupoPergunta grupoPergunta = new GrupoPergunta();
			grupoPergunta.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			grupoPerguntaService.salvar(grupoPergunta, this.auditoria);
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
			grupoPerguntaService.salvar(null, this.auditoria);
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
			GrupoPergunta grupoPergunta = new GrupoPergunta();
			grupoPergunta.setId(1L);
			grupoPergunta.setDescricao("");
			GrupoPergunta grupoPerguntaCadastrado = new GrupoPergunta();
			grupoPerguntaCadastrado.setId(2L);
			grupoPerguntaCadastrado.setDescricao("");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			Mockito.doReturn(grupoPerguntaCadastrado).when(grupoPerguntaService)
					.buscarPorDescricao(grupoPergunta.getDescricao());
			grupoPerguntaService.salvar(grupoPergunta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado", getMensagem("app_rst_label_grupo"),
				getMensagem("app_rst_label_descricao")));
	}

	@Test
	public void desativar() throws Exception {
		LOGGER.debug("Testando desativar Classificacao Pontuacao");
		String mensagemErro = "";
		try {
			GrupoPergunta grupoPergunta = new GrupoPergunta();
			grupoPergunta.setId(1L);
			Mockito.doReturn(grupoPergunta).when(grupoPerguntaService).pesquisarPorId(grupoPergunta.getId());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			grupoPerguntaService.desativarGrupoPergunta(grupoPergunta, this.auditoria);
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
			GrupoPergunta grupoPergunta = new GrupoPergunta();
			grupoPergunta.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
			Mockito.doReturn(grupoPergunta).when(grupoPerguntaService).pesquisarPorId(grupoPergunta.getId());
			Mockito.doReturn(true).when(grupoPerguntaDAO).validarUso(grupoPergunta.getId());
			grupoPerguntaService.desativarGrupoPergunta(grupoPergunta, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_erro_grupo_pergunta_associada"));
	}

}
