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
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
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
import javax.net.ssl.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class RESService extends BaseService {

    private static final long serialVersionUID = -8961797164029557197L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RESService.class);

    private static final String TEMPLATE = "openEHR-EHR-COMPOSITION.ficha_clinica_ocupacional.v1.0.0";

    @Inject
    private ParametroService service;

    @Inject
    private TrabalhadorDAO trabalhadorDAO;

    @Inject
    private ResDAO resDao;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarHistoricoParaInformacaoSaude(InformacaoSaude informacao, MultivaluedMap<String, String> filtros, ClienteAuditoria clienteAuditoria) {
        if (filtros == null) {
            throw new BusinessException("Sem parâmetros de busca informados");
        }

        if (informacao == null) {
            throw new BusinessException("Sem informacao a ser buscado");
        }

        String cpf = filtros.getFirst("cpf") != null ? filtros.getFirst("cpf") : "";
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando histórico de " + informacao.name() + " do trabalhador " + cpf);

        return this.buscarValorParaInformacaoSaude(cpf, informacao, true);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarUltimoRegistroInformacaoSaude(String cpf, InformacaoSaude informacao, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando último registro de " + informacao != null ? informacao.name() : null + " do trabalhador " + cpf);
        return this.buscarValorParaInformacaoSaude(cpf, informacao, false);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarInformacaoSaudeTrabalhadorUltimoEncontro(String cpf, InformacaoSaude informacao, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando último registro de " + informacao != null ? informacao.name() : null + " do trabalhador " + cpf);
        return this.buscarValorParaInformacaoSaudeUltimoEncontro(cpf, informacao);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarFichaMedica(String id, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando registro médico com id " + id);

        Retrofit retro = getRetroFit();
        Map<String, Object> resultado = null;
        try {
            String token = this.autenticar();
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
                    LOGGER.debug("Não foi possível obter o encontro {} {}", id, response == null ? null : response.errorBody().toString());
                }

            }
            return resultado;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return resultado;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, Object> buscarFichasMedicasParaPaciente(String cpf, String de, String pagina, ClienteAuditoria clienteAuditoria, Usuario usuarioLogado) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando histórico do trabalhador " + cpf);

        Retrofit retro = getRetroFit();
        Map<String, String> query = MapBuilder
                .criar("quantity", "5")
                .incluir("page", pagina)
                .incluir("fromDate", de)
                .incluir(" archetypeId", TEMPLATE)
                .incluir("document", cpf)
                .build();
        try {
            Response<Object> response = retro.create(RESApi.class)
                    .buscarHistoricoAtendimento(this.autenticar(), query)
                    .execute();

            if(response.isSuccessful()) {
                if(response.body() != null) {
                    return new ImmutableMap.Builder<String, Object>()
                            .put("filtrarInformacoes", cpf.equalsIgnoreCase(usuarioLogado.getLogin()))
                            .put("resultado", response.body())
                            .build();
                }else {
                    LOGGER.debug("Nenhum atendimento encontrado para o trabalhador {}", cpf);
                }
            }else {
                LOGGER.debug("Não foi possível buscar o historico de atendimento {} {} ", response.code(), response.body());
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
                    .buscarForm(this.autenticar(), identificador)
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
        LOGGER.debug("Autenticando o token no servico {} ", this.service.getRESUrl());
        String token = updateToken();
        return token;
    }

    private String updateToken() {
        Retrofit retro = getRetroFit();
        RESApi api = retro.create(RESApi.class);
        String token = null;
        try {
            Response<Map<String, Object>> response = api.autenticarOrganizacao(getParams()).execute();
            if (response.isSuccessful()) {
                token = refreshToken(response);
            } else if (response.code() == 401) {
                LOGGER.debug("Usuario não autorizado, tentando novamente", response.errorBody().string());
                token = refreshToken(api.autenticarOrganizacao(getParams()).execute());
            } else {
                LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}", response.errorBody().string());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}", e.toString());
        }
        return token;
    }

    private Map<String, String> getParams() {
        Map<String, String> dadosClienteRes = this.service.getDadoClienteRES();
        Map<String, String> params = MapBuilder.criar("grant_type", "client_credentials")
                .incluir("client_id", dadosClienteRes.get(ParametroService.RES_API_KEY))
                .incluir("client_secret", dadosClienteRes.get(ParametroService.RES_API_SECRET))
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
            LOGGER.debug("[FALHA GRAVE] Não foi possível criar o Retrofit para o sistema RES {}", this.service.getRESUrl());
        }
        return retro;
    }

    //Issue relacionada ao retrofit, solução proposta no link: https://github.com/square/retrofit/issues/1554
    public class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return (Converter<ResponseBody, Object>) body -> {
                if (body.contentLength() == 0) return null;
                return delegate.convert(body);
            };
        }
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
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) throws CertificateException {
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
    public Object buscarPaciente(String cpf, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando dados do trabalhador " + cpf);
        Trabalhador t = trabalhadorDAO.pesquisarPorCpf(cpf);
        if (t != null) {
            return ImmutableMap.of("name", t.getNome(), "cpf", t.getCpf(), "birthDate", t.getDataNascimento(), "gender", t.getGenero().getCodigo());
        } else {
            return ImmutableMap.of();
        }
    }

    private void reautenticar() {
        updateToken();
    }


    private Map<String, Object> buscarValorParaInformacaoSaude(String cpf, InformacaoSaude informacao, boolean historico) {
        LOGGER.debug("Buscando valor para infomacoes de saude no sistema RES {}", cpf);
        Retrofit retro = getRetroFit();
        Map<String, String> params = ImmutableMap.of("amPath", informacao.getPathDado()
                ,"documentType","cpf"
                ,"document", cpf
                ,"archetypeId",informacao.getIdArquetipo());
        Call<Map<String, Object>> chamada;

        if (!historico) {
            chamada = retro.create(RESApi.class).buscarInformacaoSaude(autenticar(), params);
        } else {
            chamada = retro.create(RESApi.class).buscarHistoricoParaInformacaoSaude(autenticar(), params);
        }

        try {
            Response<Map<String, Object>> response = chamada.execute();
            Map<String, Object> resultado = new HashMap<>();
            if (response != null && response.isSuccessful()) {
                resultado = response.body();
            } else {
                LOGGER.debug(response != null ? response.errorBody().string() : "null");
            }

            return resultado;
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return ImmutableMap.of();
        }
    }

    private Map<String, Object> buscarValorParaInformacaoSaudeUltimoEncontro(String cpf, InformacaoSaude informacao) {
        LOGGER.debug("Buscando valor para infomacoes de saude do ultimo encontro no sistema RES {}", cpf);

        Map<String, String> params = ImmutableMap.of("amPath", informacao.getPathDado(),"documentType","cpf","document", cpf,
                                                     "archetypeId",informacao.getIdArquetipo());

        try {
            Response<Map<String, Object>> response = this.getRetroFit().create(RESApi.class).buscarInformacaoSaudeDoUltimoEncontro(this.autenticar(), params).execute();

            if (response != null && response.isSuccessful()) {
                return response.body();
            } else {
                LOGGER.debug(response != null ? response.errorBody().string() : "null");
                return ImmutableMap.of();
            }
        } catch (Exception e) {
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

        @GET("v2/compositions")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarInformacaoSaude(@Header("Authorization") String authorization, @QueryMap(encoded = true) Map<String, String> params);

        @GET("v2/compositions/history")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarHistoricoParaInformacaoSaude(@Header("Authorization") String authorization, @QueryMap(encoded = true) Map<String, String> params);

        @GET("v2/compositions/fromLastEncounter")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarInformacaoSaudeDoUltimoEncontro(@Header("Authorization") String authorization, @QueryMap(encoded = true) Map<String, String> params);

        @GET("v1/encounters")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarHistoricoAtendimento(@Header("Authorization") String authorization, @QueryMap(encoded = true) Map<String, String> params);

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

}
