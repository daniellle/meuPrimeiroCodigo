package br.com.ezvida.rst.security;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fw.security.interceptor.ChaveSegurancaComRSA;

public class ChaveAcesso implements ChaveSegurancaComRSA {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChaveAcesso.class);

    public InputStream getChavePrivada() {

        LOGGER.info("Carregando chave: {}", "/certificados/rsa-private.pem");

        return ChaveAcesso.class.getResourceAsStream("/certificados/rsa-private.pem");

    }

    public InputStream getChavePublica() {

        LOGGER.info("Carregando chave: {}", "/certificados/rsa-public.pem");

        return ChaveAcesso.class.getResourceAsStream("/certificados/rsa-public.pem");

    }

}
