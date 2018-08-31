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
@Table(name = "TEL_PROFISSIONAL", uniqueConstraints = @UniqueConstraint(name = "ID_TEL_PROFISSIONAL_PK", columnNames = { "ID_TEL_PROFISSIONAL" }))
public class TelefoneProfissional extends BaseEntity<Long> {

	private static final long serialVersionUID = 6822536142386552829L;

	@Id
	@Column(name = "ID_TEL_PROFISSIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TEL_PROFISSIONAL")
	@SequenceGenerator(name = "SEQ_TEL_PROFISSIONAL", sequenceName = "SEQ_TEL_PROFISSIO_ID_TEL_PROFI", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TELEFONE_FK")
	private Telefone telefone;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PROFISSIONAL_FK")
	@JsonIgnore
	private Profissional profissional;
	
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


	public Profissional getProfissional() {
		return profissional;
	}


	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}


	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((profissional == null) ? 0 : profissional.hashCode());
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
		TelefoneProfissional other = (TelefoneProfissional) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (profissional == null) {
			if (other.profissional != null)
				return false;
		} else if (!profissional.equals(other.profissional))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}
}
