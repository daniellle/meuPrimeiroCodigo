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
@Table(name = "EMAIL_EMPRESA", uniqueConstraints = @UniqueConstraint(name = "PK_EMAIL_EMPRESA", columnNames = {
		"ID_EMAIL_EMPRESA" }))
public class EmailEmpresa extends BaseEntity<Long> {

	private static final long serialVersionUID = -6391250015850802425L;

	@Id
	@Column(name = "ID_EMAIL_EMPRESA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMAIL_EMPRESA")
	@SequenceGenerator(name = "SEQUENCE_EMAIL_EMPRESA", sequenceName = "SEQ_EMAIL_EMPRESA_ID_EMAIL_EMP", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMAIL_FK")
	private Email email;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMPRESA_FK")
	private Empresa empresa;

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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
		result = prime * result + ((getEmpresa() == null) ? 0 : getEmpresa().hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		EmailEmpresa other = (EmailEmpresa) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (empresa == null) {
			if (other.empresa != null)
				return false;
		} else if (!empresa.equals(other.empresa))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
