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
	
	@QueryParam("porte")
	private Long idPorte;
	
	@QueryParam("estado")
	private Long idEstado;
	
	@QueryParam("cnae")
	private String codCnae;
	
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
	
	public Long getIdPorte() {
		return idPorte;
	}
	
	public Long getIdEstado() {
		return idEstado;
	}
	
	public String getCodCnae() {
		return codCnae;
	}

}
