package br.com.ezvida.rst.service;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.ezvida.girst.apiclient.model.Credencial;
import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.model.UsuarioGirstView;
import br.com.ezvida.rst.model.dto.UsuarioDTO;

public interface UsuarioService extends Serializable {
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    Usuario getUsuario(String login);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    Map<String, String> getConfiguracao(Ambiente ambiente);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    ListaPaginada<br.com.ezvida.girst.apiclient.model.Usuario> pesquisarPaginado(
            br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.rst.dao.filter.ListaPaginada<UsuarioGirstView> pesquisarPaginadoGirst(
            br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.girst.apiclient.model.Usuario buscarPorId(String id, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario cadastrarUsuario(
            br.com.ezvida.girst.apiclient.model.Usuario usuario
            , ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario alterarUsuario(
            br.com.ezvida.girst.apiclient.model.Usuario usuario, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario alterarPerfil(br.com.ezvida.girst.apiclient.model.Usuario usuario, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario desativarUsuario(String id, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.girst.apiclient.model.Usuario buscarPorEmail(String email);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.girst.apiclient.model.Usuario buscarPorLogin(String login);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    boolean isAdm(String cpf);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	UsuarioDTO consultarDadosUsuario(String login);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void alterarSenhaRST(Credencial credencial);
}
