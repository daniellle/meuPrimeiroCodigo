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
@Table(name = "TEL_SINDICATO", uniqueConstraints = @UniqueConstraint(name = "TEL_SINDICATO_PK", columnNames = {
		"ID_TEL_SINDICATO" }))
public class TelefoneSindicato extends BaseEntity<Long> {

	private static final long serialVersionUID = 4531458592575178798L;

	@Id
	@Column(name = "ID_TEL_SINDICATO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_TEL_SINDICATO")
	@SequenceGenerator(name = "SEQUENCE_TEL_SINDICATO", sequenceName = "SEQ_TEL_SINDICATO_ID_TEL_SINDI", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TELEFONE_FK")
	private Telefone telefone;

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

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((sindicato == null) ? 0 : sindicato.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
		TelefoneSindicato other = (TelefoneSindicato) obj;
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
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}
}
