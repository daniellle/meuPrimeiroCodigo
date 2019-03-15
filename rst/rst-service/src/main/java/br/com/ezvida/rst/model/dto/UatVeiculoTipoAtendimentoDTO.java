package br.com.ezvida.rst.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;

@JsonInclude(Include.NON_NULL)
public class UatVeiculoTipoAtendimentoDTO {

	private Long id;
	private String descricao;
	
	public UatVeiculoTipoAtendimentoDTO() {
	}

	public UatVeiculoTipoAtendimentoDTO(UatVeiculoTipoAtendimento veiculoTipoAtendimento) {
		this.id = veiculoTipoAtendimento.getId();
		this.descricao = veiculoTipoAtendimento.getDescricao();
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
	
}
