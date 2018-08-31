package br.com.ezvida.rst.web.endpoint.v1;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.SindicatoDAO;
import br.com.ezvida.rst.service.SindicatoService;

public class SindicatoEndpointTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SindicatoEndpointTest.class);

    @InjectMocks
    private SindicatoEndpoint endpoint;

    @Spy
    @InjectMocks
    private SindicatoService service;

    @Mock
    private SindicatoDAO dao;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        LOGGER.debug("Service: {}", service);
        LOGGER.debug("Dao: {}", dao);
        LOGGER.debug("Response: {}", response);

//        configuracao = service.(Ambiente.desenvolvimento);

    }

    //@Test
    public void getSindicato() throws Exception {

//        HttpServletRequest request = mock(HttpServletRequest.class);
//
//        Response response = endpoint.getSindicato(request, configuracao.get("cliente").replaceAll("@[a-zA-z.]*", StringUtils.EMPTY));
//
//        Map<String, Object> sindicato = new Decoder<Map<String, Object>>() {
//
//            private static final long serialVersionUID = 2999613323241870291L;
//
//        }.decodificar(response.getEntity().toString(), TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class));
//
//        LOGGER.debug("Usuário encontrado: [\n{}\n]", sindicato);

    }

    //@Test
    public void getSindicatos() throws Exception {

//        HttpServletRequest request = mock(HttpServletRequest.class);
//
//        UriInfo uri = mock(UriInfo.class);
//
//        MultivaluedMap<String, String> filtros = new MultivaluedHashMap<>();
//
//        filtros.add("sAMAccountName", configuracao.get("cliente").replaceAll("@[a-zA-z.]*", StringUtils.EMPTY));
//
//        when(uri.getQueryParameters()).thenReturn(filtros);
//
//        Response response = endpoint.gets(request, null, uri);
//
//        List<Map<String, Object>> sindicatos = new Decoder<List<Map<String, Object>>>() {
//
//            private static final long serialVersionUID = 2999613323241870291L;
//
//        }.decodificar(response.getEntity().toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, Map.class));
//
//        LOGGER.debug("Usuários coletados: [\n{}\n]", sindicatos);

    }
}
