package br.com.ezvida.rst.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UatVeiculoGroupedByTipoDTO {

	private Long idTipo;
	private String descricaoTipo;
	private Boolean isTipoAtendimento;
	private List<UatVeiculoDTO> veiculos;
	
	public Long getIdTipo() {
		return idTipo;
	}
	
	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}

	public String getDescricaoTipo() {
		return descricaoTipo;
	}

	public void setDescricaoTipo(String descricaoTipo) {
		this.descricaoTipo = descricaoTipo;
	}

	public List<UatVeiculoDTO> getVeiculos() {
		return veiculos;
	}
	
	public void setVeiculos(List<UatVeiculoDTO> veiculos) {
		this.veiculos = veiculos;
	}

	public Boolean getIsTipoAtendimento() {
		return isTipoAtendimento;
	}

	public void setIsTipoAtendimento(Boolean isTipoAtendimento) {
		this.isTipoAtendimento = isTipoAtendimento;
	}

	public void addVeiculos(List<UatVeiculoDTO> list) {
		this.veiculos = new ArrayList<>();
		this.veiculos.addAll(list);
	}
}
