package br.com.ezvida.rst.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.ezvida.rst.anotacoes.Preferencial;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Payload;

import br.com.ezvida.girst.apiclient.client.CredencialClient;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.Token;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.utils.SegurancaUtils;
import fw.core.common.encode.JacksonMapper;
import fw.core.service.BaseService;
import fw.security.exception.UnauthenticatedException;
import fw.security.exception.UnauthorizedException;
import fw.security.interceptor.ChaveSeguranca;
import fw.security.interceptor.TipoOAuth;

@Stateless
public class CredencialService extends BaseService {

    private static final long serialVersionUID = 8235659745814804044L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CredencialService.class);

    @Inject
	@Preferencial
    private UsuarioService usuarioService;
    
    @Inject
    private CredencialClient credencialClient;
    
    @Inject
    private APIClientService apiClientService;


    @Inject
	private SegurancaUtils segurancaUtils;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Token validar(HttpServletRequest request) {

        try {
			String login = segurancaUtils.validarAutenticacao(request);

			LOGGER.debug("Solicitando autorizacao do usuario [ {} ]", login);
            
			return gerarToken(usuarioService.getUsuario(login));

		} catch (UnauthenticatedException e) {
			throw new UnauthenticatedException(e.getMessage(), e);
		} catch (Exception e) {
            throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"), e);
        }

    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Token validar(@Nonnull Payload payload) {

		Usuario usuario = usuarioService.getUsuario(payload.getSubject());

		if (usuario == null) {
			throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"));
		}

		try {

			return gerarToken(usuario);

		} catch (UnsupportedEncodingException e) {
			throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"), e);
		}

	}

    private Token gerarToken(Usuario usuario) throws UnsupportedEncodingException {
		LOGGER.debug("Criando filtro de dados");

		DadosFilter dadosFilter = new DadosFilter(usuario.getPapeis(), usuario.getIdDepartamentos(), usuario.getIdEmpresas(),
				usuario.getIdParceiros(), usuario.getIdRedesCredenciadas(), usuario.getIdSindicatos(), usuario.getIdTrabalhadores());

		String dados = StringUtils.EMPTY;
		try {
			JacksonMapper mapper = new JacksonMapper();
			dados = new String(mapper.writeValueAsBytes(dadosFilter), "UTF-8");
		} catch (Exception e) {
			LOGGER.error("Erro ao tentar converter dados filter", e);
		}

		LOGGER.debug("Gerando token do usuario");
        //@formatter:off
        String tokenAcesso = ChaveSeguranca.getInstance().gerar(
            JWT.create().withSubject(usuario.getLogin())
                .withClaim("nome", usuario.getNome())
                .withClaim("email", usuario.getEmail())
                .withArrayClaim("papeis", usuario.getPapeis().stream().toArray(String[]::new))
                .withArrayClaim("permissoes", usuario.getPermissoes().stream().toArray(String[]::new))
                .withClaim("dados", dados)
                .withExpiresAt(DateUtils.addSeconds(new Date(), Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN)), TipoOAuth.ACCESS_TOKEN);
        //@formatter:on

		LOGGER.debug("Gerando token de atualizacao do usuario");
        //@formatter:off
        String tokenAtualizacao = ChaveSeguranca.getInstance().gerar(
            JWT.create().withSubject(usuario.getLogin())
                .withClaim("nome", usuario.getNome())
                .withClaim("email", usuario.getEmail())
                .withArrayClaim("papeis", usuario.getPapeis().stream().toArray(String[]::new))
                .withArrayClaim("permissoes", usuario.getPermissoes().stream().toArray(String[]::new))
                .withClaim("dados", dados)
						.withExpiresAt(DateUtils.addSeconds(new Date(),
								Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN * 24 * 30 * 6)),
				TipoOAuth.JWT_BEARER);
        //@formatter:on

        return new Token(usuario, tokenAcesso, 3600L, (long) (Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN * 24 * 30 * 6), OAuth.OAUTH_HEADER_NAME,
                tokenAtualizacao);

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String recuperarSenha(String email) {
		br.com.ezvida.girst.apiclient.model.Usuario usuario = usuarioService.buscarPorEmail(email);
		
		br.com.ezvida.girst.apiclient.model.Credencial credencial = new br.com.ezvida.girst.apiclient.model.Credencial();
		credencial.setUsuario(usuario.getLogin());
		return credencialClient.recuperarSenha(apiClientService.getURL(), credencial);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void alterarSenha(Map<String, String> propriedades) {
        credencialClient.alterarSenhaComHash(apiClientService.getURL(), propriedades, null);
    }

}