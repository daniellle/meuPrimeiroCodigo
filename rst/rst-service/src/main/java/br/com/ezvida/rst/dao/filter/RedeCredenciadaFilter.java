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

	public String getSituacao() {
		return situacao;
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

	public Long getSegmento() {
		return segmento;
	}
}
