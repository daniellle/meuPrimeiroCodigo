package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class QuestionarioFilter extends FilterBase {
	
	@QueryParam("nome")
	private String nome;
	
	@QueryParam("situacao")
	private String situacao;
	
	@QueryParam("versao")
	private String versao;
	
	@QueryParam("tipo")
	private String tipo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
