package br.com.ezvida.rst.security;

import fw.security.interceptor.ChaveSegurancaComRSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ChaveAcesso implements ChaveSegurancaComRSA {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChaveAcesso.class);

    public InputStream getChavePrivada() {

        LOGGER.info("Carregando chave: {}", "/certificados/rsa-private.pem");

        return ChaveAcesso.class.getResourceAsStream("/certificados/rsa-private.pem");

    }

    public InputStream getChavePublica() {

        LOGGER.info("Carregando chave: {}", "/certificados/rsa-public.pem");
        InputStream stream = ChaveAcesso.class.getResourceAsStream("/certificados/rsa-public.pem");
        return stream;

    }

}
