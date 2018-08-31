package br.com.ezvida.rst.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.DependenteDAO;
import br.com.ezvida.rst.model.Dependente;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.TrabalhadorDependente;

@RunWith(MockitoJUnitRunner.class)
public class TrabalhadorDependenteServiceTest {
	
	@InjectMocks
	private DependenteService dependenteService;

	@Mock
	private DependenteDAO dependenteDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorServiceTest.class);
	
	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
	}
	
	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar novo dependente");
		String mensagemErro = "";
		try {
			Trabalhador trabalhador = new Trabalhador();
			trabalhador.setId(1L);
			trabalhador.setCpf("59941882630");

			Dependente dependente = new Dependente();
			dependente.setId(1L);
			dependente.setNome("Teste");
			dependente.setCpf("59941882630");

			TrabalhadorDependente trabalhadorDependente = new TrabalhadorDependente();
			trabalhadorDependente.setId(1L);
			trabalhadorDependente.setDependente(dependente);
			dependenteService.salvar(dependente);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");
				
	}

}
