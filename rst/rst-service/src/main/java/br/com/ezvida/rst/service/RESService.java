package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.ResDAO;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.enums.InformacaoSaude;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.utils.MapBuilder;
import com.google.common.collect.ImmutableMap;
import fw.core.exception.BusinessErrorException;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class RESService extends BaseService {


    private static final long serialVersionUID = -8961797164029557197L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RESService.class);

    private static final String TEMPLATE = "openEHR-EHR-COMPOSITION.ficha_clinica_ocupacional.v1.0.0";

    private String token;

    @Inject
    private ParametroService service;

    @Inject
    private TrabalhadorDAO trabalhadorDAO;

    @Inject
    private ResDAO resDao;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarHistoricoParaInformacaoSaude(List<InformacaoSaude> informacoes,
                                                     MultivaluedMap<String, String> filtros,
                                                     ClienteAuditoria clienteAuditoria) {
        if (filtros == null) {
            throw new BusinessException("Sem parâmetros de busca informados");
        }

        if (informacoes == null) {
            throw new BusinessException("Sem informacao a ser buscado");
        }

        String cpf = filtros.getFirst("cpf") != null ? filtros.getFirst("cpf") : "";
        LogAuditoria.registrar(LOGGER,
                clienteAuditoria,
                "Buscando último registro de [" + Arrays.toString(informacoes.stream()
                        .map(InformacaoSaude::getNome)
                        .toArray()) + "] do trabalhador " + cpf);

        return this.buscarValorParaInformacaoSaude(cpf, informacoes, true);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarUltimoRegistroInformacaoSaude(String cpf,
                                                      List<InformacaoSaude> informacoes,
                                                      ClienteAuditoria clienteAuditoria) {
        if (StringUtils.isBlank(cpf)) {
            throw new BusinessErrorException("CPF não informado");
        }
        if (informacoes == null || informacoes.isEmpty()) {
            throw new BusinessErrorException("Informações de saúde não informadas");
        }
        if (!informacoes.stream()
                        .map(InformacaoSaude::getIdArquetipo)
                        .allMatch(informacoes.get(0).getIdArquetipo()::equals)) {
            throw new BusinessException("As informações de saúde precisam estar no mesmo arquéitipo");
        }
        if (clienteAuditoria == null) {
            throw new BusinessErrorException("Objeto de auditoria não informado");
        }
        LogAuditoria.registrar(LOGGER,
                clienteAuditoria,
                "Buscando último registro de [" + Arrays.toString(informacoes.stream()
                                                                             .map(InformacaoSaude::getNome)
                                                                             .toArray()) + "] do trabalhador " + cpf);
        return this.buscarValorParaInformacaoSaude(cpf, informacoes, false);
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarFichaMedica(String id, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando registro médico com id " + id);

        Retrofit retro = getRetroFit();
        Map<String, Object> resultado = null;
        try {
            token = autenticar();
            if (token != null) {
                Response<Object> response = retro.create(RESApi.class)
                                                 .buscarEncontroMedico(token, id)
                                                 .execute();
                if (response != null || response.isSuccessful()) {
                    Object form = buscarForm(TEMPLATE);
                    resultado = new ImmutableMap.Builder<String, Object>()
                            .put("resultado", response.body())
                            .put("form", form)
                            .build();
                } else {
                    LOGGER.debug("Não foi possível obter o encontro {} {}",
                            id,
                            response == null ? null : response.errorBody().toString());
                }

            }
            return resultado;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return resultado;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, Object> buscarFichasMedicasParaPaciente(String cpf,
                                                               String de,
                                                               String pagina,
                                                               ClienteAuditoria clienteAuditoria,
                                                               Usuario usuarioLogado) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando histórico do trabalhador " + cpf);

        Retrofit retro = getRetroFit();
        Map<String, String> query = MapBuilder
                .criar("quantity", "5")
                .incluir("page", pagina)
                .incluir("fromDate", de)
             //   .incluir(" archetypeId", TEMPLATE)
                .incluir("document", cpf)
                .build();
        try {
            Response<Object> response = retro.create(RESApi.class)
                                             .buscarHistoricoAtendimento(autenticar(), query)
                                             .execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    return new ImmutableMap.Builder<String, Object>()
                            .put("filtrarInformacoes", cpf.equalsIgnoreCase(usuarioLogado.getLogin()))
                            .put("resultado", response.body())
                            .build();
                } else {
                    LOGGER.debug("Nenhum atendimento encontrado para o trabalhador {}", cpf);
                }
            } else {
                LOGGER.debug("Não foi possível buscar o historico de atendimento {} {} ",
                        response.code(),
                        response.body());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarForm(String identificador) {
        Retrofit retro = getRetroFit();
        Object resultado = null;
        try {
            resultado = retro.create(RESApi.class)
                             .buscarForm(autenticar(), identificador)
                             .execute()
                             .body();
            if (resultado != null) {
                return resultado;
            }
            LOGGER.debug("Não foi possível buscar o form {} ", resultado);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return resultado;
    }

    private String autenticar() {
        if (token == null) {
            LOGGER.debug("Gerando nova token devido a 401");
            token = this.updateToken();
        }
        return token;
    }

    private String updateToken() {
        Retrofit retro = getRetroFit();
        RESApi api = retro.create(RESApi.class);
        String tokenUpdate = null;
        try {
            Response<Map<String, Object>> response = api.autenticarOrganizacao(getParams()).execute();
            if (response.isSuccessful()) {
                tokenUpdate = refreshToken(response);
            } else if (response.code() == 401) {
                LOGGER.debug("Usuario não autorizado, tentando novamente", response.errorBody().string());
                tokenUpdate = refreshToken(api.autenticarOrganizacao(getParams()).execute());
            } else {
                LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}",
                        response.errorBody().string());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}", e.toString());
        }
        return tokenUpdate;
    }

    private Map<String, String> getParams() {
        Map<String, String> dadosClienteRes = this.service.getDadoClienteRES();
        Map<String, String> params = MapBuilder.criar("grant_type", "client_credentials")
                                               .incluir("client_id", dadosClienteRes.get(ParametroService.RES_API_KEY))
                                               .incluir("client_secret",
                                                       dadosClienteRes.get(ParametroService.RES_API_SECRET))
                                               .build();
        return params;
    }

    private Retrofit getRetroFit() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl(this.service.getRESUrl())
                .client(getOkHttpClient())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (retro == null) {
            LOGGER.debug("[FALHA GRAVE] Não foi possível criar o Retrofit para o sistema RES {}",
                    this.service.getRESUrl());
        }
        return retro;
    }

    private String refreshToken(Response<Map<String, Object>> response) {
        Map<String, Object> tokenJSON = response.body();
        String token = tokenJSON.get("token_type") + " " + tokenJSON.get("access_token").toString();
        String refresh = String.valueOf(tokenJSON.get("refresh_token"));
        this.resDao.updateToken(token);
        this.resDao.updateRefresh(refresh);
        return token;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public OkHttpClient getOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new UnauthorizedInterceptor());
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarPaciente(String cpf, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando dados do trabalhador " + cpf);
        Trabalhador t = trabalhadorDAO.pesquisarPorCpf(cpf);
        if (t != null) {
            return ImmutableMap.of("name",
                    t.getNome(),
                    "cpf",
                    t.getCpf(),
                    "birthDate",
                    t.getDataNascimento(),
                    "gender",
                    t.getGenero().getCodigo());
        } else {
            return ImmutableMap.of();
        }
    }

    private Map<String, Object> buscarValorParaInformacaoSaude(String cpf,
                                                               List<InformacaoSaude> informacoes,
                                                               boolean historico) {
        LOGGER.debug("Buscando valor para infomacoes de saude no sistema RES {}", cpf);
        Retrofit retro = getRetroFit();
        Map<String, String> params = new HashMap<>();
        params.put("documentType", "cpf");
        params.put("document", cpf);
        params.put("archetypeId", informacoes.get(0).getIdArquetipo());
        params.put("from", LocalDate.now().minus(200, ChronoUnit.YEARS).toString());
        Call<Map<String, Object>> chamada;

        if (!historico) {
            chamada = retro.create(RESApi.class)
                           .buscarInformacaoSaude(autenticar(),
                                   params,
                                   informacoes.stream().map(InformacaoSaude::getAmPath).collect(Collectors.toList()));
        } else {
            chamada = retro.create(RESApi.class).buscarHistoricoParaInformacaoSaude(autenticar(), params, informacoes.stream().map(InformacaoSaude::getAmPath).collect(Collectors.toList()));
        }
        try {
            Response<Map<String, Object>> response = chamada.execute();
            Map<String, Object> resultado = new HashMap<>();
            if (response != null && response.isSuccessful()) {
                resultado = response.body();
            } else if (response != null && response.errorBody() != null) {
                LOGGER.debug(response.errorBody().string());
            }
            return resultado;
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return ImmutableMap.of();
        }
    }


    private interface RESApi {

        /**
         * Retorna um JSON no formato { max : Integer // número máximo de resultados da query, results: List<Map<String, Object>> // lista de dados
         * JSON de encontros médicos. }
         *
         * @param authorization
         * @param params
         * @return
         */

        @GET("v2/compositions/")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarInformacaoSaude(@Header("Authorization") String authorization,
                                                        @QueryMap(encoded = true) Map<String, String> params,
                                                        @Query("amPath") List<String> paths);

        @GET("v2/compositions/history")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarHistoricoParaInformacaoSaude(@Header("Authorization") String authorization,
                                                                     @QueryMap(encoded = true) Map<String, String> params,  @Query("amPath") List<String> paths);

        @GET("v1/encounters")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarHistoricoAtendimento(@Header("Authorization") String authorization,
                                                @QueryMap(encoded = true) Map<String, String> params);

        @GET("v1/encounters/{id}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarEncontroMedico(@Header("Authorization") String authorization, @Path("id") String idEncontro);

        @GET("v1/templates/{id}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarForm(@Header("Authorization") String authorization, @Path("id") String identificador);

        @FormUrlEncoded
        @POST("v1/oauth/token")
        Call<Map<String, Object>> autenticarOrganizacao(@FieldMap Map<String, String> params);
    }

    //Issue relacionada ao retrofit, solução proposta no link: https://github.com/square/retrofit/issues/1554
    public class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                Annotation[] annotations,
                                                                Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return (Converter<ResponseBody, Object>) body -> {
                if (body.contentLength() == 0) {
                    return null;
                }
                return delegate.convert(body);
            };
        }
    }

    private class UnauthorizedInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            okhttp3.Response result = chain.proceed(original);
            if (result.code() == 401) {
                String token = updateToken();
                Request novaRequest = original.newBuilder()
                                              .header("Authorization", token)
                                              .build();
                return chain.proceed(novaRequest);
            }
            return result;
        }
    }
}
