package br.com.ezvida.rst.model.dto;

public class ProximaDoseDTO {

	private Long id;

	private String dtProximaDoseVacina;

	private boolean remover;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDtProximaDoseVacina() {
		return dtProximaDoseVacina;
	}

	public void setDtProximaDoseVacina(String dtProximaDoseVacina) {
		this.dtProximaDoseVacina = dtProximaDoseVacina;
	}

	public boolean isRemover() {
		return remover;
	}

	public void setRemover(boolean remover) {
		this.remover = remover;
	}

}
