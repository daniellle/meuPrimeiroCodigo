package br.com.ezvida.rst.utils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.enums.Ambiente;
import fw.security.exception.UnauthenticatedException;

public class SegurancaUtils {

    private static final String SHIB_SESSION_ID = "Shib-Session-ID";
    private static final String SHIB_SESSION_LOGIN = "Shib-Session-Login";
    private static final Logger LOGGER = LoggerFactory.getLogger(SegurancaUtils.class);


    @Inject
    private Ambiente ambiente;

    public String validarAutenticacao(HttpServletRequest request) {
        if (ambiente == Ambiente.DESENVOLVIMENTO) {
            LOGGER.warn(" UTILIZANDO SEGURANÇA DO AMBIENTE DE DESENVOLVIMENTO");
			return "65020081515"; // administrador
            // return "15138902040"; // gestor dn
            // return "01863352538"; // gestor dr
            // return "03004596539"; // gestor empresa
            // return "46335766744"; // trabalhador
			// return "37139762520"; // carol adm
        }
        LOGGER.debug("SessionLogin {}", request.getAttribute(SHIB_SESSION_LOGIN));
        if (request.getAttribute(SHIB_SESSION_ID) == null || request.getAttribute(SHIB_SESSION_LOGIN) == null) {
            throw new UnauthenticatedException("Erro ao obter parametros de autenticacao");
        }

        String sessionId = (String) request.getAttribute(SHIB_SESSION_ID);
        String sessionLogin = (String) request.getAttribute(SHIB_SESSION_LOGIN);

        if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(sessionLogin)) {
            throw new UnauthenticatedException("Parametros de autenticacao vazios");
        }
        return sessionLogin;
    }

}
