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
import br.com.ezvida.rst.dao.UatEquipamentoTipoDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatEquipamentoTipo;

@RunWith(MockitoJUnitRunner.class)
public class UatEquipamentoTipoServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoTipoServiceTest.class);

	@InjectMocks
	private UatEquipamentoTipoService uatEquipamentoTipoService;
	
	@Mock
	private UatEquipamentoTipoDAO uatEquipamentoTipoDAO;

	@Test
	public void deveTrazerListaComTodosEquipamentoTipoPorArea() throws Exception {
		LOGGER.info("Testando listar EquipamentoTipo por area");
		
		List<UatEquipamentoTipo> list = Arrays.asList(Mockito.mock(UatEquipamentoTipo.class));
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		
		Mockito.when(uatEquipamentoTipoDAO.pesquisarPorIdArea(Mockito.anyLong())).thenReturn(list);
		
		List<UatEquipamentoTipo> retorno = uatEquipamentoTipoService.listarTodosPorArea(1L, auditoria);
		
		assertEquals(list, retorno);
	}
	

}
