package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fw.core.model.BaseEntity;

@Entity
@Table(name = "END_SINDICATO", uniqueConstraints = @UniqueConstraint(name = "END_SINDICATO_PK", columnNames = {
		"ID_END_SINDICATO" }))
public class EnderecoSindicato extends BaseEntity<Long> {

	private static final long serialVersionUID = 4531458592575188798L;

	@Id
	@Column(name = "ID_END_SINDICATO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_END_SINDICATO")
	@SequenceGenerator(name = "SEQUENCE_END_SINDICATO", sequenceName = "SEQ_END_SINDICATO_ID_END_SINDI", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ENDERECO_FK")
	private Endereco endereco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SINDICATO_FK")
	@JsonIgnore
	private Sindicato sindicato;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((sindicato == null) ? 0 : sindicato.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoSindicato other = (EnderecoSindicato) obj;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sindicato == null) {
			if (other.sindicato != null)
				return false;
		} else if (!sindicato.equals(other.sindicato))
			return false;
		return true;
	}
}
