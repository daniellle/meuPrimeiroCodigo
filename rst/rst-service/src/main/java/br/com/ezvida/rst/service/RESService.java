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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
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
import java.io.IOException;
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
    public Object buscarHistoricoParaInformacaoSaude(InformacaoSaude informacao, MultivaluedMap<String, String> filtros,
                                                     ClienteAuditoria clienteAuditoria) {

        if (filtros == null) {
            throw new BusinessException("Sem parâmetros de busca informados");
        }
        if (informacao == null) {
            throw new BusinessException("Sem informacao a ser buscado");
        }
        String cpf = filtros.getFirst("cpf") != null ? filtros.getFirst("cpf") : "";
        LogAuditoria.registrar(LOGGER, clienteAuditoria,
                               "Buscando histórico de " + informacao.name() + " do trabalhador " + cpf);
        Map<String, Object> paciente = this.buscarDadosPacienteRES(cpf);
        if (paciente != null) {
            Map<String, Object> historico = this.buscarValorParaInformacaoSaude(paciente.get("originalReferenceId")

                                                                                        .toString(), informacao, true);
            return historico;
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarUltimoRegistroInformacaoSaude(String cpf, InformacaoSaude informacao,
                                                      ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria,
                               "Buscando último registro de " + informacao != null ? informacao.name() : null + " do trabalhador " + cpf);
        Map<String, Object> paciente = this.buscarDadosPacienteRES(cpf);
        if (paciente != null) {
            Map<String, Object> cluster = this.buscarValorParaInformacaoSaude(paciente.get("originalReferenceId")
                                                                                      .toString(), informacao, false);
            if (cluster != null && !cluster.isEmpty()) {
                return cluster;
            }
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object buscarFichaMedica(String id, ClienteAuditoria clienteAuditoria) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando registro médico com id " + id);

        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl())
                                               .client(getOkHttpClient())
                                               .addConverterFactory(GsonConverterFactory.create())
                                               .build();
        try {
            String token = this.autenticar();
            Map<String, Object> resultado = null;
            if (token != null) {
                Response<Object> r = retro.create(RESApi.class)
                                          .buscarEncontroMedico(token, id)
                                          .execute();
                if (r != null) {
                    if (r.isSuccessful()) {
                        Object form = buscarForm(TEMPLATE);
                        resultado = new ImmutableMap.Builder<String, Object>()
                                .put("resultado", r.body())
                                .put("form", form)
                                .build();
                    } else {
                        LOGGER.debug("Não foi possível obter o encontro {} {}", id, r.errorBody());
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
    public Object buscarForm(String identificador) {
        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl())
                                               .client(getOkHttpClient())
                                               .addConverterFactory(GsonConverterFactory.create())
                                               .build();
        try {
            Object resultado = retro.create(RESApi.class)
                                    .buscarForm(this.autenticar(), identificador)
                                    .execute()
                                    .body();

            return resultado;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, Object> buscarFichasMedicasParaPaciente(String cpf, String de, String pagina,
                                                               ClienteAuditoria clienteAuditoria,
                                                               Usuario usuarioLogado) {
        LogAuditoria.registrar(LOGGER, clienteAuditoria, "Buscando histórico do trabalhador " + cpf);

        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl())
                                               .client(getOkHttpClient())
                                               .addConverterFactory(GsonConverterFactory.create())
                                               .build();
//        Map<String, Object> paciente = this.buscarDadosPacienteRES(cpf);
        Map<String, String> query = MapBuilder
                .criar("quantity", "5")
                .incluir("page", pagina)
                .incluir("fromDate", de)
                .incluir(" archetypeId", TEMPLATE)
                .incluir("document", cpf)
                .build();
        try {
            Object resultado = retro.create(RESApi.class)
                                    .buscarHistoricoAtendimento(this.autenticar()
//                                                , String.valueOf(paciente.get("originalReferenceId"))
//                                                , TEMPLATE
                                            , query)
                                    .execute()
                                    .body();
            return new ImmutableMap.Builder<String, Object>()
                    .put("filtrarInformacoes", cpf.equalsIgnoreCase(usuarioLogado.getLogin()))
                    .put("resultado", resultado)
                    .build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    private String autenticar() {
        String token = this.resDao.getToken();
        if (token == null) {
            updateToken();
        }

        return token;
    }

    private void updateToken() {

        Retrofit retro = new Retrofit.Builder().baseUrl(service.getRESUrl())
                                               .client(getOkHttpClient())
                                               .addConverterFactory(GsonConverterFactory.create())
                                               .build();

        RESApi api = retro.create(RESApi.class);
        Map<String, String> dadosClienteRes = this.service.getDadoClienteRES();
        Map<String, String> params = MapBuilder.criar("grant_type", "client_credentials")
                                               .incluir("client_id",
                                                        dadosClienteRes.get(ParametroService.RES_API_KEY))
                                               .incluir("client_secret",
                                                        dadosClienteRes.get(ParametroService.RES_API_SECRET))
                                               .build();
        String token = null;
        try {
            Response<Map<String, Object>> response = api.autenticarOrganizacao(params)
                                                        .execute();
            if (response.isSuccessful()) {
                Map<String, Object> tokenJSON = response.body();
                token = tokenJSON.get("token_type") + " " + tokenJSON.get("access_token")
                                                                     .toString();
                String refresh = String.valueOf(tokenJSON.get("refresh_token"));
                this.resDao.updateToken(token);
                this.resDao.updateRefresh(refresh);
            } else {
                LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}", response.errorBody()
                                                                                                    .string());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug("[FALHA GRAVE] Não foi possível autenticar no sistema RES {}", e.toString());
        }
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
            return ImmutableMap.of("name", t.getNome(), "cpf", t.getCpf(), "birthDate", t.getDataNascimento());
        } else {
            return ImmutableMap.of();
        }
    }

    private void reautenticar() {
        updateToken();
    }

    private Map<String, Object> buscarDadosPacienteRES(String cpf) {
        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl())
                                               .client(getOkHttpClient())
                                               .addConverterFactory(GsonConverterFactory.create())
                                               .build();
        try {
            Map<String, Object> resultado = retro.create(RESApi.class)
                                                 .buscarPaciente(this.autenticar(), cpf)
                                                 .execute()
                                                 .body();
            return resultado;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }


    private Map<String, Object> buscarValorParaInformacaoSaude(String referenciaOriginalPaciente,
                                                               InformacaoSaude informacao,
                                                               boolean historico) {

        Retrofit retro = new Retrofit.Builder().baseUrl(this.service.getRESUrl())
                                               .client(getOkHttpClient())
                                               .addConverterFactory(GsonConverterFactory.create())
                                               .build();
        Map<String, String> params = ImmutableMap.of("amPath", informacao.getPathDado());
        Call<Map<String, Object>> chamada = null;

        if (!historico) {
            chamada = retro.create(RESApi.class)
                           .buscarInformacaoSaude(autenticar(), informacao.getIdArquetipo(), referenciaOriginalPaciente,
                                                  params);
        } else {
            chamada = retro.create(RESApi.class)
                           .buscarHistoricoParaInformacaoSaude(autenticar(), informacao.getIdArquetipo(),
                                                               referenciaOriginalPaciente, params);
        }

        try {
            Response<Map<String, Object>> response = chamada.execute();
            Map<String, Object> resultado = new HashMap<>();
            if (response != null && response.isSuccessful()) {
                resultado = response.body();
            } else {
                LOGGER.debug(response != null ? response.errorBody()
                                                        .string() : "null");
            }

            return resultado;

        } catch (IOException e) {
            LOGGER.debug(e.toString());
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

        @GET("compositions/{archetypeId}/{patientOriginalReferenceId}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarInformacaoSaude(@Header("Authorization") String authorization
                , @Path("archetypeId") String archetypeId
                , @Path("patientOriginalReferenceId") String originalReferenceId
                , @QueryMap(encoded = true) Map<String, String> params);

        @GET("compositions/history/{archetypeId}/{patientOriginalReferenceId}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarHistoricoParaInformacaoSaude(@Header("Authorization") String authorization
                , @Path("archetypeId") String archetypeId
                , @Path("patientOriginalReferenceId") String originalReferenceId
                , @QueryMap(encoded = true) Map<String, String> params);

        @GET("encounters")///{patientOriginalReferenceId}/{archetypeId}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarHistoricoAtendimento(@Header("Authorization") String authorization,
//                                                @Path("patientOriginalReferenceId") String originalReferenceId,
//                                                @Path("archetypeId") String archetypeId,
                                                @QueryMap(encoded = true) Map<String, String> params);

        @GET("encounters/{id}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarEncontroMedico(@Header("Authorization") String authorization, @Path("id") String idEncontro);

        @GET("templates/{id}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Object> buscarForm(@Header("Authorization") String authorization, @Path("id") String identificador);

        @GET("demographics/patients/document/{document}")
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        Call<Map<String, Object>> buscarPaciente(@Header("Authorization") String authorization,
                                                 @Path("document") String document);

        @FormUrlEncoded
        @POST("oauth/token")
        Call<Map<String, Object>> autenticarOrganizacao(@FieldMap Map<String, String> params);
    }

}
