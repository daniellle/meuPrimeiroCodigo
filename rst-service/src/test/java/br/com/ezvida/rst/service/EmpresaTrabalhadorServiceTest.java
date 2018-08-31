package br.com.ezvida.rst.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import fw.core.service.BaseService;

public class EmpresaTrabalhadorServiceTest extends BaseService{

	private static final long serialVersionUID = -3226399466126973248L;
	
	@InjectMocks
	@Spy
	private EmpresaTrabalhadorService service;
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testValidacoesDataAdmissao() {
		EmpresaTrabalhador empresaTrabalhador = new EmpresaTrabalhador();
		Date dataAdmissao = new Date();
		dataAdmissao.setYear(dataAdmissao.getYear() + 1);
		empresaTrabalhador.setDataAdmissao(dataAdmissao);
		String msg = "";
		try {
			service.salvar(empresaTrabalhador, getClienteAuditoria());
		} catch (Exception e) {
			msg = e.getMessage();
		}
		
		Assert.assertEquals(msg, getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_admissao")));
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testValidacoesDataDemissao() {
		EmpresaTrabalhador empresaTrabalhador = new EmpresaTrabalhador();
		Date dataDemissao = new Date();
		dataDemissao.setYear(dataDemissao.getYear() + 1);
		empresaTrabalhador.setDataAdmissao(new Date());
		empresaTrabalhador.setDataDemissao(dataDemissao);
		String msg = "";
		try {
			service.salvar(empresaTrabalhador, getClienteAuditoria());
		} catch (Exception e) {
			msg = e.getMessage();
		}
		
		Assert.assertEquals(msg, getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_data_demissao")));
	}
	
	@SuppressWarnings("deprecation")
	public void testValidacoesDatas() {
		EmpresaTrabalhador empresaTrabalhador = new EmpresaTrabalhador();
		Date data = new Date();
		data.setYear(data.getYear() + 1);
		empresaTrabalhador.setDataAdmissao(data);
		empresaTrabalhador.setDataDemissao(new Date());
		String msg = "";
		try {
			service.salvar(empresaTrabalhador, getClienteAuditoria());
		} catch (Exception e) {
			msg = e.getMessage();
		}
		
		Assert.assertEquals(msg, getMensagem("app_rst_data_demissao_menor_que_data_admissao",
				getMensagem("app_rst_data_demissao"), getMensagem("app_rst_data_admissao")));
	}
	
	private ClienteAuditoria getClienteAuditoria() {
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setNavegador("navegador_teste_novo");
		auditoria.setUsuario("usuario_profissional_teste");
		auditoria.setFuncionalidade(Funcionalidade.EMPRESA);

		return auditoria;
	}

}
