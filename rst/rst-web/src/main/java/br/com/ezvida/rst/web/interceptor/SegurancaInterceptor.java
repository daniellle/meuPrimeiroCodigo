package br.com.ezvida.rst.web.interceptor;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;

import com.auth0.jwt.interfaces.Payload;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import br.com.ezvida.rst.utils.SegurancaUtils;
import fw.security.binding.Autenticacao;
import fw.security.exception.UnauthorizedException;
import fw.security.interceptor.ChaveSeguranca;
import fw.security.interceptor.TipoOAuth;
import fw.security.interceptor.ValidacaoSeguranca;
import fw.security.request.OAuthAccessTokenResourceRequest;
import fw.security.request.OAuthAssertionResourceRequest;
import fw.security.request.OAuthRefreshTokenResourceRequest;

abstract class SegurancaInterceptor implements Serializable {

	private static final long serialVersionUID = 2084625442842225421L;

	Payload validarAutenticacao(InvocationContext context) throws OAuthProblemException, OAuthSystemException, UnsupportedEncodingException {

		Optional<Object> optional = Arrays.stream(context.getParameters()).filter(parametro -> parametro instanceof HttpServletRequest).findAny();

		if (optional == null || !optional.isPresent()) {
			throw new UnauthorizedException("Usuário não tem autorização para acessar essa aplicação");
		}

		HttpServletRequest request = (HttpServletRequest) optional.get();

		String login = SegurancaUtils.validarAutenticacao(request);

		Map<TipoOAuth, String> token = getAccessToken(request);

		if (context.getMethod().getDeclaredAnnotation(Autenticacao.class) != null) {

			Autenticacao autenticacao = context.getMethod().getDeclaredAnnotation(Autenticacao.class);

			if (TipoOAuth.REFRESH_TOKEN.equals(autenticacao.value())) {
				token = getRefreshToken(request);
			} else if (TipoOAuth.JWT_BEARER.equals(autenticacao.value())) {
				token = getAssertion(request);
			}

		}

		if (token == null || token.isEmpty()) {
			throw new UnauthorizedException("Usuário não tem autorização para acessar essa aplicação");
		}

		Map.Entry<TipoOAuth, String> entry = Iterables.getLast(token.entrySet());

		Payload payload = ChaveSeguranca.getInstance().validar(entry.getValue(), entry.getKey());

		if (payload != null && context.getTarget() instanceof ValidacaoSeguranca && payload.getSubject().equals(login)) {
			((ValidacaoSeguranca) context.getTarget()).validar(payload);
		}

		return payload;

	}

	private Map<TipoOAuth, String> getAccessToken(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

		OAuthAccessTokenResourceRequest resource = new OAuthAccessTokenResourceRequest(request, ParameterStyle.HEADER);

		if (StringUtils.isNotBlank(resource.getAccessToken())) {
			return ImmutableMap.of(TipoOAuth.ACCESS_TOKEN, resource.getAccessToken());
		}

		return Collections.emptyMap();

	}

	private Map<TipoOAuth, String> getRefreshToken(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

		OAuthRefreshTokenResourceRequest resource = new OAuthRefreshTokenResourceRequest(request);

		if (Strings.isNullOrEmpty(resource.getRefreshToken())) {
			throw new UnauthorizedException("Usuário não tem autorização para acessar essa aplicação");
		}

		return ImmutableMap.of(TipoOAuth.REFRESH_TOKEN, resource.getRefreshToken());

	}

	private Map<TipoOAuth, String> getAssertion(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

		OAuthAssertionResourceRequest resource = new OAuthAssertionResourceRequest(request);

		if (Strings.isNullOrEmpty(resource.getAssertion())) {
			throw new UnauthorizedException("Usuário não tem autorização para acessar essa aplicação");
		}

		return ImmutableMap.of(TipoOAuth.JWT_BEARER, resource.getAssertion());

	}

}
