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
import br.com.ezvida.rst.dao.CertificadoDAO;
import br.com.ezvida.rst.dao.filter.CertificadoFilter;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Certificado;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class CertificadoServiceTest extends BaseService {
	
	private static final long serialVersionUID = -3544102412480333762L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CertificadoServiceTest.class);
	private ClienteAuditoria auditoria = new ClienteAuditoria();
	
	@Mock
	private CertificadoDAO certificadoDAO;

	@InjectMocks
	@Spy
	private CertificadoService certificadoService;
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void pesquisar() throws Exception {
		LOGGER.debug("Testando pesquisar Certificado");
		CertificadoFilter filtro = new CertificadoFilter();
		filtro.setId(1L);
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			certificadoService.pesquisarPaginado(filtro);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, "");

	}

	@Test
	public void buscarComId() throws Exception {
		LOGGER.debug("Testando buscar Certificado");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			certificadoService.buscarPorId(new Long(1), this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_nenhum_registro_encontrado"));
	}

	@Test
	public void buscarComIdNull() throws Exception {
		LOGGER.debug("Testando buscar Certificado");
		String mensagemErro = "";
		try {
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
			certificadoService.buscarPorId(null, this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, getMensagem("app_rst_parametro_nulo"));
	}

	@Test
	public void cadastrarNovo() throws Exception {
		LOGGER.debug("Testando salvar Certificado");
		String mensagemErro = "";
		try {
			
			Certificado certificado = new Certificado();
			certificado.setId(1L);
			
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			certificadoService.salvar(certificado,this.auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, null);
				
	}

}
