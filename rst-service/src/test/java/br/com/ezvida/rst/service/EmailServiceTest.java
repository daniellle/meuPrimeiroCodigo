package br.com.ezvida.rst.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.EmailDAO;
import br.com.ezvida.rst.model.Email;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest extends BaseService{

	private static final long serialVersionUID = 4358111657128544366L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceTest.class);

	@Mock
	EmailDAO emailDAO;

	@InjectMocks
	EmailService emailService;
	
	@Test
	public void cadastrarNovoCnpjInvalido() throws Exception {
		LOGGER.debug("Testando salvar Email inválido");
		String mensagemErro = "";
		try {
			Email email = new Email();
			email.setDescricao("teste");
			emailService.salvar(email);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_campo_invalido", getMensagem("app_rst_label_email")));
	}
}
