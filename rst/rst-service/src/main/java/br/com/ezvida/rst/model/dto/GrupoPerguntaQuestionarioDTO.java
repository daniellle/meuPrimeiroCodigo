package br.com.ezvida.rst.model.dto;

import java.util.List;

public class GrupoPerguntaQuestionarioDTO {

	private Long id;
	
	private String descricao;
	
	private Integer ordenacao;
	
	private List<PerguntaQuestionarioDTO> listaPerguntaQuestionarioDTO;
	
	public GrupoPerguntaQuestionarioDTO(Long id, String descricao, Integer ordenacao) {
		this.id = id;
		this.descricao = descricao;
		this.ordenacao = ordenacao;
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

	public List<PerguntaQuestionarioDTO> getListaPerguntaQuestionarioDTO() {
		return listaPerguntaQuestionarioDTO;
	}

	public void setListaPerguntaQuestionarioDTO(List<PerguntaQuestionarioDTO> listaPerguntaQuestionarioDTO) {
		this.listaPerguntaQuestionarioDTO = listaPerguntaQuestionarioDTO;
	}
}
