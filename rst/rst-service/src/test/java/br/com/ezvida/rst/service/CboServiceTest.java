package br.com.ezvida.rst.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.CboDAO;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class CboServiceTest extends BaseService {

	private static final long serialVersionUID = -7377534974264288182L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CboServiceTest.class);
	
	private ClienteAuditoria auditoria = new ClienteAuditoria();

	@InjectMocks
	@Spy
	private CboService cboService;

	@Mock
	private CboDAO cboDAO;

	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void pesquisarPorIdNulo() throws Exception {
		LOGGER.info("Testando pesquisar Cbo por id nulo");
		String mensagemErro = "";
		try {
			cboService.pesquisarPorId(null, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_id_consulta_nulo"));
	}

}
