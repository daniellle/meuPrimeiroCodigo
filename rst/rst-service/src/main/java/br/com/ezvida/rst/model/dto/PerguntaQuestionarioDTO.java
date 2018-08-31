package br.com.ezvida.rst.model.dto;

import java.util.List;

import br.com.ezvida.rst.enums.TipoResposta;

public class PerguntaQuestionarioDTO {

	private Long id;
	
	private String descricao;

	private Integer ordenacao;

	private TipoResposta tipoResposta;
	
	private String numeracao;

	private List<RespostaQuestionarioDTO> listaRespostaQuestionario;

	public PerguntaQuestionarioDTO() {

	}

	public PerguntaQuestionarioDTO(Long id, String descricao, Integer ordenacao, 
			TipoResposta tipoResposta) {
		this.id = id;
		this.descricao = descricao;
		this.ordenacao = ordenacao;
		this.tipoResposta = tipoResposta;
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
	
	public TipoResposta getTipoResposta() {
		return tipoResposta;
	}

	public void setTipoResposta(TipoResposta tipoResposta) {
		this.tipoResposta = tipoResposta;
	}

	public String getNumeracao() {
		return numeracao;
	}

	public void setNumeracao(String numeracao) {
		this.numeracao = numeracao;
	}

	public List<RespostaQuestionarioDTO> getListaRespostaQuestionario() {
		return listaRespostaQuestionario;
	}

	public void setListaRespostaQuestionario(List<RespostaQuestionarioDTO> listaRespostaQuestionario) {
		this.listaRespostaQuestionario = listaRespostaQuestionario;
	}
}
