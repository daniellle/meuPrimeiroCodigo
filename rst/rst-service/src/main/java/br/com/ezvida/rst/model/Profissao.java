package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fw.core.model.BaseEntity;

@Entity
@Table(name = "PROFISSAO", uniqueConstraints = @UniqueConstraint(name = "PK_PROFISSAO", columnNames = {
		"ID_PROFISSAO" }))
public class Profissao extends BaseEntity<Long> {

	private static final long serialVersionUID = 3313738147732604927L;

	@Id
	@Column(name = "ID_PROFISSAO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PROFISSAO")
	@SequenceGenerator(name = "SEQUENCE_PROFISSAO", sequenceName = "SEQ_PROFISSAO_ID_PROFISSAO", allocationSize = 1)
	private Long id;

	@Column(name = "DS_PROFISSAO", nullable = false, length = 50)
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
