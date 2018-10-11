package br.com.ezvida.rst.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.ezvida.rst.dao.ParametroDAO;
import br.com.ezvida.rst.model.Parametro;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;

@Stateless
public class ParametroService extends BaseService {

    public static final String URL_API_GIRST = "url_api_girst";
    public static final String USUARIO_API_GIRST = "usuario_api_girst";
    public static final String SENHA_API_GIRST = "senha_api_girst";
    public static final String MODO_DESENV = "modo_desenv";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String CODIGO_SISTEMA_CADASTRO = "codigo_sistema_cadastro";
    public static final String RES_API_SECRET = "res_api_secret";
    public static final String RES_API_KEY = "res_api_key";
    public static final String RES_TOKEN = "res_token";
    public static final String RES_REFRESH_TOKEN = "res_refresh_token";
    public static final String TERMO_USO = "termo_uso";
    public static final String IGEV = "igev";
    public static final String HOST_ELASTICSEARCH = "host_elasticsearch";
    public static final String MAIL_SMTP_HOST = "mail_smtp_host";
    public static final String MAIL_SMTP_PORT = "mail_smtp_port";
    public static final String MAIL_SMTP_AUTH = "mail_smtp_auth";
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail_smtp_starttls_enable";
    public static final String MAIL_SSL_TRUST = "mail_smtp_ssl_trust";
    public static final String EMAIL_REMETENTE_SESI = "email_remetente_sesi";
    public static final String EMAIL_NOME_REMETENTE_SESI = "email_nome_remetente_sesi";
    public static final String EMAIL_SENHA_SESI = "email_senha_sesi";
    public static final String EMAIL_USERNAME_SESI = "email_username_sesi";
    public static final String EMAIL_ASSUNTO_SESI = "email_assunto_sesi";
    public static final String TAMANHO_MAXIMO_UPLOAD_ARQUIVO = "tamanho_maximo_upload_arquivo";
    public static final String TOKEN_ACESSO_CLIENTE_RST = "token_acesso_cliente_rst";
    public static final String SOLICITACAO_EMAIL_SESI = "solicitacao_email_sesi";
    public static final String SOLICITACAO_EMAIL_SESI_CORPO_TEXTO = "solicitacao_email_sesi_corpo_texto";
    public static final String SOLICITACAO_TELEFONE_CENTRAL_RELACIONAMENTO = "telefone_central_relacionamento_sesi_viva_mais";
    private static final long serialVersionUID = -542751432948139228L;
    private static final String RES_URL = "res_url";
    //private static final String RES_URL = "http://localhost:8000";
    private static final String IMUNIZACAO_URL = "imunizacao_url";
    @Inject
    private ParametroDAO parametroDao;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvarParametro(String nome, String valor) {
        Parametro parametro = null;
        parametro = this.parametroDao.carregarPorNome(nome);
        if (parametro == null) {
            parametro = new Parametro();
        }

        parametro.setNome(nome);
        parametro.setValor(valor);
        try {
            this.parametroDao.salvar(parametro);
        } catch (Exception e) {
            throw new BusinessException("");
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String carregarPorNome(String nome) {

        Parametro parametro = parametroDao.carregarPorNome(nome);

        if (parametro == null) {
            throw new BusinessException(getMensagem("app_rst_naoencontrado", nome));
        }
        return parametro.getValor();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Parametro carregarParametroPorNome(String nome) {

        Parametro parametro = parametroDao.carregarPorNome(nome);

        if (parametro == null) {
            throw new BusinessException(getMensagem("app_rst_naoencontrado", nome));
        }
        return parametro;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getRESUrl() {

        return carregarPorNome(RES_URL);
        //return RES_URL;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getImunizacaoUrl(){
        return carregarPorNome(IMUNIZACAO_URL);
    }



    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, String> getDadoClienteRES() {
        Map<String, String> resultado = new HashMap<>();
        Parametro api = parametroDao.carregarPorNome(RES_API_KEY);
        if (api == null || StringUtils.isEmpty(api.getValor())) {
            throw new BusinessException(getMensagem("app_rst_naoencontrado", RES_API_KEY));
        }
        Parametro secret = parametroDao.carregarPorNome(RES_API_SECRET);
        if (secret == null || StringUtils.isEmpty(secret.getValor())) {
            throw new BusinessException(getMensagem("app_rst_naoencontrado", RES_API_KEY));
        }
        resultado.put(RES_API_KEY, api.getValor());
        resultado.put(RES_API_SECRET, secret.getValor());

        return resultado;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getResToken() {
        return carregarPorNome(RES_TOKEN);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getResRefreshToken() {
        return carregarPorNome(RES_REFRESH_TOKEN);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getUrlApiGirst() {
        return carregarPorNome(URL_API_GIRST);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getUsuarioApiGirst() {
        return carregarPorNome(USUARIO_API_GIRST);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getSenhaApiGirst() {
        return carregarPorNome(SENHA_API_GIRST);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getModoDesenv() {
        return carregarPorNome(MODO_DESENV);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getAccessToken() {
        return carregarPorNome(ACCESS_TOKEN);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getRefreshToken() {
        return carregarPorNome(REFRESH_TOKEN);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getTokenType() {
        return TOKEN_TYPE;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getCodigoSistemaCadastro() {
        return carregarPorNome(CODIGO_SISTEMA_CADASTRO);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Parametro getTermoUso() {
        return carregarParametroPorNome(TERMO_USO);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Parametro getIgev() {
        return carregarParametroPorNome(IGEV);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getTamanhoMaximoUpload() {
        return carregarPorNome(TAMANHO_MAXIMO_UPLOAD_ARQUIVO);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getTokenAcessoClienteRst() {
        return carregarPorNome(TOKEN_ACESSO_CLIENTE_RST);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Parametro getSolicitacaoTelefoneCentralRelacionamento() {
        return carregarParametroPorNome(SOLICITACAO_TELEFONE_CENTRAL_RELACIONAMENTO);
    }

}