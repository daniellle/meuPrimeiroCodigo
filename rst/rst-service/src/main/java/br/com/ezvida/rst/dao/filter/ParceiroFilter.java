package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class ParceiroFilter extends FilterBase {

	@QueryParam("id")
	private Long id;

	@QueryParam("cpfCnpj")
	private String cpfCnpj;

	@QueryParam("razaoSocialNome")
	private String razaoSocialNome;

	@QueryParam("especialidade")
	private Long especialidade;

	@QueryParam("situacao")
	private String situacao;

	public Long getId() {
		return id;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getRazaoSocialNome() {
		return razaoSocialNome;
	}

	public Long getEspecialidade() {
		return especialidade;
	}

	public String getSituacao() {
		return situacao;
	}
}
