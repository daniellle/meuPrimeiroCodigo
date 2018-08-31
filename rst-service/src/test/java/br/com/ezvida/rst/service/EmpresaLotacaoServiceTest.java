package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaCboDAO;
import br.com.ezvida.rst.dao.EmpresaFuncaoDAO;
import br.com.ezvida.rst.dao.EmpresaJornadaDAO;
import br.com.ezvida.rst.dao.EmpresaLotacaoDAO;
import br.com.ezvida.rst.dao.EmpresaSetorDAO;
import br.com.ezvida.rst.model.EmpresaCbo;
import br.com.ezvida.rst.model.EmpresaFuncao;
import br.com.ezvida.rst.model.EmpresaJornada;
import br.com.ezvida.rst.model.EmpresaLotacao;
import br.com.ezvida.rst.model.EmpresaSetor;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class EmpresaLotacaoServiceTest extends BaseService {

	private static final long serialVersionUID = 2579182053436435739L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaLotacaoServiceTest.class);

	@InjectMocks
	@Spy
	private EmpresaLotacaoService empresaLotacaoService;

	@InjectMocks
	@Spy
	private EmpresaFuncaoService empresaFuncaoService;

	@InjectMocks
	@Spy
	private EmpresaJornadaService empresaJornadaService;

	@InjectMocks
	@Spy
	private EmpresaCboService empresaCboService;

	@InjectMocks
	@Spy
	private EmpresaSetorService empresaSetorService;

	@Mock
	private EmpresaLotacaoDAO empresaLotacaoDAO;

	@Mock
	private EmpresaFuncaoDAO empresaFuncaoDAO;

	@Mock
	private EmpresaSetorDAO empresaSetorDAO;

	@Mock
	private EmpresaJornadaDAO empresaJornadaDAO;

	@Mock
	private EmpresaCboDAO empresaCboDAO;
	
	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		auditoria.setNavegador("navegador_teste_novo");
		auditoria.setUsuario("usuario_teste");
		
		MDC.put("USER",auditoria.getUsuario());
		MDC.put("FUNCIONALIDADE","empresa_lotacao_teste");		
		MDC.put("NAVEGADOR", auditoria.getNavegador());
	}

	@Test
	public void cadastrarNovaEmpresaLotacaoDuplicada() throws Exception {
		LOGGER.debug("Testando salvar Empresa Lotação duplicada");
		String mensagemErro = "";
		try {

			EmpresaLotacao empresaLotacao = getEmpresaLotacao(false);

			EmpresaLotacao empresaLotacaoCadastrada = getEmpresaLotacao(false);
			empresaLotacaoCadastrada.setId(1L);

			Mockito.doReturn(empresaLotacaoCadastrada).when(empresaLotacaoDAO)
					.pesquisarEmpresaLotacaoVinculada(empresaLotacao);

			Set<EmpresaLotacao> listaEmpresaLotacao = new HashSet<EmpresaLotacao>();
			listaEmpresaLotacao.add(empresaLotacao);
			empresaLotacaoService.salvar(listaEmpresaLotacao,auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_lotacao_cadastrada_para_esta_empresa"));
	}

	@Test
	public void validarSeRelacionamentosDesativados() {
		String mensagemErro = StringUtils.EMPTY;

		Mockito.doReturn(getEmpresaCbo(true)).when(empresaCboDAO).pesquisarPorId(1L);
		Mockito.doReturn(getEmpresaFuncao(true)).when(empresaFuncaoDAO).pesquisarPorId(1L);
		Mockito.doReturn(getEmpresaJornada(true)).when(empresaJornadaDAO).pesquisarPorId(1L);
		Mockito.doReturn(getEmpresaSetor(true)).when(empresaSetorDAO).pesquisarPorId(1L);

		EmpresaLotacao empLotacao = getEmpresaLotacao(true);

		try {
			empresaLotacaoService.salvar(empLotacao);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_cbo_nao_ativo"));

		try {
			empLotacao.setEmpresaCbo(null);
			empresaLotacaoService.salvar(empLotacao);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_funcao_nao_ativo"));

		try {
			empLotacao.setEmpresaFuncao(null);
			empresaLotacaoService.salvar(empLotacao);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_jornada_nao_ativo"));

		try {
			empLotacao.setEmpresaJornada(null);
			empresaLotacaoService.salvar(empLotacao);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_setor_nao_ativo"));
		
	}

	private EmpresaCbo getEmpresaCbo(boolean desativado) {
		EmpresaCbo cbo = new EmpresaCbo();
		cbo.setId(1L);
		if (desativado) {
			cbo.setDataExclusao(new Date());
		}

		return cbo;
	}

	private EmpresaFuncao getEmpresaFuncao(boolean desativado) {
		EmpresaFuncao funcao = new EmpresaFuncao();
		funcao.setId(1L);
		if (desativado) {
			funcao.setDataExclusao(new Date());
		}

		return funcao;
	}

	private EmpresaSetor getEmpresaSetor(boolean desativado) {
		EmpresaSetor setor = new EmpresaSetor();
		setor.setId(1L);
		if (desativado) {
			setor.setDataExclusao(new Date());
		}
		return setor;
	}

	private EmpresaJornada getEmpresaJornada(boolean desativado) {
		EmpresaJornada jornada = new EmpresaJornada();
		jornada.setId(1L);
		if (desativado) {
			jornada.setDataExclusao(new Date());
		}
		
		return jornada;
	}

	private EmpresaLotacao getEmpresaLotacao(boolean desativado) {
		EmpresaLotacao empLotacao = new EmpresaLotacao();
		empLotacao.setEmpresaCbo(getEmpresaCbo(desativado));
		empLotacao.setEmpresaFuncao(getEmpresaFuncao(desativado));
		empLotacao.setEmpresaJornada(getEmpresaJornada(desativado));
		empLotacao.setEmpresaSetor(getEmpresaSetor(desativado));

		return empLotacao;
	}
}
