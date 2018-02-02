package br.com.ezvida.rst.web.endpoint.v1;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.type.TypeFactory;

import br.com.ezvida.rst.dao.UsuarioDAO;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.service.UsuarioServiceProd;
import fw.core.common.encode.Decoder;

//@RunWith(MockitoJUnitRunner.class)
public class UsuarioEndpointTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioEndpointTest.class);

    @InjectMocks
    private UsuarioEndpoint endpoint;

    @Spy
    @InjectMocks
    private UsuarioServiceProd service;

    @Mock
    private UsuarioDAO dao;

    @Mock
    private HttpServletResponse response;

    private Map<String, String> configuracao;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        LOGGER.debug("Service: {}", service);
        LOGGER.debug("Dao: {}", dao);
        LOGGER.debug("Response: {}", response);

        configuracao = service.getConfiguracao(Ambiente.DESENVOLVIMENTO);

    }

    //@Test
    public void getUsuario() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);

        Response response = endpoint.getUsuario(request, configuracao.get("cliente").replaceAll("@[a-zA-z.]*", StringUtils.EMPTY));

        Map<String, Object> usuario = new Decoder<Map<String, Object>>() {

            private static final long serialVersionUID = 2999613323241870291L;

        }.decodificar(response.getEntity().toString(), TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class));

        LOGGER.debug("Usuário encontrado: [\n{}\n]", usuario);

    }

}
