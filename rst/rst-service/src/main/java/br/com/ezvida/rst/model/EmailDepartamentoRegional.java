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
@Table(name = "EMAIL_DEP_REGIONAL", uniqueConstraints = @UniqueConstraint(name = "PK_EMAIL_DEP_REGIONAL", columnNames = {
		"ID_EMAIL_DEP_REGIONAL" }))
public class EmailDepartamentoRegional extends BaseEntity<Long> {

	private static final long serialVersionUID = 4275503644559361251L;

	@Id
	@Column(name = "ID_EMAIL_DEP_REGIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMAIL_DEP_REGIONAL")
	@SequenceGenerator(name = "SEQUENCE_EMAIL_DEP_REGIONAL", sequenceName = "SEQ_EMAIL_DEP_REG_ID_EMAIL_DEP", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMAIL_FK", referencedColumnName = "ID_EMAIL", nullable = false)
	private Email email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "ID_DEPARTAMENTO_REGIONAL_FK", referencedColumnName = "ID_DEPARTAMENTO_REGIONAL", nullable = false)
	private DepartamentoRegional departamentoRegional;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public DepartamentoRegional getDepartamentoRegional() {
		return departamentoRegional;
	}

	public void setDepartamentoRegional(DepartamentoRegional departamentoRegional) {
		this.departamentoRegional = departamentoRegional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((departamentoRegional == null) ? 0 : departamentoRegional.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		EmailDepartamentoRegional other = (EmailDepartamentoRegional) obj;
		if (departamentoRegional == null) {
			if (other.departamentoRegional != null)
				return false;
		} else if (!departamentoRegional.equals(other.departamentoRegional))
			return false;
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
		return true;
	}
}
