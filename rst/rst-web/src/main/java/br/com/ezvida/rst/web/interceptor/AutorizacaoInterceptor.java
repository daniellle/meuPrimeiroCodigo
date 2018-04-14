package br.com.ezvida.rst.web.interceptor;

import java.text.MessageFormat;
import java.util.Iterator;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import fw.security.binding.Autorizacao;
import fw.security.binding.Papel;
import fw.security.binding.Permissao;
import fw.security.exception.SecurityException;
import fw.security.exception.UnauthenticatedException;
import fw.security.exception.UnauthorizedException;
import fw.security.interceptor.Logico;

@Interceptor
@Autorizacao
public class AutorizacaoInterceptor extends SegurancaInterceptor {

	private static final long serialVersionUID = 8094963683731360819L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AutorizacaoInterceptor.class);

	@AroundInvoke
	public Object interceptar(InvocationContext context) throws Exception {

		if (ArrayUtils.isEmpty(context.getParameters())) {
			throw new UnauthorizedException("Usuário não tem autorização para acessar essa aplicação");
		}

		try {

			Payload payload = validarAutenticacao(context);

			if (payload == null) {
				throw new UnauthorizedException("Usuário não tem autorização para acessar essa aplicação");
			}

			if (payload.getExpiresAt() != null && LOGGER.isTraceEnabled()) {
				LOGGER.trace("Data de Expiração: {}", DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(payload.getExpiresAt()));
			}

			validarAutorizacao(payload, context.getMethod().getDeclaredAnnotation(Autorizacao.class));

		} catch (UnauthenticatedException e) {
			throw new UnauthenticatedException(e.getMessage(), e);
		} catch (UnauthorizedException e) {
			throw new UnauthorizedException(e.getMessage(), e);
		} catch (Exception e) {
			throw new SecurityException("Houve um erro ao tentar autorizar o usuário", e);
		}

		return context.proceed();

	}

	private void validarAutorizacao(Payload payload, Autorizacao seguranca) {

		if (payload == null || seguranca == null) {
			return;
		}

		if (ArrayUtils.isNotEmpty(seguranca.papeis())) {
			for (Papel papel : seguranca.papeis()) {
				validarRestricoes(payload.getClaim("papeis"), papel.logico(), papel.value());
			}
		}

		if (ArrayUtils.isNotEmpty(seguranca.permissoes())) {
			for (Permissao permissao : seguranca.permissoes()) {
				validarRestricoes(payload.getClaim("permissoes"), permissao.logico(), permissao.value());
			}
		}

	}

	private void validarRestricoes(Claim claim, Logico logico, String... restricoes) {

		String mensagem = "Usuário não tem permissões as restrições necessárias";

		if (ArrayUtils.isEmpty(restricoes)) {
			return;
		}

		if (claim == null) {
			throw new UnauthorizedException(mensagem);
		}

		Iterator<String> claims = Lists.newArrayList(restricoes).iterator();

		if (Logico.OR.equals(logico) && !Iterators.any(claims, Predicates.in(claim.asList(String.class)))) {
			throw new UnauthorizedException(MessageFormat.format("Usuário não tem pelo menos uma das permissões [{0}] necessárias", restricoes));
		}

		if (Logico.AND.equals(logico) && !Iterators.all(claims, Predicates.in(claim.asList(String.class)))) {
			throw new UnauthorizedException(MessageFormat.format("Usuário não tem as permissões [{0}] necessárias", restricoes));
		}

	}

}
