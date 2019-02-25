package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "UAT_EQUIPAMENTO_TIPO", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_EQUIPAMENTO_TIPO", columnNames = {
"ID_UAT_EQUIPAMENTO_TIPO" }))
public class UatEquipamentoTipo extends AbstractData  {
	
	private static final long serialVersionUID = 1L;
	
    @Id
	@Column(name = "ID_UAT_EQUIPAMENTO_TIPO")
    private Long id;

	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UAT_EQUIPAMENTO_AREA_FK", referencedColumnName = "ID_UAT_EQUIPAMENTO_AREA")
	private UatEquipamentoArea uatEquipamentoArea;

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

	public UatEquipamentoArea getUatEquipamentoArea() {
		return uatEquipamentoArea;
	}

	public void setUatEquipamentoArea(UatEquipamentoArea uatEquipamentoArea) {
		this.uatEquipamentoArea = uatEquipamentoArea;
	}
}
