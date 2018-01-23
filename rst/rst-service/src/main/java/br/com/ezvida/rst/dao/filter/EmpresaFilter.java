package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class EmpresaFilter extends FilterBase {

	@QueryParam("id")
	private Long id;

	@QueryParam("cnpj")
	private String cnpj;

	@QueryParam("razaoSocial")
	private String razaoSocial;

	@QueryParam("nomeFantasia")
	private String nomeFantasia;

	@QueryParam("situacao")
	private String situacao;

	@QueryParam("unidadeObra")
	private Long unidadeObra;

	@QueryParam("idUnidadeObra")
	private Long idUnidadeObra;
	
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

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public String getStatusCat() {
		return situacao;
	}

}
