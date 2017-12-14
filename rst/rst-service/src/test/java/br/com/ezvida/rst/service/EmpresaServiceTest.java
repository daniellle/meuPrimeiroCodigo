package br.com.ezvida.rst.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.EmpresaFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Empresa;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class EmpresaServiceTest extends BaseService {

	private static final long serialVersionUID = -5703214297622387111L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaServiceTest.class);

	@Mock
	EmpresaDAO empresaDAO;

	@InjectMocks
	EmpresaService empresaService;

	@Mock
	private EmailEmpresaService emailEmpresaService;

	@Mock
	private EnderecoEmpresaService enderecoEmpresaService;

	@Mock
	private TelefoneEmpresaService telefoneEmpresaService;

	@Mock
	private EmpresaUnidadeAtendimentoTrabalhadorService empresaUatService;

	@Mock
	private EmpresaCnaeService empresaCnaeService;
	
	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste_novo");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.EMPRESA);
		
		MDC.put("USER",auditoria.getUsuario());
		MDC.put("FUNCIONALIDADE","profissional_teste");		
		MDC.put("NAVEGADOR", auditoria.getNavegador());
	}

	@Test
	public void pesquisarComfiltro() throws Exception {
		LOGGER.debug("Testando pesquisar empresa com filtro");
		EmpresaFilter empresaFilter = new EmpresaFilter();
		empresaFilter.setCnpj("00000000000");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			empresaService.pesquisarPaginado(empresaFilter, auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void consularComId() throws Exception {
		LOGGER.debug("Testando pesquisar empresa por id");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			empresaService.pesquisarPorId(new Long(1), auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));

	}

	@Test
	public void consularComIdNull() throws Exception {
		LOGGER.debug("Testando pesquisar empresa com id nulo");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			empresaService.pesquisarPorId(null, auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));

	}

	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar Empresa");
		String mensagemErro = "";
		try {
			Empresa empresa = new Empresa();
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			empresaService.salvar(empresa, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, null);
	}

	@Test
	public void alterar() throws Exception {
		LOGGER.debug("Testando alterar Empresa");
		String mensagemErro = "";
		try {
			Empresa empresa = new Empresa();
			empresa.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			empresaService.salvar(empresa, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, null);
	}

	@Test
	public void cadastrarNovoCnpjInvalido() throws Exception {
		LOGGER.debug("Testando salvar Empresa com cnpj inválido");
		String mensagemErro = "";
		try {
			Empresa empresa = new Empresa();
			empresa.setCnpj("11111111111111");
			empresa.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			empresaService.salvar(empresa, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cnpj")));
	}

	@Test
	public void cadastrarNovoCnpjDuplicado() throws Exception {
		LOGGER.debug("Testando salvar Empresa com cnpj duplicado");
		String mensagemErro = "";
		try {
			Empresa empresa = new Empresa();
			empresa.setId(1L);
			empresa.setCnpj("09238588000190");

			Empresa empresaCadastrado = new Empresa();
			empresaCadastrado.setId(2L);
			empresaCadastrado.setCnpj("09238588000190");
			Mockito.doReturn(empresaCadastrado).when(empresaDAO).pesquisarPorCNPJ(empresa.getCnpj());

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			empresaService.salvar(empresa, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado",
				getMensagem("app_rst_label_empresa"), getMensagem("app_rst_label_cnpj")));
	}

	@Test
	public void cadastrarNovoNitContatoInvalido() throws Exception {
		LOGGER.debug("Testando salvar Empresa com nit contato inválido");
		String mensagemErro = "";
		try {
			Empresa empresa = new Empresa();
			empresa.setNumeroNitContato("88540602031");
			empresa.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			empresaService.salvar(empresa, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro,
				getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_contato")));
	}

	@Test
	public void cadastrarNovoNitResponsavelInvalido() throws Exception {
		LOGGER.debug("Testando salvar Empresa com nit responsável inválido");
		String mensagemErro = "";
		try {
			Empresa empresa = new Empresa();
			empresa.setNumeroNitResponsavel("88540602031");
			empresa.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			empresaService.salvar(empresa, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro,
				getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit_responsavel")));
	}

}
