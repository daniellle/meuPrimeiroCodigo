package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.model.UsuarioGirstView;
import br.com.ezvida.rst.model.dto.PerfilUsuarioDTO;
import br.com.ezvida.rst.model.dto.UsuarioDTO;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface UsuarioService extends Serializable {
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    Usuario getUsuario(String login);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    Map<String, String> getConfiguracao(Ambiente ambiente);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    ListaPaginada<br.com.ezvida.girst.apiclient.model.Usuario> pesquisarPaginado(
            br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.rst.dao.filter.ListaPaginada<UsuarioGirstView> PESQUISARPAGINADOGIRST(
            br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria);
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    List<PerfilUsuarioDTO> pesquisarPaginadoRelatorio(
            br.com.ezvida.rst.dao.filter.UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria);
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.rst.dao.filter.ListaPaginada<PerfilUsuarioDTO> pesquisarListaPaginadaPerfilUsuario(
            UsuarioFilter usuarioFilter, DadosFilter dados
            , ClienteAuditoria auditoria);
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.girst.apiclient.model.Usuario buscarPorId(String id, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario cadastrarUsuario(
            br.com.ezvida.girst.apiclient.model.Usuario usuario, Usuario usuarioLogado
            , ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario alterarUsuario(
            br.com.ezvida.girst.apiclient.model.Usuario usuario, Usuario usuarioLogado, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario sicronizarTrabalhadorUsuario(Trabalhador trabalhador, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario alterarPerfilSenha(br.com.ezvida.girst.apiclient.model.UsuarioCredencial usuarioCredencial, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    br.com.ezvida.girst.apiclient.model.Usuario desativarUsuario(String id, Usuario usuarioLogado, ClienteAuditoria auditoria);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.girst.apiclient.model.Usuario buscarPorEmail(String email);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    br.com.ezvida.girst.apiclient.model.Usuario buscarPorLogin(String login);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    boolean isAdm(String cpf);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    UsuarioDTO consultarDadosUsuario(String login);
}
