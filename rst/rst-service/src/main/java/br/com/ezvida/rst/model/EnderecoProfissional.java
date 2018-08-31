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
@Table(name = "END_PROFISSIONAL", uniqueConstraints = @UniqueConstraint(name = "END_PROFISSIONAL_PK", columnNames = { "ID_END_PROFISSIONAL" }))
public class EnderecoProfissional extends BaseEntity<Long>{

	private static final long serialVersionUID = 4531548592575188798L;

	@Id
	@Column(name = "ID_END_PROFISSIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_ID_END_PROFISSIONAL")
	@SequenceGenerator(name = "SEQUENCE_ID_END_PROFISSIONAL", sequenceName = "SEQ_END_PROFISSIO_ID_END_PROFI", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ENDERECO_FK")
	private Endereco endereco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PROFISSIONAL_FK")
	@JsonIgnore
	private Profissional profissional;

	@Override
	public Long getId() {
		return id;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((profissional == null) ? 0 : profissional.hashCode());
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
		EnderecoProfissional other = (EnderecoProfissional) obj;
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
		if (profissional == null) {
			if (other.profissional != null)
				return false;
		} else if (!profissional.equals(other.profissional))
			return false;
		return true;
	}
}
