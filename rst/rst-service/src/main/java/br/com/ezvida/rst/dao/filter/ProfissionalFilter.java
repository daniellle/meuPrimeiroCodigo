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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
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

	public void setStatusProfissional(String statusProfissional) {
		this.statusProfissional = statusProfissional;
	}

}
