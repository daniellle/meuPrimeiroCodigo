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

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public Long getIdDepRegional() {
		return idDepRegional;
	}

	public void setIdDepRegional(Long idDepRegional) {
		this.idDepRegional = idDepRegional;
	}

	public String getStatusCat() {
		return statusCat;
	}

	public void setStatusCat(String statusCat) {
		this.statusCat = statusCat;
	}
}
