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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public String getSituacao() {
		return situacao;
	}

	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

}
