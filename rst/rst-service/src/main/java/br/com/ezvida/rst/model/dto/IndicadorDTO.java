package br.com.ezvida.rst.model.dto;

public class IndicadorDTO {

	private String descricao;
	private String orientacao;
	private Boolean aprovado;

	public IndicadorDTO() {
	}

	public IndicadorDTO(String descricao, String orientacao, Boolean aprovado) {
		this.descricao = descricao;
		this.orientacao = orientacao;
		this.aprovado = aprovado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getAprovado() {
		return aprovado;
	}

	public void setAprovado(Boolean aprovado) {
		this.aprovado = aprovado;
	}

	public String getOrientacao() {
		return orientacao;
	}

	public void setOrientacao(String orientacao) {
		this.orientacao = orientacao;
	}
}
