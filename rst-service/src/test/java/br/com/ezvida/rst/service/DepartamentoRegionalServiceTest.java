package br.com.ezvida.rst.service;

import java.util.Date;
import java.util.Set;

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
import br.com.ezvida.rst.dao.DepartamentoRegionalDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.DepartamentoRegionalFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.DepartamentoRegional;
import br.com.ezvida.rst.model.DepartamentoRegionalProdutoServico;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class DepartamentoRegionalServiceTest extends BaseService {
	private static final long serialVersionUID = 341484888139569823L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoRegionalServiceTest.class);

	@InjectMocks
	@Spy
	private DepartamentoRegionalService departamentoRegionalService;

	@Mock
	private DepartamentoRegionalDAO departamentoRegionalDAO;

	@Mock
	private TelefoneDepartamentoRegionalService telefoneDepartamentoRegionalService;

	@Mock
	private EmailDepartamentoRegionalService emailDepartamentoRegionalService;

	@Mock
	private EnderecoDepartamentoRegionalService enderecoDepartamentoRegionalService;

	@Mock
	private Set<DepartamentoRegionalProdutoServico> mDepartamentoRegionalProdutoServico;

	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.DEPARTAMENTO_REGIONAL);
	}

	@Test
	public void pesquisarTodosAtivos() throws Exception {
		LOGGER.debug("Testando pesquisar todos Departamentos Regionais ativos");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			departamentoRegionalService.listarTodos(Situacao.ATIVO, new DadosFilter(), new DepartamentoRegionalFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void pesquisarTodosInativos() throws Exception {
		LOGGER.debug("Testando pesquisar todos Departamentos Regionais inativos");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			departamentoRegionalService.listarTodos(Situacao.INATIVO, new DadosFilter(), new DepartamentoRegionalFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void pesquisarPorId() throws Exception {
		LOGGER.debug("Testando pesquisar Departamento Regional por id");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			departamentoRegionalService.pesquisarPorId(new Long(1), this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void pesquisarPorIdNulo() throws Exception {
		LOGGER.debug("Testando pesquisar Departamento Regional por id nulo");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			departamentoRegionalService.pesquisarPorId(null, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

	@Test
	public void pesquisarPaginado() throws Exception {
		LOGGER.debug("Testando pesquisar Departamento Regional paginado");
		DepartamentoRegionalFilter departamentoRegionalFilter = new DepartamentoRegionalFilter();
		departamentoRegionalFilter.setCnpj("12345678901234");
		departamentoRegionalFilter.setPagina(1);
		departamentoRegionalFilter.setQuantidadeRegistro(10);
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			departamentoRegionalService.pesquisarPaginado(departamentoRegionalFilter, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar Departamento Regional");
		String mensagemErro = "";
		try {
			DepartamentoRegional departamentoRegional = new DepartamentoRegional();

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			departamentoRegionalService.salvar(departamentoRegional, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void alterar() throws Exception {
		LOGGER.debug("Testando alterar Departamento Regional");
		String mensagemErro = "";
		try {
			DepartamentoRegional departamentoRegional = new DepartamentoRegional();
			departamentoRegional.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			departamentoRegionalService.salvar(departamentoRegional, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void cadastrarNovoCnpjInvalido() throws Exception {
		LOGGER.debug("Testando salvar Departamento Regional com cnpj inválido");
		String mensagemErro = "";
		try {
			DepartamentoRegional departamentoRegional = new DepartamentoRegional();
			departamentoRegional.setCnpj("11111111111111");
			departamentoRegional.setDataCriacao(new Date());

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			departamentoRegionalService.salvar(departamentoRegional, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
	}

	@Test
	public void cadastrarNovoCnpjDuplicado() throws Exception {
		LOGGER.debug("Testando salvar Departamento Regional com cnpj duplicado");
		String mensagemErro = "";
		try {
			DepartamentoRegional departamentoRegional = new DepartamentoRegional();
			departamentoRegional.setId(1L);
			departamentoRegional.setCnpj("09238588000190");

			DepartamentoRegional departamentoRegionalCadastrado = new DepartamentoRegional();
			departamentoRegionalCadastrado.setId(2L);
			departamentoRegionalCadastrado.setCnpj("09238588000190");
			Mockito.doReturn(departamentoRegionalCadastrado).when(departamentoRegionalDAO)
					.pesquisarPorCNPJ(departamentoRegional.getCnpj());

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			departamentoRegionalService.salvar(departamentoRegional, this.auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado",
				getMensagem("app_rst_label_departamento_regional"), getMensagem("app_rst_label_cnpj")));
	}
}
