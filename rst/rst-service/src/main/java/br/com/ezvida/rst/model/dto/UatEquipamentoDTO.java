package br.com.ezvida.rst.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.ezvida.rst.model.UatEquipamento;

@JsonInclude(Include.NON_NULL)
public class UatEquipamentoDTO {

	private Long id;
	private Integer quantidade;
	private Long idUat;
	private Long idTipo;
	private String descricao;
	
	public UatEquipamentoDTO(UatEquipamento uatEquipamento) {
		this.id = uatEquipamento.getId();
		this.quantidade = uatEquipamento.getQuantidade();
		this.idTipo = uatEquipamento.getUatEquipamentoTipo().getId();
		this.descricao = uatEquipamento.getUatEquipamentoTipo().getDescricao();
	}
	
	public UatEquipamentoDTO() {
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	public Long getIdUat() {
		return idUat;
	}
	
	public void setIdUat(Long idUat) {
		this.idUat = idUat;
	}
	
	public Long getIdTipo() {
		return idTipo;
	}
	
	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
