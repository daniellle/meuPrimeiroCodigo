package br.com.ezvida.rst.model.dto;

public class ProdutoServicoDTO {

	private String nome;

	public ProdutoServicoDTO() {

	}

	public ProdutoServicoDTO(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
