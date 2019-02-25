package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="UAT_QUADRO_PESSOAL_AREA", uniqueConstraints = @UniqueConstraint(
    name = "IDX_PK_UAT_QUADRO_PESSOAL_AREA", columnNames = { "ID_UAT_QUADRO_PESSOAL_AREA"}))
public class UatQuadroPessoalArea  extends AbstractData {
	
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_UAT_QUADRO_PESSOAL_AREA")
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
