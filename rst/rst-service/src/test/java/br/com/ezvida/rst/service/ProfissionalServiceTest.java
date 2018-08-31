package br.com.ezvida.rst.service;

import java.util.Calendar;
import java.util.Date;

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

import com.google.gson.Gson;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.ProfissionalDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ProfissionalFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Estado;
import br.com.ezvida.rst.model.Profissional;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class ProfissionalServiceTest extends BaseService {

	private static final long serialVersionUID = 251324318874104094L;

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(ProfissionalServiceTest.class);

	@InjectMocks
	@Spy
	private ProfissionalService profissionalService;

	@Mock
	private ProfissionalDAO profissionalDAO;

	@Mock
	private EmailProfissionalService emailProfissionalService;

	@Mock
	private TelefoneProfissionalService telefoneProfissionalService;

	@Mock
	private EnderecoProfissionalService enderecoProfissionalService;

	@Mock
	private UnidadeAtendimentoTrabalhadorProfissionalService unidadeAtendimentoTrabalhadorProfissionalService;

	@Mock
	private ProfissionalEspecialidadeService profissionalEspecialidadeService;

	@Mock
	private Profissional profissional;

	ClienteAuditoria auditoria = new ClienteAuditoria();
	Gson gson = new Gson();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste_novo");
		auditoria.setUsuario("usuario_profissional_teste");
		auditoria.setFuncionalidade(Funcionalidade.PROFISSIONAL);
	}

	@Test
	public void pesquisarPaginado() throws Exception {
		ProfissionalFilter filtro = new ProfissionalFilter();
		filtro.setCpf("00000000000");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			profissionalService.pesquisarPaginado(filtro, auditoria, new DadosFilter());
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void consularComId() throws Exception {
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			profissionalService.pesquisarPorId(new Long(1), auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void consularComIdNull() throws Exception {
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			profissionalService.pesquisarPorId(null, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

	@Test
	public void cadastrarNovo() throws Exception {
		String mensagemErro = "";
		try {
			Estado estado = new Estado();
			estado.setId(1L);
			Profissional profissional = new Profissional();
			profissional.setId(1L);
			profissional.setRegistro("registro");
			profissional.setEstado(estado);

			Profissional profissionalCadastrado = new Profissional();
			profissionalCadastrado.setId(2L);
			profissionalCadastrado.setRegistro("registro");
			profissionalCadastrado.setEstado(estado);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			profissionalService.salvar(profissional, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void alterar() throws Exception {
		String mensagemErro = "";
		try {
			Estado estado = new Estado();
			estado.setId(1L);
			Profissional profissional = new Profissional();
			profissional.setId(1L);
			profissional.setRegistro("registro");
			profissional.setEstado(estado);

			Profissional profissionalCadastrado = new Profissional();
			profissionalCadastrado.setId(2L);
			profissionalCadastrado.setRegistro("registro");
			profissionalCadastrado.setEstado(estado);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			profissionalService.salvar(profissional, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
	}

	@Test
	public void cadastrarNovoCpfInvalido() throws Exception {
		String mensagemErro = "";
		try {
			Profissional profissional = new Profissional();
			profissional.setId(1L);
			profissional.setCpf("12345678951");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			profissionalService.salvar(profissional, auditoria);
			// LogAuditoria.Registrar(LOGGER, auditoria, "profissional", "(TESTE) Salvar
			// Profissional com cpf inválido: ", profissional);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cpf")));
	}

	@Test
	public void cadastrarNovoNitInvalido() throws Exception {
		String mensagemErro = "";
		try {
			Profissional profisisonal = new Profissional();
			profisisonal.setNit("1181735140");
			profisisonal.setDataCriacao(new Date());
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			profissionalService.salvar(profisisonal, auditoria);
			// LogAuditoria.Registrar(LOGGER, auditoria, "profissional", "(TESTE) salvar
			// Profissional com nit invalido: ", profissional);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_nit")));
	}

	@Test
	public void cadastrarNovoConselhoRegionalDuplicado() throws Exception {
		String mensagemErro = "";
		try {
			Estado estado = new Estado();
			estado.setId(1L);

			Profissional profissional = new Profissional();
			profissional.setId(1L);
			profissional.setRegistro("registro");
			profissional.setEstado(estado);

			Profissional profissionalCadastrado = new Profissional();
			profissionalCadastrado.setId(2L);
			profissionalCadastrado.setRegistro("registro");
			profissionalCadastrado.setEstado(estado);
			Mockito.doReturn(profissionalCadastrado).when(profissionalDAO).pesquisarPorConselhoRegional(profissional);

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			profissionalService.salvar(profissional, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_duplicado",
				getMensagem("app_rst_label_profissional"), getMensagem("app_rst_label_Conselho")));
	}

	@Test
	public void cadastrarNovoDataFutura() throws Exception {
		String mensagemErro = "";
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date dataFutura = calendar.getTime();

			Profissional profissional = new Profissional();
			profissional.setId(1L);
			profissional.setDataNascimento(dataFutura);

			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			profissionalService.salvar(profissional, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro,
				getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_label_data_nascimento")));
	}

}
