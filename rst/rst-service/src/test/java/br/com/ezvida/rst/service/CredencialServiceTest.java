package br.com.ezvida.rst.service;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;

import br.com.ezvida.rst.dao.UsuarioDAO;
import br.com.ezvida.rst.model.Token;
import fw.security.interceptor.ChaveSeguranca;
import fw.security.interceptor.ChaveSegurancaComRSA;
import fw.security.interceptor.TipoOAuth;

@RunWith(MockitoJUnitRunner.class)
public class CredencialServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredencialServiceTest.class);

    @InjectMocks
    private CredencialService credencialService;

    @Spy
    @InjectMocks
    private UsuarioServiceProd usuarioService;

    @Mock
    private UsuarioDAO usuarioDAO;

    @Before
    public void inicializar() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void gerar() throws Exception {

        LOGGER.info("Gerando chave: {}", ChaveSegurancaComRSA.getInstance().getChavePrivada());

        String token = ChaveSeguranca.getInstance()
                .gerar(JWT.create().withSubject(RandomStringUtils.random(8)).withClaim("nome", RandomStringUtils.random(8))
                        .withClaim("sobrenome", RandomStringUtils.random(8)).withClaim("email", RandomStringUtils.random(8).concat("@solutis.net.br"))
                        .withArrayClaim("papeis", new String[] { RandomStringUtils.random(8) })
                        .withArrayClaim("permissoes", new String[] { RandomStringUtils.random(8), RandomStringUtils.random(8) })
                        .withExpiresAt(DateUtils.addSeconds(new Date(), Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN)), TipoOAuth.ACCESS_TOKEN);

        LOGGER.debug("Token: {}", token);

    }

    @Test
    public void validar() throws Exception {

//        LOGGER.debug("UsuarioServiceProd {}", usuarioService);
//        LOGGER.debug("UsuarioDAO {}", usuarioDAO);
//
//        String token = ChaveSeguranca.getInstance()
//                .gerar(JWT.create().withSubject("ismael.queiroz").withClaim("nome", RandomStringUtils.random(8))
//                        .withClaim("sobrenome", RandomStringUtils.random(8)).withClaim("email", RandomStringUtils.random(8).concat("@solutis.net.br"))
//                        .withArrayClaim("papeis", new String[] { RandomStringUtils.random(8) })
//                        .withArrayClaim("permissoes", new String[] { RandomStringUtils.random(8), RandomStringUtils.random(8) })
//                        .withExpiresAt(DateUtils.addSeconds(new Date(), Token.TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN)), TipoOAuth.ACCESS_TOKEN);
//
//        Payload payload = OAuthUtils.validarToken(ChaveSegurancaComRSA.getInstance().getChavePublica(), token);
//
//        LOGGER.debug("Executando validação do payload, Token: {}", credencialService.validar(payload));

    }

    @Test
    public void revogar() throws Exception {
    }

}