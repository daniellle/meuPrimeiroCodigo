package br.com.ezvida.rst.auditoria.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mdc implements Serializable {
	
	private static final long serialVersionUID = 7609414939153268699L;

	private String funcionalidade;
	
	private String navegador;
	
	@JsonProperty("tipo_operacao")
	private String tipoOperacao;
	
	private String usuario;

	public String getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(String funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public String getNavegador() {
		return navegador;
	}

	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	
}
