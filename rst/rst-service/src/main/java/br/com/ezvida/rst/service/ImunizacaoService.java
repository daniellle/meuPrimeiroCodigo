package br.com.ezvida.rst.service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.dao.filter.ImunizacaoFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Periodicidade;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.model.dto.DoseDTO;
import br.com.ezvida.rst.model.dto.VacinaAutodeclaradaDTO;
import br.com.ezvida.rst.model.pagination.Page;
import fw.core.service.BaseService;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Stateless
public class ImunizacaoService extends BaseService {

    private static final long serialVersionUID = -8961797164029557197L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImunizacaoService.class);

    @Inject
    private ParametroService parametroService;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<VacinaAutodeclaradaDTO> buscarHistoricoVacinasAutodeclaradas(String user, ImunizacaoFilter imunizacaoFilter) {

        Retrofit retrofit = getRetroFit();

		LOGGER.debug("Buscando histórico");
		ListaPaginada<VacinaAutodeclaradaDTO> resultado = new ListaPaginada<VacinaAutodeclaradaDTO>(0L, new ArrayList<VacinaAutodeclaradaDTO>());

        try {
            LocalDate dataVacinacaoEnd = null;
            LocalDate dataVacinacaoStart = null;

            Periodicidade periodicidade = Periodicidade.getPeriodicidade(imunizacaoFilter.getPeriodo());
            if (periodicidade != null) {
                dataVacinacaoEnd = LocalDate.now();
                dataVacinacaoStart = dataVacinacaoEnd.minusMonths(periodicidade.getQuantidadeMes());
            }

			LOGGER.debug("Criando response usuario = {}; filter = {}, dataVacinacaoStart = {}, dataVacinacaoEnd = {}", user,
					imunizacaoFilter.toString(), dataVacinacaoStart, dataVacinacaoEnd);

            Response<Page<VacinaAutodeclaradaDTO>> response = retrofit.create(ImunizacaoAPI.class)
                    .buscarHistorico(this.getToken(),user, imunizacaoFilter.getPagina(), imunizacaoFilter.getQuantidadeRegistro(), imunizacaoFilter.getNome(),
                            dataVacinacaoStart != null ? dataVacinacaoStart.toString() : null,
                            dataVacinacaoEnd != null ? dataVacinacaoEnd.toString() : null)
                    .execute();

            if (response == null || !response.isSuccessful()) {
                LOGGER.debug("Não foi possível obter as vacinas", response == null ? null : response.errorBody().toString());
            }


            /*Collections.sort(response.body().getContent(), new Comparator<VacinaAutodeclaradaDTO>() {
                @Override
                public int compare(VacinaAutodeclaradaDTO o1, VacinaAutodeclaradaDTO o2) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
                    return LocalDate.parse(o2.getDataVacinacao(), formatter).compareTo(LocalDate.parse(o1.getDataVacinacao(),formatter));
                }
            });*/
            Page<VacinaAutodeclaradaDTO> result = response.body();

			if (result != null) {
				Long totalElementos = result.getTotalElements() != null ? new Long(result.getTotalElements()) : null;
				resultado = new ListaPaginada<VacinaAutodeclaradaDTO>(totalElementos, result.getContent());
			}

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }


        return resultado;
    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DoseDTO> buscarVacinasProximaDosesMensal(String user, Integer month, Integer year) {

		Retrofit retrofit = getRetroFit();

		LOGGER.debug("Buscando vacinas e proximas doses do mes");
		List<DoseDTO> resultado = null;


		try {
			LOGGER.debug("Criando response usuario = {}; month = {}, year = {}", user, month, year);

			Response<List<DoseDTO>> response = retrofit.create(ImunizacaoAPI.class).buscarVacinasProximaDosesMensal(this.getToken(), user, month, year)
					.execute();

			if (response == null || !response.isSuccessful()) {
				LOGGER.debug("Não foi possível obter as vacinas", response == null ? null : response.errorBody().toString());
			}

			/*if (body == null) {
				resultado.put("proximas", new ArrayList<>());
				resultado.put("vacinas", new ArrayList<>());
			}*/

			return response.body();

		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		return resultado;
	}

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public VacinaAutodeclaradaDTO salvarVacinaAutodeclarada(VacinaAutodeclaradaDTO vacina, Usuario usuario) {

        Retrofit retrofit = getRetroFit();

        try {
            Response<VacinaAutodeclaradaDTO> response = retrofit.create(ImunizacaoAPI.class).salvar(this.getToken(), usuario.getLogin(), vacina).execute();

            if (response == null || !response.isSuccessful()) {
                LOGGER.debug("Não foi possível salvar a vacina {} {}", vacina, response == null ? null : response.errorBody().toString());
            }

            return response.body();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public VacinaAutodeclaradaDTO editarVacinasAutoDeclaradas(VacinaAutodeclaradaDTO vacina, Usuario usuario) {

        Retrofit retrofit = getRetroFit();

        try {
            Response<VacinaAutodeclaradaDTO> response = retrofit.create(ImunizacaoAPI.class).atualizar(this.getToken(), usuario.getLogin(), vacina).execute();
            if (response == null || !response.isSuccessful()) {
                LOGGER.debug("Não foi possível salvar a vacina {} {}", vacina, response == null ? null : response.errorBody().toString());
            }
            return response.body();

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public VacinaAutodeclaradaDTO removerVacinaAutodeclarada(int id, Usuario usuario) {

        Retrofit retrofit = getRetroFit();

        try {
            Response<VacinaAutodeclaradaDTO> response = retrofit.create(ImunizacaoAPI.class).remover(id, this.getToken(),  usuario.getLogin()).execute();
            if (response == null || !response.isSuccessful()) {
                LOGGER.debug("Não foi possível remover a vacina {} {}", id, response == null ? null : response.errorBody().toString());
            }
            return response.body();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DoseDTO> buscarProximasDosesMaisRecentes(Usuario usuario){

        Retrofit retrofit = getRetroFit();
        int limit = 5;

        try{
            Response<List<DoseDTO>> response = retrofit.create(ImunizacaoAPI.class).buscarUltimasProximasDoses(this.getToken(), usuario.getLogin(), limit).execute();
            if(response == null || !response.isSuccessful()){
                LOGGER.debug("Não foi possível procurar as proximas doses da vacina {} {}", usuario, response == null ? null : response.errorBody().toString());
            }
            return response.body();
        }catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public VacinaAutodeclaradaDTO buscarVacinaAutodeclaradaPorID(int id, Usuario usuario) {

        Retrofit retrofit = getRetroFit();

        try {
            Response<VacinaAutodeclaradaDTO> response = retrofit.create(ImunizacaoAPI.class).buscarVacinaAutodeclaradaPorID(id,this.getToken(), usuario.getLogin())
                    .execute();
            if (response == null || !response.isSuccessful()) {
                LOGGER.debug("Não foi possível obter a vacina {} {}", id, response == null ? null : response.errorBody().toString());
            }
            return response.body();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<VacinaAutodeclaradaDTO> buscarUltimasVacinas(int limite, String login) {
        Retrofit retrofit = getRetroFit();

        try {
            Response<List<VacinaAutodeclaradaDTO>> response = retrofit.create(ImunizacaoAPI.class)
                    .buscarUltimasVacinas(limite, this.getToken(), login)
                    .execute();
            if (response == null || !response.isSuccessful()) {
                LOGGER.debug("Não foi possível obter a lista de vacinas", response == null ? null : response.errorBody().toString());
            }
            return response.body();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    //Retrofit e HTTP Client

    private Retrofit getRetroFit() {
		LOGGER.debug("Criando cliente rest {}", this.parametroService.getImunizacaoUrl());
		Retrofit retro = new Retrofit.Builder().baseUrl(this.parametroService.getImunizacaoUrl()).client(getOkHttpClient())
                .addConverterFactory(new NullOnEmptyConverterFactory()).addConverterFactory(GsonConverterFactory.create()).build();
        if (retro == null) {
			LOGGER.error("[FALHA GRAVE] Não foi possível criar o Retrofit para o sistema Imunizacao {}", this.parametroService.getImunizacaoUrl());
        }
        return retro;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public OkHttpClient getOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
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
            // builder.addInterceptor(new AddHeaderInterceptor());
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


    // Issue relacionada ao retrofit, solução proposta no link: https://github.com/square/retrofit/issues/1554
    public class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return (Converter<ResponseBody, Object>) body -> {
                if (body.contentLength() == 0)
                    return null;
                return delegate.convert(body);
            };
        }
    }

    //Autenticação

    private String getToken(){
        String accessToken = this.parametroService.getAccessToken();
        String token = "Bearer " + accessToken;
        return token;
    }


    private interface ImunizacaoAPI {

//        public static final String PATH_EDGE = "vacinas/";
        public static final String PATH_EDGE = "";
        public static final String PATH_VACINAS_AUTODECLARADAS = PATH_EDGE + "vacinas-autodeclaradas";

        @GET(PATH_VACINAS_AUTODECLARADAS + "/{id}")
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Call<VacinaAutodeclaradaDTO> buscarVacinaAutodeclaradaPorID(@Path("id") int idVacina, @Header("Authorization") String token, @Header("X-Zuul-Principal") String user);

        @POST(PATH_VACINAS_AUTODECLARADAS)
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Call<VacinaAutodeclaradaDTO> salvar(@Header("Authorization") String token, @Header("X-Zuul-Principal") String user, @Body VacinaAutodeclaradaDTO vacina);

        @PUT(PATH_VACINAS_AUTODECLARADAS)
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Call<VacinaAutodeclaradaDTO> atualizar(@Header("Authorization") String token, @Header("X-Zuul-Principal") String user, @Body VacinaAutodeclaradaDTO vacina);

        @GET(PATH_VACINAS_AUTODECLARADAS + "/historico")
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Call<Page<VacinaAutodeclaradaDTO>> buscarHistorico(@Header("Authorization") String token, @Header("X-Zuul-Principal") String user, @Query("page") Integer page,
                                                           @Query("size") Integer size, @Query("nome") String nome, @Query("dataVacinacaoStart") String dataVacinacaoStart,
                                                           @Query("dataVacinacaoEnd") String dataVacinacaoEnd);

        @GET(PATH_VACINAS_AUTODECLARADAS + "/ultimas-vacinas/{limite}")
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Call<List<VacinaAutodeclaradaDTO>> buscarUltimasVacinas(@Path("limite") int limite, @Header("Authorization") String token, @Header("X-Zuul-Principal") String user);


        @DELETE(PATH_VACINAS_AUTODECLARADAS + "/{id}")
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Call<VacinaAutodeclaradaDTO> remover(@Path("id") int idVacina, @Header("Authorization") String token, @Header("X-Zuul-Principal") String user);

        @GET(PATH_VACINAS_AUTODECLARADAS + "/ultimas-proximas-doses/{filter}")
        @Headers({"Content-Type: application/json", "Accept: application/json"})
        Call <List<DoseDTO>> buscarUltimasProximasDoses(@Header("Authorization") String token, @Header("X-Zuul-Principal") String user, @Path("filter") int limit);

		@GET(PATH_VACINAS_AUTODECLARADAS + "/mensal/{month}/{year}")
		@Headers({ "Content-Type: application/json", "Accept: application/json" })
		Call<List<DoseDTO>> buscarVacinasProximaDosesMensal(@Header("Authorization") String token, @Header("X-Zuul-Principal") String user, @Path("month") Integer month,
				@Path("year") Integer year);

    }

}
