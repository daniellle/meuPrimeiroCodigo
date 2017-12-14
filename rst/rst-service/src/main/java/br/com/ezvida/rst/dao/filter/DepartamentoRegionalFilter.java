package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class DepartamentoRegionalFilter extends FilterBase {

	@QueryParam("id")
	private Long id;
	
	@QueryParam("cnpj")
	private String cnpj;

	@QueryParam("razaoSocial")
	private String razaoSocial;

	@QueryParam("idEstado")
	private Long idEstado;

	@QueryParam("statusDepartamento")
	private String situacao;
	
	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;
	
	@QueryParam("ativos")
	private boolean ativos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

	public void setAplicarDadosFilter(boolean aplicarDadosFilter) {
		this.aplicarDadosFilter = aplicarDadosFilter;
	}

	public boolean isAtivos() {
		return ativos;
	}

	public void setAtivos(boolean ativos) {
		this.ativos = ativos;
	}
}
