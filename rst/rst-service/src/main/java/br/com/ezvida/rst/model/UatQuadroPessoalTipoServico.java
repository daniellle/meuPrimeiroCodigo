package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="UAT_QUADRO_PESSOAL_TIPO_SERVICO")
public class UatQuadroPessoalTipoServico extends AbstractData {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_UAT_QUADRO_PESSOAL_TIPO_SERVICO")
	private Long id;

	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UAT_QUADRO_PESSOAL_AREA_FK", referencedColumnName = "ID_UAT_QUADRO_PESSOAL_AREA")
	private UatQuadroPessoalArea uatQuadroPessoalArea;

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

	public UatQuadroPessoalArea getUatQuadroPessoalArea() {
		return uatQuadroPessoalArea;
	}

	public void setUatQuadroPessoalArea(UatQuadroPessoalArea uatQuadroPessoalArea) {
		this.uatQuadroPessoalArea = uatQuadroPessoalArea;
	}
}
