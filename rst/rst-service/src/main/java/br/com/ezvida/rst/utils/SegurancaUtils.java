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
            //return "08194790980"; // trabalhador
           //return "28928105900";
            // return "80782078168";
//            return "86010158168"; // adm do grupo
             return  "65020081515"; // administrador
          //  return "37139762520"; // adm
          //  return "65020081515"; // administrador
            //return "37139762520"; // adm
//             return "09934689421"; // dna
//             return "48552219026"; // gestor dn aplicacoes
//             return "56866951135"; // gestor dr master
//            return "90468451170"; //
//            return "74055251564"; // gestor dr aplicacoes
            //return "99573911434";
            //return "84755789850";
           //  return "71141598973"; // gestor empresa master
            // return "92883645361"; // gestor empresa
//             return "46335766744"; // trabalhador
            // return "37139762520"; // carol adm
            // return "64432620005"; //profissonal saude
            // return "19743559698"; //recursos humanos
            // return "93693744181"; // MTSDN
            // return "97739727989"; // GCDN
            // return  "83855096740"; //MTSDR
           // return "78907216410"; //GCDR
             //return "95901290801"; //GUS
//            return "04265927947"; // Superintendente DR
//            return "01178192474";
//            return "83704786209";
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
