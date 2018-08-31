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
@Table(name = "jornada", uniqueConstraints = @UniqueConstraint(name = "PK_jornada", columnNames = { "id_jornada" }))
public class Jornada extends BaseEntity<Long> {
	
	private static final long serialVersionUID = -806340605481063074L;
	
	@Id
	@Column(name = "id_jornada")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_JORNADA")
	@SequenceGenerator(name = "SEQUENCE_JORNADA", sequenceName = "seq_jornada_id_jornada", allocationSize = 1)
	private Long id;
	
	@Column(name = "ds_jornada", nullable = false, length = 50)
	private String descricao;
	
	@Column(name = "qtd_horas", nullable = true)
	private Integer qtdHoras;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQtdHoras() {
		return qtdHoras;
	}

	public void setQtdHoras(Integer qtdHoras) {
		this.qtdHoras = qtdHoras;
	}
}
