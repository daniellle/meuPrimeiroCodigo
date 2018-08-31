package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class UnidAtendTrabalhadorFilter extends FilterBase {

	@QueryParam("cnpj")
	private String cnpj;

	@QueryParam("razaoSocial")
	private String razaoSocial;

	@QueryParam("idDepRegional")
	private Long idDepRegional;

	@QueryParam("statusCat")
	private String statusCat;

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public Long getIdDepRegional() {
		return idDepRegional;
	}

	public String getStatusCat() {
		return statusCat;
	}
}
