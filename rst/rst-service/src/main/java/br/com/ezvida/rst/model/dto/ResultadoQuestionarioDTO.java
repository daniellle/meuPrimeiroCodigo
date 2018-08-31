package br.com.ezvida.rst.model.dto;

import java.util.List;

public class ResultadoQuestionarioDTO {

	private String tituloQuestionario;
	private String descricaoQuestionario;
	private ClassificacaoDTO classificacao;
	private List<IndicadorDTO> listaIndicadores;
	
	public String getTituloQuestionario() {
		return tituloQuestionario;
	}

	public void setTituloQuestionario(String tituloQuestionario) {
		this.tituloQuestionario = tituloQuestionario;
	}

	public String getDescricaoQuestionario() {
		return descricaoQuestionario;
	}

	public void setDescricaoQuestionario(String descricaoQuestionario) {
		this.descricaoQuestionario = descricaoQuestionario;
	}

	public ClassificacaoDTO getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoDTO classificacao) {
		this.classificacao = classificacao;
	}

	public List<IndicadorDTO> getListaIndicadores() {
		return listaIndicadores;
	}

	public void setListaIndicadores(List<IndicadorDTO> listaIndicadores) {
		this.listaIndicadores = listaIndicadores;
	}
}
