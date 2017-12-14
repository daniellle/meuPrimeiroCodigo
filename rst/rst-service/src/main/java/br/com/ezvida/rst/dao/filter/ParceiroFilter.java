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

	public void setId(Long id) {
		this.id = id;
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

	public void setRazaoSocialNome(String razaoSocialNome) {
		this.razaoSocialNome = razaoSocialNome;
	}

	public Long getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Long especialidade) {
		this.especialidade = especialidade;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
}
