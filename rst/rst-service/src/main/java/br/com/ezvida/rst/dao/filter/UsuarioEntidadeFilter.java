package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class UsuarioEntidadeFilter extends FilterBase {

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

	public String getCpf() {
		return cpf;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

}
