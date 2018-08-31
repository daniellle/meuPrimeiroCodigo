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
@Table(name = "EMAIL_SINDICATO", uniqueConstraints = @UniqueConstraint(name = "EMAIL_SINDICATO_PK", columnNames = {
		"ID_EMAIL_SINDICATO" }))
public class EmailSindicato extends BaseEntity<Long> {

	private static final long serialVersionUID = 4531458591575188798L;

	@Id
	@Column(name = "ID_EMAIL_SINDICATO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMAIL_SINDICATO")
	@SequenceGenerator(name = "SEQUENCE_EMAIL_SINDICATO", sequenceName = "SEQ_EMAIL_SINDICA_ID_EMAIL_SIN", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMAIL_FK")
	private Email email;

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

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
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
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		EmailSindicato other = (EmailSindicato) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
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
