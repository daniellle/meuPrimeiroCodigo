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
@Table(name = "CONSELHO_REGIONAL", uniqueConstraints = @UniqueConstraint(name = "PK_CONSELHO_REGIONAL", columnNames = { "ID_CONSELHO_REGIONAL" }))
public class ConselhoRegional extends BaseEntity<Long> {

	private static final long serialVersionUID = -8436826483523278557L;

	@Id
	@Column(name = "ID_CONSELHO_REGIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_CONSELHO_REGIONAL")
	@SequenceGenerator(name = "SEQUENCE_CONSELHO_REGIONAL", sequenceName = "SEQ_CONSELHO_REGIONAL_ID", allocationSize = 1)
	private Long id;

	@Column(name = "NM_CONSELHO_REGIONAL", nullable = false, length = 50)
	private String nome;

	@Column(name = "DS_SIGLA", nullable = false, length = 10)
	private String sigla;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
