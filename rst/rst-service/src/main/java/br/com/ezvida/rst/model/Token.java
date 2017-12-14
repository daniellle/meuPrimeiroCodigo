package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fw.core.model.BaseEntity;

//@formatter:off
@Entity
@JsonIgnoreProperties(value = { "token_acesso", "usuario" })
//@formatter:on
public class Token extends BaseEntity<String> {

    private static final long serialVersionUID = -3448451727031586208L;

	public static final int TEMPO_PADRAO_EXPIRACAO_REFRESH_TOKEN = 86400;

    @Id
    @Expose
    @SerializedName("access_token")
    private String id;

    @Expose
    @SerializedName("expires_in")
    private Long expiraEm;

    private Long tempoExpiracaoToken;

    private Long tempoExpiracaoAtualizacaoToken;

    @Expose
    @SerializedName("token_type")
    private String tipoToken;

    @Expose
    @SerializedName("refresh_token")
    private String tokenAtualizacao;

    @JsonIgnore
	private Usuario usuario;

    public Token() {
        // Construtor padrÃ£o
    }

	public Token(Usuario usuario, String tokenAcesso, Long expiraEm, Long tempoExpiracaoAtualizacaoToken,
			String tipoToken, String tokenAtualizacao) {
        this.usuario = usuario;
        this.id = tokenAcesso;
        this.expiraEm = expiraEm;
        this.tipoToken = tipoToken;
        this.tokenAtualizacao = tokenAtualizacao;
        this.tempoExpiracaoToken = DateUtils.addSeconds(new Date(), expiraEm.intValue()).getTime();
        this.tempoExpiracaoAtualizacaoToken = DateUtils.addSeconds(new Date(), tempoExpiracaoAtualizacaoToken.intValue()).getTime();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getTokenAcesso() {
        return getId();
    }

    public Long getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Long expiraEm) {
        this.expiraEm = expiraEm;
    }

    public Long getTempoExpiracaoToken() {
        return tempoExpiracaoToken;
    }

    public void setTempoExpiracaoToken(Long tempoExpiracaoToken) {
        this.tempoExpiracaoToken = tempoExpiracaoToken;
    }

    public Long getTempoExpiracaoAtualizacaoToken() {
        return tempoExpiracaoAtualizacaoToken;
    }

    public void setTempoExpiracaoAtualizacaoToken(Long tempoExpiracaoAtualizacaoToken) {
        this.tempoExpiracaoAtualizacaoToken = tempoExpiracaoAtualizacaoToken;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public String getTokenAtualizacao() {
        return tokenAtualizacao;
    }

    public void setTokenAtualizacao(String tokenAtualizacao) {
        this.tokenAtualizacao = tokenAtualizacao;
    }

	public Usuario getUsuario() {
        return usuario;
    }

	public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}