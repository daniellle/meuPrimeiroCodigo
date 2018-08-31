package br.com.ezvida.rst.model.dto;

public class RespostaQuestionarioDTO {

	private Long id;

	private String descricao;

	private Integer ordenacao;

	private Boolean selecionado;
	
	private Integer pontuacao;

	public RespostaQuestionarioDTO() {

	}

	public RespostaQuestionarioDTO(Long id, String descricao, Integer ordenacao, Integer pontuacao) {
		this.id = id;
		this.descricao = descricao;
		this.ordenacao = ordenacao;
		this.pontuacao = pontuacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(Integer ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}
}
