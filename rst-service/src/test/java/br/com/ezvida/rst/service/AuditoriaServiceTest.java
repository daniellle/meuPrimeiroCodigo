package br.com.ezvida.rst.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ezvida.rst.dao.filter.AuditoriaFilter;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class AuditoriaServiceTest extends BaseService{

	private static final long serialVersionUID = -3354463242860494103L;
	
	@InjectMocks
	@Spy
	private AuditoriaService service;
	
	AuditoriaFilter auditoriaFilter = new AuditoriaFilter();
	
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoriaFilter.setDataInicial("10/10/2017");
		auditoriaFilter.setDataFinal("13/10/2017");
		auditoriaFilter.setFuncionalidade(Funcionalidade.TODOS.getCodigoJson());
		auditoriaFilter.setTipoOperacaoAuditoria(TipoOperacaoAuditoria.TODOS.getCodigoJson());
		auditoriaFilter.setPagina(1);
		
	}
	
	@Test
	public void testPesquisarPorFiltroNoDates() {
		boolean erro = false;
		try {
			
			service.pesquisarPorFiltro(auditoriaFilter, new DadosFilter(),null);
		} catch (Exception e) {
			erro = true;
		}
		Assert.assertEquals(erro, true);
	}
	
	@Test
	public void testPesquisarPorFiltroDatesNoFiltro() {
		boolean erro = false;
		try {
			service.pesquisarPorFiltro(null, null,null);
		} catch (Exception e) {
			erro = true;
		}
		Assert.assertEquals(erro, true);
	}
	
	@Test
	public void testPesquisarPorFiltro() {
		boolean erro = false;
		try {
			
			auditoriaFilter.setUsuario("9999");
			service.pesquisarPorFiltro(auditoriaFilter, new DadosFilter(),null);
		} catch (Exception e) {
			erro = true;
		}
		Assert.assertEquals(erro, true);
	}


}
