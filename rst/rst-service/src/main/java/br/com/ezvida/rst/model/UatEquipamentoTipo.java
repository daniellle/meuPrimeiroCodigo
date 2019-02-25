package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UAT_EQUIPAMENTO_TIPO")
public class UatEquipamentoTipo extends AbstractData  {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(name = "ID_UAT_EQUIPAMENTO_TIPO")
    private Long id; 

	private String descricao;

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
