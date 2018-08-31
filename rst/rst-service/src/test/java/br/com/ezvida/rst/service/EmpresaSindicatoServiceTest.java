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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.EmpresaSindicatoDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.model.Empresa;
import br.com.ezvida.rst.model.EmpresaSindicato;
import br.com.ezvida.rst.model.Sindicato;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class EmpresaSindicatoServiceTest extends BaseService {
	private static final long serialVersionUID = 3914869073821437030L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaSindicatoServiceTest.class);

	@InjectMocks
	@Spy
	private EmpresaSindicatoService empresaSindicatoService;

	@Mock
	private EmpresaSindicatoDAO empresaSindicatoDAO;

	ClienteAuditoria auditoria = new ClienteAuditoria();
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste_novo");
		auditoria.setUsuario("usuario_profissional_teste");
		auditoria.setFuncionalidade(Funcionalidade.EMPRESA);
	}

	
	@Test
	public void cadastrarNovoDataAssociacaoFutura() throws Exception {
		LOGGER.debug("Testando salvar Empresa Sindicato com data de associação futura");
		String mensagemErro = "";
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date dataFutura = calendar.getTime();

			EmpresaSindicato empresaSindicato = new EmpresaSindicato();
			empresaSindicato.setId(1L);
			empresaSindicato.setDataAssociacao(dataFutura);
			;

			empresaSindicatoService.salvar(1L, empresaSindicato, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro,
				getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_associacao")));
	}

	@Test
	public void cadastrarNovoDataDesligamentoFutura() throws Exception {
		LOGGER.debug("Testando salvar Empresa Sindicato com data de desligamento futura");
		String mensagemErro = "";
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date dataFutura = calendar.getTime();

			EmpresaSindicato empresaSindicato = new EmpresaSindicato();
			empresaSindicato.setId(1L);
			empresaSindicato.setDataAssociacao(new Date());
			empresaSindicato.setDataDesligamento(dataFutura);
			;

			empresaSindicatoService.salvar(1L, empresaSindicato, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro,
				getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_desligamento")));
	}

	@Test
	public void cadastrarNovoJaAssociado() throws Exception {
		LOGGER.debug("Testando salvar Empresa Sindicato com sindicato já associado a esta empresa");
		String mensagemErro = "";
		try {
			Sindicato sindicato = new Sindicato();
			sindicato.setId(1L);

			Empresa emp = new Empresa();
			emp.setId(1L);

			EmpresaSindicato empresaSindicato = new EmpresaSindicato();
			empresaSindicato.setId(1L);
			empresaSindicato.setSindicato(sindicato);
			empresaSindicato.setDataAssociacao(new Date());
			empresaSindicato.setDataDesligamento(new Date());

			EmpresaSindicato empresaSindicatoCadastrado = new EmpresaSindicato();
			empresaSindicatoCadastrado.setId(2L);
			empresaSindicatoCadastrado.setSindicato(sindicato);
			Mockito.doReturn(empresaSindicatoCadastrado).when(empresaSindicatoDAO)
					.buscarPorSindicatoEEmpresa(empresaSindicato.getId(), emp.getId());

			empresaSindicatoService.salvar(emp.getId(), empresaSindicato, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_registro_ja_associado",
				getMensagem("app_rst_label_sindicato"), getMensagem("app_rst_label_empresa")));
	}

	public void cadastrarNovoNoPeriodo() throws Exception {
		LOGGER.debug("Testando salvar Empresa Sindicato com sindicato no período");
		String mensagemErro = "";
		try {
			Sindicato sindicato = new Sindicato();
			sindicato.setId(1L);

			Empresa emp = new Empresa();
			emp.setId(1L);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 2);
			Date dataFutura = calendar.getTime();

			EmpresaSindicato empresaSindicato = new EmpresaSindicato();
			empresaSindicato.setId(1L);
			empresaSindicato.setDataAssociacao(new Date());
			empresaSindicato.setDataDesligamento(dataFutura);

			EmpresaSindicato empresaSindicatoCadastrado = new EmpresaSindicato();
			empresaSindicatoCadastrado.setId(2L);
			empresaSindicato.setDataAssociacao(new Date());
			Mockito.doReturn(empresaSindicatoCadastrado).when(empresaSindicatoDAO)
					.buscarPorSindicatoEEmpresa(empresaSindicato.getId(), emp.getId());

			empresaSindicatoService.salvar(emp.getId(), empresaSindicato, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_sindicato_ja_cadastrado_no_periodo"));
	}

}
