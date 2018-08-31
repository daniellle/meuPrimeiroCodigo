package br.com.ezvida.rst.model.dto;

import java.util.List;

public class QuestionarioDTO {

	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private List<GrupoPerguntaQuestionarioDTO> listaGrupoPerguntaQuestionario;
	
	public QuestionarioDTO() {
		
	}
	
	public QuestionarioDTO(Long id, String nome, String descricao) {
		this.id = id;
		this.descricao = descricao;
		this.nome = nome;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<GrupoPerguntaQuestionarioDTO> getListaGrupoPerguntaQuestionario() {
		return listaGrupoPerguntaQuestionario;
	}

	public void setListaGrupoPerguntaQuestionario(List<GrupoPerguntaQuestionarioDTO> listaGrupoPerguntaQuestionario) {
		this.listaGrupoPerguntaQuestionario = listaGrupoPerguntaQuestionario;
	}

}
