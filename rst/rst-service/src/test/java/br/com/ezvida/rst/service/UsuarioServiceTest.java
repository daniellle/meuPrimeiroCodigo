package br.com.ezvida.rst.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.girst.apiclient.client.UsuarioClient;
import br.com.ezvida.girst.apiclient.model.Usuario;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.enums.Funcionalidade;
import fw.core.service.BaseService;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest extends BaseService{

	private static final long serialVersionUID = 2886040246272407578L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioServiceTest.class);


	@InjectMocks
	private UsuarioService usuarioService;
	@InjectMocks
    private UsuarioClient usuarioClient;
	
	private Usuario usuario;
	private ClienteAuditoria auditoria = new ClienteAuditoria();
	
	@Before
	public void inicializar() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.usuario = new Usuario();
		this.usuario.setId(1L);
		this.usuario.setLogin("999999999");
		this.usuario.setEmail("usuario@mockito.com");
		
		auditoria.setNavegador("navegador_teste");
		auditoria.setUsuario("usuario_teste");
		auditoria.setFuncionalidade(Funcionalidade.USUARIOS);
	}
	
	
	@Test
	public void cadastrarUsuarioNulo() throws Exception {
		LOGGER.debug("Testando salvar usuário Nulo");
		String mensagemErro = "";
		try {
			Usuario usuario = new Usuario();
			usuarioService.cadastrarUsuario(usuario,auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, null);
	}
	
	@Test
	public void alterarUsuario() throws Exception {
		LOGGER.debug("Testando alterar usuário ");
		String mensagemErro = "";
		try {
			this.usuario.setNome("Usuário Alterado");
				
			Mockito.when(usuarioService.alterarUsuario(this.usuario, this.auditoria)).thenReturn(this.usuario);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, " Nenhum registro encontrado");
	}
	
	@Test
	public void desativarUsuario() throws Exception {
		LOGGER.debug("Testando desativar usuário ");
		String mensagemErro = "";
		try {				
			usuarioService.desativarUsuario(this.usuario.getId().toString(),auditoria);
		} catch (Exception e) {
			mensagemErro = e.getMessage();
		}

		Assert.assertEquals(mensagemErro, null);
	}
}
