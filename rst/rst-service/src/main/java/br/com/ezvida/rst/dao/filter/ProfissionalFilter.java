package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class ProfissionalFilter extends FilterBase {
	
	@QueryParam("cpf")
	private String cpf;
	
	@QueryParam("registro")
	private String registro;
	
	@QueryParam("nome")
	private String nome;
	
	@QueryParam("statusProfissional")
	private String statusProfissional;

	public String getNome() {
		return nome;
	}

	public String getRegistro() {
		return registro;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getStatusProfissional() {
		return statusProfissional;
	}

	

}
