package br.com.ezvida.rst.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.ezvida.rst.model.UatVeiculo;

@JsonInclude(Include.NON_NULL)
public class UatVeiculoDTO {

	private Long id;
	private Integer quantidade;
	private Long idUat;
	private Long idTipo;
	private Long idVeiculoTipoAtendimento;
	private String descricao;
	
	public UatVeiculoDTO() {
	}

	public UatVeiculoDTO(Long id, Integer quantidade, String descricao) {
		this.id = id;
		this.quantidade = quantidade;
		this.descricao = descricao;
	}

	public UatVeiculoDTO(UatVeiculo uatVeiculo) {
		this.id = uatVeiculo.getId();
		this.quantidade = uatVeiculo.getQuantidade();
		this.idUat = uatVeiculo.getUnidadeAtendimentoTrabalhador() != null ? uatVeiculo.getUnidadeAtendimentoTrabalhador().getId() : null;
		this.idVeiculoTipoAtendimento = uatVeiculo.getUnidadeVeiculoTipoAtendimento() != null ? uatVeiculo.getUnidadeVeiculoTipoAtendimento().getId() : null;
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

	public Long getIdVeiculoTipoAtendimento() {
		return idVeiculoTipoAtendimento;
	}

	public void setIdVeiculoTipoAtendimento(Long idVeiculoTipoAtendimento) {
		this.idVeiculoTipoAtendimento = idVeiculoTipoAtendimento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}
}
