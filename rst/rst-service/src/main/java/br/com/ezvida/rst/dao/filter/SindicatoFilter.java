package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class SindicatoFilter extends FilterBase {
	
	@QueryParam("cnpj")
	private String cnpj;

	@QueryParam("razaoSocial")
	private String razaoSocial;

	@QueryParam("nomeFantasia")
	private String nomeFantasia;

	@QueryParam("situacao")
	private String situacao;
	
	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;

	@QueryParam("ids")
	private String ids;

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

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

	public void setAplicarDadosFilter(boolean aplicarDadosFilter) {
		this.aplicarDadosFilter = aplicarDadosFilter;
	}
}
