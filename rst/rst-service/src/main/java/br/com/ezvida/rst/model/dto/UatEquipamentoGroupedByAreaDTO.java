package br.com.ezvida.rst.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.ezvida.rst.model.UatEquipamentoArea;

@JsonInclude(Include.NON_NULL)
public class UatEquipamentoGroupedByAreaDTO {

	private UatEquipamentoArea area;
	private List<UatEquipamentoDTO> equipamentos;
	
	public UatEquipamentoGroupedByAreaDTO() {
	}

	public UatEquipamentoGroupedByAreaDTO(UatEquipamentoArea area, List<UatEquipamentoDTO> equipamentos) {
		this.area = area;
		this.equipamentos = equipamentos;
	}

	public UatEquipamentoArea getArea() {
		return area;
	}
	
	public void setArea(UatEquipamentoArea area) {
		this.area = area;
	}
	
	public List<UatEquipamentoDTO> getEquipamentos() {
		return equipamentos;
	}
	
	public void setEquipamentos(List<UatEquipamentoDTO> equipamentos) {
		this.equipamentos = equipamentos;
	}
}
