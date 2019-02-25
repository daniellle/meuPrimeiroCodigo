package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fw.core.model.BaseEntity;

@Entity
@Table(name = "UAT_INSTALACAO_FISICA_CATEGORIA", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_INSTALACAO_FISICA_CATEGORIA", columnNames = {
"ID_UAT_INSTALACAO_FISICA_CATEGORIA" }))
public class UatInstalacaoFisicaCategoria extends BaseEntity<Long>  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_UAT_INSTALACAO_FISICA_CATEGORIA")
	private Long id;

	@Column(name = "DESCRICAO", nullable = false)
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
