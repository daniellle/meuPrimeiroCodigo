package br.com.ezvida.rst.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.model.EmpresaTrabalhador;
import br.com.ezvida.rst.model.EmpresaTrabalhadorLotacao;
import fw.core.service.BaseService;

public class EmpresaTrabalhadorLotacaoServiceTest extends BaseService{

	private static final long serialVersionUID = -3540564757267779857L;
	

	@InjectMocks
	@Spy
	private EmpresaTrabalhadorLotacaoService service;
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testValidacoessetDataAssociacao() {
		EmpresaTrabalhadorLotacao empresaTrabalhadorLotacao = new EmpresaTrabalhadorLotacao();
		empresaTrabalhadorLotacao.setEmpresaTrabalhador(new EmpresaTrabalhador());
		empresaTrabalhadorLotacao.getEmpresaTrabalhador().setDataAdmissao(new Date());
		Date dataAssociacao = new Date();
		dataAssociacao.setYear(dataAssociacao.getYear() + 1);
		empresaTrabalhadorLotacao.setDataAssociacao(dataAssociacao);
		boolean msg = false;
		try {
			service.salvar(empresaTrabalhadorLotacao, new ClienteAuditoria());
		} catch (Exception e) {
			msg = true;
		}
		
		Assert.assertEquals(msg,true);
	}
	
}
