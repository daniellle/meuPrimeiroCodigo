package br.com.ezvida.rst.service;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.model.DepartamentoRegionalProdutoServico;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class DepartamentoRegionalProdutoServicoServiceTest extends BaseService {

	private static final long serialVersionUID = -2566226647100813356L;
	
	@InjectMocks
	@Spy
	private DepartamentoRegionalProdutoServicoService service;
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testDesativarIdNull() {
		boolean erro = false;
		try {
			service.desativarDepartamentoRegionalProdutoServico(new DepartamentoRegionalProdutoServico(), new ClienteAuditoria());
		} catch (Exception e) {
			erro = true;
		}
		Assert.assertEquals(erro, true);
	}
	
	@Test
	public void testDesativarIdNullMSG() {
		String erro = "";
		try {
			service.desativarDepartamentoRegionalProdutoServico(new DepartamentoRegionalProdutoServico(), new ClienteAuditoria());
		} catch (Exception e) {
			erro = e.getMessage();
		}
		Assert.assertEquals(erro, getMensagem("app_rst_generic_itens_not_add"));
	}
	
	@Test
	public void testSalvarIdNull() {
		boolean erro = false;
		try {
			service.salvar(new DepartamentoRegionalProdutoServico());
		} catch (Exception e) {
			erro = true;
		}
		Assert.assertEquals(erro, true);
	}
	
	@Test
	public void testSalvarIdNullMSG() {
		String erro = "";
		try {
			service.salvar(null, new HashSet<>(), new ClienteAuditoria());
		} catch (Exception e) {
			erro = e.getMessage();
		}
		Assert.assertEquals(erro, getMensagem("app_rst_generic_itens_not_add"));
	}
	
	
	
	
	
	
	

}
