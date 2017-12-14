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

	public String getStatusCat() {
		return situacao;
	}

	public void setStatusCat(String statusCat) {
		this.situacao = statusCat;
	}

	public Long getUnidadeObra() {
		return unidadeObra;
	}

	public void setUnidadeObra(Long unidadeObra) {
		this.unidadeObra = unidadeObra;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdUnidadeObra() {
		return idUnidadeObra;
	}

	public void setIdUnidadeObra(Long idUnidadeObra) {
		this.idUnidadeObra = idUnidadeObra;
	}
}
