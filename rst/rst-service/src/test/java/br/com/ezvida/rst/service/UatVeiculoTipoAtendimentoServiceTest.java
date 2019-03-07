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
import br.com.ezvida.rst.dao.UatVeiculoTipoAtendimentoDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import br.com.ezvida.rst.model.dto.UatVeiculoTipoAtendimentoDTO;

@RunWith(MockitoJUnitRunner.class)
public class UatVeiculoTipoAtendimentoServiceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoTipoAtendimentoServiceTest.class);

	@InjectMocks
	private UatVeiculoTipoAtendimentoService uatVeiculoTipoAtendimentoService;

	@Mock
	private UatVeiculoTipoAtendimentoDAO uatVeiculoTipoAtendimentoDAO;

	@Test
	public void deveTrazerListaComTodosTiposDeAtendimentoVeiculos() throws Exception {
		LOGGER.info("Testando listarTodos");
		
		UatVeiculoTipoAtendimento uatVeiculoTipoAtendimento = new UatVeiculoTipoAtendimento();
		uatVeiculoTipoAtendimento.setId(1L);
		uatVeiculoTipoAtendimento.setDescricao("Descricao");
		
		ClienteAuditoria auditoria = new ClienteAuditoria();
		auditoria.setFuncionalidade(Funcionalidade.GESTAO_UNIDADE_SESI);
		auditoria.setTipoOperacao(TipoOperacaoAuditoria.CONSULTA);
		
		List<UatVeiculoTipoAtendimento> list = Arrays.asList(uatVeiculoTipoAtendimento);
		
		Mockito.doReturn(list).when(uatVeiculoTipoAtendimentoDAO).pesquisarTodos();
		
		List<UatVeiculoTipoAtendimentoDTO> retorno = uatVeiculoTipoAtendimentoService.listarTodos(auditoria);
		
		assertEquals(list.get(0).getId(), retorno.get(0).getId());
		assertEquals(list.get(0).getDescricao(), retorno.get(0).getDescricao());
	}
	

}
