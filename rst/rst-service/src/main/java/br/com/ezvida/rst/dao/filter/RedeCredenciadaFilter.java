package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class RedeCredenciadaFilter extends FilterBase {

	
	@QueryParam("id")
	private Long id;
	
	@QueryParam("cnpj")
	private String cnpj;

	@QueryParam("razaoSocial")
	private String razaoSocial;

	@QueryParam("segmento")
	private Long segmento;

	@QueryParam("situacao")
	private String situacao;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public Long getSegmento() {
		return segmento;
	}

	public void setSegmento(Long segmento) {
		this.segmento = segmento;
	}
}
