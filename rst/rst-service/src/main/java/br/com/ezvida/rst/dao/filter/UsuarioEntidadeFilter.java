package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class UsuarioEntidadeFilter extends FilterBase{
	
	@QueryParam("cpf")
	private String cpf;
	
	@QueryParam("razaoSocial")
	private String razaoSocial;
	
	@QueryParam("cnpj")
	private String cnpj;
	
	@QueryParam("idEstado")
	private Long idEstado;
	
	@QueryParam("nomeFantasia")
	private String nomeFantasia;
	
	public UsuarioEntidadeFilter() {
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

}
