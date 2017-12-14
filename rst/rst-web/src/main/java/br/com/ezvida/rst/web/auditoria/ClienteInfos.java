package br.com.ezvida.rst.web.auditoria;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.interfaces.Payload;
import com.google.common.collect.Sets;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;
import br.com.ezvida.rst.model.Usuario;
import fw.core.common.encode.JacksonMapper;
import fw.security.model.PrincipalUsuario;

public class ClienteInfos {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteInfos.class);

	public static ClienteAuditoria getClienteInfos(SecurityContext context, HttpServletRequest request,
			TipoOperacaoAuditoria tipoOperacao, Funcionalidade funcionalidade){
		ClienteAuditoria clienteInfo = new ClienteAuditoria();
		Payload payload = ((PrincipalUsuario) context.getUserPrincipal()).getPayload();
		clienteInfo.setUsuario(payload.getClaims().get("sub").asString());
		clienteInfo.setNavegador(request.getHeader("User-Agent"));	
		clienteInfo.setTipoOperacao(tipoOperacao);
		clienteInfo.setFuncionalidade(funcionalidade);		
		return clienteInfo;
	}

	public static final Usuario getUsuario(SecurityContext context){
		Payload payload = ((PrincipalUsuario) context.getUserPrincipal()).getPayload();
		String nome = payload.getClaims().get("nome").asString();
		String email = payload.getClaims().get("email").asString();
		String login = payload.getClaims().get("sub").asString();
		Usuario usuario = new Usuario(null, login, nome, null, null, email);
		usuario.getPapeis().addAll(Sets.newHashSet(payload.getClaims().get("papeis").asList(String.class)));
		usuario.getPermissoes().addAll(Sets.newHashSet(payload.getClaims().get("permissoes").asList(String.class)));
		return usuario;
	}

	public static final DadosFilter getDadosFilter(SecurityContext context) {
		Payload payload = ((PrincipalUsuario) context.getUserPrincipal()).getPayload();

		JacksonMapper mapper = new JacksonMapper();
		DadosFilter oject = null;
		try {
			oject = mapper.readValue(payload.getClaim("dados").asString(), DadosFilter.class);
		} catch (Exception e) {
			LOGGER.error("Erro ao conveter filtro de dados do usuário", e);
		}
		return oject;
	}
}
