package br.com.ezvida.rst.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UsuarioEntidadeDAO;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.UsuarioEntidade;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioEntidadeServiceTest extends BaseService {

	private static final long serialVersionUID = -6561500819720228501L;
	@InjectMocks
	@Spy
	private UsuarioEntidadeService usuarioEntidadeService;
	@Mock
	private UsuarioEntidadeDAO usuarioEntidadeDAO;

	private ClienteAuditoria auditoria = new ClienteAuditoria();
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.USUARIOS);
	}
	
	@Test
	public void salvar() throws Exception {
		String mensagemErro = "";
		try {
			UsuarioEntidade esuarioEntidade = new UsuarioEntidade();
			esuarioEntidade.setCpf("12345678951");
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			usuarioEntidadeService.salvar(esuarioEntidade, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}
	
	@Test
	public void salvarListaVazia() throws Exception {
		String mensagemErro = "";
		try {
			List<UsuarioEntidade> lista = new ArrayList<>();
			
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.INCLUSAO);
			usuarioEntidadeService.salvar(lista, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, getMensagem("app_rst_generic_itens_not_add"));
	}
	
	@Test
	public void alterar() throws Exception {
		String mensagemErro = "";
		try {
			UsuarioEntidade esuarioEntidade = new UsuarioEntidade();
			esuarioEntidade.setId(1L);
			this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.ALTERACAO);
			usuarioEntidadeService.salvar(esuarioEntidade, auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}
	
	@Test
	public void desativar() throws Exception {
		String mensagemErro = "";
		UsuarioEntidade esuarioEntidade = new UsuarioEntidade();
		esuarioEntidade.setId(1L);
		
		UsuarioEntidade esuarioEntidade2 = new UsuarioEntidade();
		esuarioEntidade2.setId(1L);
		Mockito.when(usuarioEntidadeDAO.pesquisarPorId(esuarioEntidade.getId())).thenReturn(esuarioEntidade);
		this.auditoria.setTipoOperacao(TipoOperacaoAuditoria.DESATIVACAO);
		usuarioEntidadeService.desativarUsuarioEntidade(esuarioEntidade, auditoria);
		try {
			
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}
		Assert.assertEquals(mensagemErro, "");
	}
	
	
}
