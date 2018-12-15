package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.client.CredencialClient;
import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.Credencial;
import br.com.ezvida.rst.model.Token;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.utils.SegurancaUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Payload;
import fw.core.common.encode.JacksonMapper;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthenticatedException;
import fw.security.exception.UnauthorizedException;
import fw.security.interceptor.ChaveSeguranca;
import fw.security.interceptor.TipoOAuth;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

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
    private EmpresaTrabalhadorLotacaoService empresaTrabalhadorLotacaoService;


    @Inject
	private SegurancaUtils segurancaUtils;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Token validar(HttpServletRequest request) {

        try {
			String login = segurancaUtils.validarAutenticacao(request);

			LOGGER.debug("Solicitando autorizacao do usuario [ {} ]", login);



			empresaTrabalhadorLotacaoService.validarTrabalhador(login);

			return gerarToken(usuarioService.getUsuario(login));

		} catch (BusinessErrorException e){
			LOGGER.error(e.getMessage(), e);
			throw new BusinessErrorException(getMensagem("app_rst_empregado_invalido"));
		} catch (UnauthenticatedException e) {
			LOGGER.error(e.getMessage(), e);
			throw new UnauthenticatedException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Erro ao gerar token de autorização do usuário para o sistema", e);
            throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"), e);
        }

    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Token validar(String login) {

		try {
			LOGGER.debug("Solicitando autorizacao do usuario  [ {} ] para o sistema", login);

			return gerarToken(usuarioService.getUsuario(login), null, null, null, null, null, Credencial.CLIENTE.toString());

		} catch (UnauthenticatedException e) {
			LOGGER.error("Erro ao gerar token de autorização do usuário para o sistema", e);
			throw new UnauthenticatedException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Erro ao gerar token de autorização do usuário para o sistema", e);
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
		LOGGER.info("Criando filtro de dados", usuario.getLogin(), usuario.getId(), usuario);

		DadosFilter dadosFilter = new DadosFilter(usuario.getPapeis(), usuario.getIdDepartamentos(), usuario.getIdEmpresas(),
				usuario.getIdParceiros(), usuario.getIdRedesCredenciadas(), usuario.getIdSindicatos(), usuario.getIdTrabalhadores(), usuario.getIdUnidadesSESI());

		String dados = StringUtils.EMPTY;
		try {
			JacksonMapper mapper = new JacksonMapper();
			dados = new String(mapper.writeValueAsBytes(dadosFilter), "UTF-8");
		} catch (Exception e) {
			LOGGER.error("Erro ao tentar converter dados filter", e);
		}

		Date expiracaoToken = DateUtils.addSeconds(new Date(), Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN);
		Date expiracaoAtualizacaoToken = DateUtils.addSeconds(new Date(), Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN * 24 * 30 * 6);
		Long expiraEm = 3600L;
		Long atualizacaoExpiraEm = (long) (Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN * 24 * 30 * 6);

		return gerarToken(usuario, expiracaoToken, expiracaoAtualizacaoToken, expiraEm, atualizacaoExpiraEm, dados, Credencial.USUARIO.toString());

	}

	private Token gerarToken(Usuario usuario, Date expiracaoToken, Date expiracaoAtualizacaoToken, Long expiraEm, Long atualizacaoExpiraEm,
			String dados, String credencial) throws UnsupportedEncodingException {
		LOGGER.info("Gerando token do usuario", usuario);

		//@formatter:off
        String tokenAcesso = ChaveSeguranca.getInstance().gerar(
            JWT.create().withSubject(usuario.getLogin())
                .withClaim("nome", usuario.getNome())
                .withClaim("email", usuario.getEmail())
				.withClaim("nivel", usuario.getHierarquia())
                .withArrayClaim("papeis", usuario.getPapeis().stream().toArray(String[]::new))
                .withArrayClaim("permissoes", usuario.getPermissoes().stream().toArray(String[]::new))
                .withClaim("dados", dados)
                .withClaim("credencial", credencial)
                .withExpiresAt(expiracaoToken), TipoOAuth.ACCESS_TOKEN);
        //@formatter:on

		LOGGER.debug("Gerando token de atualizacao do usuario");
		//@formatter:off
            String tokenAtualizacao = ChaveSeguranca.getInstance().gerar(
                JWT.create().withSubject(usuario.getLogin())
                    .withClaim("nome", usuario.getNome())
                    .withClaim("email", usuario.getEmail())
					.withClaim("nivel", usuario.getHierarquia())
                    .withArrayClaim("papeis", usuario.getPapeis().stream().toArray(String[]::new))
                    .withArrayClaim("permissoes", usuario.getPermissoes().stream().toArray(String[]::new))
                    .withClaim("dados", dados)
                    .withClaim("credencial", credencial)
    				.withExpiresAt(expiracaoAtualizacaoToken),
    				TipoOAuth.JWT_BEARER);
            //@formatter:on

		LOGGER.debug("Retornando token gerada");
		return new Token(usuario, tokenAcesso, expiraEm, atualizacaoExpiraEm, OAuth.OAUTH_HEADER_NAME, tokenAtualizacao);

	}

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String recuperarSenha(String email) {
		br.com.ezvida.girst.apiclient.model.Usuario usuario = usuarioService.buscarPorEmail(email);

		if(usuario.getPerfisSistema().isEmpty()){
		 LOGGER.error("Usuário sem perfil não pode alterar senha");
		 throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"));
		 }
		
		br.com.ezvida.girst.apiclient.model.Credencial credencial = new br.com.ezvida.girst.apiclient.model.Credencial();
		credencial.setUsuario(usuario.getLogin());
		return credencialClient.recuperarSenha(apiClientService.getURL(), credencial);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void alterarSenha(Map<String, String> propriedades) {
        credencialClient.alterarSenhaComHash(apiClientService.getURL(), propriedades, null);
    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String verificarHash(String hashRecuperacaoSenha) {
		return credencialClient.verificarHashRecuperacaoSenha(apiClientService.getURL(), hashRecuperacaoSenha);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String reenviarEmailHash(String hashRecuperacaoSenha) {
		return credencialClient.reenviarEmailRecuperacaoSenha(apiClientService.getURL(), hashRecuperacaoSenha);
	}

}