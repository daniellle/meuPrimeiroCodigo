package br.com.ezvida.rst.model.dto;

import java.util.List;

public class VacinaAutodeclaradaDTO {

	private Long id;

	private String cpf;

	private String nome;

	private String lote;

	private String local;

	private Boolean outrasDoses;

	private String dataVacinacao;

	private String dataProximaDose;

	private String dataCriacao;

	private String dataAlteracao;

	private List<ProximaDoseDTO> listaProximasDoses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Boolean getOutrasDoses() {
		return outrasDoses;
	}

	public void setOutrasDoses(Boolean outrasDoses) {
		this.outrasDoses = outrasDoses;
	}

	public String getDataVacinacao() {
		return dataVacinacao;
	}

	public void setDataVacinacao(String dataVacinacao) {
		this.dataVacinacao = dataVacinacao;
	}

	public String getDataProximaDose() {
		return dataProximaDose;
	}

	public void setDataProximaDose(String dataProximaDose) {
		this.dataProximaDose = dataProximaDose;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(String dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public List<ProximaDoseDTO> getListaProximasDoses() {
		return listaProximasDoses;
	}

	public void setListaProximasDoses(List<ProximaDoseDTO> listaProximasDoses) {
		this.listaProximasDoses = listaProximasDoses;
	}

}
