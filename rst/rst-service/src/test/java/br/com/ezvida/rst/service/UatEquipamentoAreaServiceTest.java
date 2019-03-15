package br.com.ezvida.rst.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatEquipamentoAreaDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatEquipamentoArea;

@RunWith(MockitoJUnitRunner.class)
public class UatEquipamentoAreaServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoAreaServiceTest.class);

	@InjectMocks
	private UatEquipamentoAreaService uatEquipamentoAreaService;
	
	@Mock
	private UatEquipamentoAreaDAO uatEquipamentoAreaDAO;

	@Test
	public void deveTrazerListaComTodosEquipamentoArea() throws Exception {
		LOGGER.info("Testando listar Todos");
		
		List<UatEquipamentoArea> list = Arrays.asList(Mockito.mock(UatEquipamentoArea.class));
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		
		Mockito.when(uatEquipamentoAreaDAO.pesquisarTodos()).thenReturn(list);
		
		List<UatEquipamentoArea> retorno = uatEquipamentoAreaService.listarTodos(auditoria);
		
		assertEquals(list, retorno);
	}
	

}
