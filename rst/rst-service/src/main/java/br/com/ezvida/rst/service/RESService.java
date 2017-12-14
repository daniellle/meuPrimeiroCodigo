package br.com.ezvida.rst.service;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.util.UriEncoder;
import com.google.common.collect.ImmutableMap;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.ResDAO;
import br.com.ezvida.rst.enums.DadosArquetipo;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.utils.MapBuilder;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

@Stateless
public class RESService extends BaseService {

    private static final long serialVersionUID = -8961797164029557197L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RESService.class);
    
    @Inject
    private ParametroService service;
    @Inject
    private ResDAO resDao;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarHistoricoRegistrosParaDado(DadosArquetipo dado, MultivaluedMap<String, String> filtros, ClienteAuditoria clienteAuditoria) {

        if (filtros == null)
            throw new BusinessException("Sem parâmetros de busca informados");
        if (dado == null)
            throw new BusinessException("Sem dado a ser buscado");
        String cpf = filtros.getFirst("cpf") != null ? filtros.getFirst("cpf") : "";
        String de = filtros.getFirst("de") != null ? filtros.getFirst("de") : "";
        String ate = filtros.getFirst("ate") != null ? filtros.getFirst("ate") : "";
        String quantidade = filtros.getFirst("quantidade") != null ? filtros.getFirst("quantidade") : "";
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando histórico de " + dado.name() + " do trabalhador " + cpf);
        Map<String, Object> pagina = this.buscarValorParaDado(cpf, dado, de, ate, quantidade, Boolean.parseBoolean(filtros.getFirst("todos")));
        if (pagina != null && !pagina.isEmpty()) {
            return pagina;
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarUltimoRegistroParaDado(String cpf, DadosArquetipo dado, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria,
                "Buscando último registro de " + dado != null ? dado.name() : null + " do trabalhador " + cpf);
        Map<String, Object> pagina = this.buscarValorParaDado(cpf, dado);
        if (pagina != null && !pagina.isEmpty()) {
            return pagina;
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarEncontro(String id, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando registro médico com id " + id);

        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl()).client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build();
        try {
            String token = this.autenticar();
            Map<String, Object> resultado = null;
            if (token != null) {
                Response<Object> r = retro.create(RESApi.class).buscarEncontroMedico(token, id).execute();
                if (r != null) {
                    if (r.isSuccessful()) {
                        Object form = buscarForm("openEHR-EHR-COMPOSITION.ficha_clinica_ocupacional.v1.0.0");

                        resultado = new ImmutableMap.Builder<String, Object>()
                            .put("resultado", r.body())
                            .put("form", form)
                            .build();
                    } else {
                        LOGGER.debug("Não foi possível obter o encontro {} {}", id, r.errorBody().toString());
                    }
                } else {
                    LOGGER.debug("Não foi possível obter o encontro {} {}", id, null);
                }

            }
            return resultado;
        } catch (IOException e) {
        	LOGGER.error(e.getMessage());
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, Object> buscarHistoricoParaPaciente(String cpf, String de, String pagina, ClienteAuditoria clienteAuditoria, Usuario usuarioLogado) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando histórico do trabalhador " + cpf);

        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl()).client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build();
        Map<String, String> queryDado = MapBuilder.criar("document", "true").incluir("fromDate", de).incluir("quantity", "5")
                .incluir("ignore", "openEHR-EHR-COMPOSITION.ficha_industria_saudavel.v1.0.0").incluir("page", pagina).build();

        queryDado.put("document", cpf);
        try {
            return new ImmutableMap.Builder<String, Object>()
                    .put("filtrarInformacoes", cpf.equalsIgnoreCase(usuarioLogado.getLogin()))
                    .put("resultado", retro.create(RESApi.class).buscarHistoricoAtendimento(this.autenticar(), queryDado).execute().body())
                    .build();
        } catch (IOException e) {
        	LOGGER.error(e.getMessage());
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarDadosPaciente(String cpf, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando dados do trabalhador " + cpf);

        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl()).client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build();
        try {
            Object resultado = retro.create(RESApi.class).buscarPaciente(this.autenticar(), cpf).execute().body();

            return resultado;
        } catch (IOException e) {
        	LOGGER.error(e.getMessage());
            return null;
        }
    }

    private Map<String, Object> buscarValorParaDado(String cpf, DadosArquetipo dado) {
        return this.buscarValorParaDado(cpf, dado, null, null, null, false);
    }

    private Map<String, Object> buscarValorParaDado(String cpf, DadosArquetipo dado, String de, String ate, String quantidade, boolean todos) {
        LOGGER.debug("Obtendo valor para o dado {} de {} ate {}", dado != null ? dado.name() : "null", de != null ? de.toString() : "null",
                ate != null ? ate.toString() : "null");
        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl()).client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build();
        Map<String, String> queryDado = criarParametrosDeBusca(dado);

        if (de != null || quantidade != null) {
            queryDado.put("from", de);
            queryDado.put("to", ate);
            queryDado.put("page", "0");
            queryDado.put("quantity", quantidade);
        } else if (!todos) {
            queryDado.put("onlyLast", "true");
        }

        queryDado.put("patientDocument", cpf);
        try {
            Response<Map<String, Object>> response = retro.create(RESApi.class).buscarDadoBasico(this.autenticar(), queryDado)
                    .execute();
            Map<String, Object> resultado = new HashMap<>();
            if (response != null && response.isSuccessful()) {
                resultado = response.body();
            } else {
                LOGGER.debug(response != null ?  response.errorBody().string() : "null");
            }

            return resultado;

        } catch (IOException e) {
            LOGGER.debug(e.toString());
            return new HashMap<String, Object>();
        }
    }

    private Map<String, String> criarParametrosDeBusca(DadosArquetipo dado) {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("archetypeId", UriEncoder.encode("openEHR-EHR-COMPOSITION.ficha_clinica_ocupacional.v1.0.0"));
		if (dado != null) {
			queryMap.put("informationPath", UriEncoder.encode(dado.getPathDado()));
			queryMap.put("informationName", UriEncoder.encode(dado.getNome()));
			queryMap.put("infoType", dado.getTipoDado());
		}
		return queryMap;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public static OkHttpClient getOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarForm(String identificador) {
        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl()).client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build();
        try {
            Object resultado = retro.create(RESApi.class).buscarForm(this.autenticar(), identificador).execute().body();

            return resultado;
        } catch (IOException e) {
        	LOGGER.error(e.getMessage());
            return null;
        }
    }

    private String autenticar() {
        String token = this.resDao.getToken();
        if (token == null) {
            Retrofit retro = new Retrofit.Builder().baseUrl(service.getRESUrl())
                    .client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create()).build();
            
            RESApi api = retro.create(RESApi.class);
            Map<String, String> dadosClienteRes = this.service.getDadoClienteRES();
            Map<String, String> params = MapBuilder.criar("grant_type", "client_credentials")
                    .incluir("client_id", dadosClienteRes.get(ParametroService.RES_API_KEY))
                    .incluir("client_secret", dadosClienteRes.get(ParametroService.RES_API_SECRET)).build();
            try {
                Response<Map<String, Object>> response = api.autenticarOrganizacao(params).execute();
                if (response.isSuccessful()) {
                    Map<String, Object> tokenJSON = response.body();
                    token = tokenJSON.get("token_type") + " " + tokenJSON.get("access_token").toString();
                    String refresh = String.valueOf(tokenJSON.get("refresh_token"));
                    this.resDao.updateToken(token);
                    this.resDao.updateRefresh(refresh);
                } else {
                    LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}", response.errorBody().string());
                }
            } catch (IOException e) {
            	LOGGER.error(e.getMessage());
                LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}", e.toString());
            }
        }

        return token;
    }

    private interface RESApi {

        /**
         * Retorna um JSON no formato { max : Integer // número máximo de resultados da query, results: List<Map<String, Object>> // lista de dados
         * JSON de encontros médicos. }
         * 
         * @param authorization
         * @param arquetipo
         * @param params
         * @return
         */
        
        @GET("compositions/patient")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarDadoBasico(@Header("Authorization") String authorization, @QueryMap(encoded = true) Map<String, String> params);

        @GET("encounters")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarHistoricoAtendimento(@Header("Authorization") String authorization, @QueryMap(encoded = true) Map<String, String> params);

        @GET("encounters/{id}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarEncontroMedico(@Header("Authorization") String authorization, @Path("id") String idEncontro);
        
        @GET("medical/templates/{id}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarForm(@Header("Authorization") String authorization, @Path("id") String identificador);

        @GET("demographics/patients/document/{document}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarPaciente(@Header("Authorization") String authorization, @Path("document") String document);

        // @Headers("Content-Type: application/x-www-form-urlencoded")
        @FormUrlEncoded
        @POST("oauth/token")
        Call<Map<String, Object>> autenticarOrganizacao(@FieldMap Map<String, String> params);
    }

}
