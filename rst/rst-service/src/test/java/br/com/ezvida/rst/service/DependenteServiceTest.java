package br.com.ezvida.rst.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ezvida.rst.model.Dependente;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class DependenteServiceTest extends BaseService{

	private static final long serialVersionUID = 5912849835337591203L;
	
	@InjectMocks
	@Spy
	private DependenteService service;
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidacoesCPF() {
		Dependente dependente = new Dependente();
		dependente.setCpf("0000000000");
		String msg = "";
		try {
			service.salvar(dependente);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		Assert.assertEquals(msg, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_cpf")));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testValidacoesDate() {
		Dependente dependente = new Dependente();
		dependente.setCpf("27823516560");
		Date dataNascimento = new Date();
		dataNascimento.setYear(dataNascimento.getYear() + 1);
		dependente.setDataNascimento(dataNascimento);
		String msg = "";
		try {
			service.salvar(dependente);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		Assert.assertEquals(msg, getMensagem("app_rst_data_maior_que_atual", getMensagem("app_rst_label_data_nascimento")));
	}

}
