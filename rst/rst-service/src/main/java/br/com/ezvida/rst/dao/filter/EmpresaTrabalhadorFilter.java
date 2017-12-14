package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class EmpresaTrabalhadorFilter extends FilterBase {

	@QueryParam("cpf")
	private String cpf;

	@QueryParam("nome")
	private String nome;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cPF) {
		cpf = cPF;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}
