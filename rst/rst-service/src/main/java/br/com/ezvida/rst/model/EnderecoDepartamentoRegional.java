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
@Table(name = "END_DEP_REGIONAL", uniqueConstraints = @UniqueConstraint(name = "PK_END_DEP_REGIONAL", columnNames = {
		"ID_END_DEP_REGIONAL" }))
public class EnderecoDepartamentoRegional extends BaseEntity<Long> {

	private static final long serialVersionUID = -6129924713999144965L;

	@Id
	@Column(name = "ID_END_DEP_REGIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_END_DEP_REGIONAL")
	@SequenceGenerator(name = "SEQUENCE_END_DEP_REGIONAL", sequenceName = "SEQ_END_DEP_REGIO_ID_END_DEP_R", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ENDERECO_FK", referencedColumnName = "ID_ENDERECO", nullable = false)
	private Endereco endereco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "ID_DEPARTAMENTO_REGIONAL_FK", referencedColumnName = "ID_DEPARTAMENTO_REGIONAL", nullable = false)
	private DepartamentoRegional departamentoRegional;

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

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
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
		EnderecoDepartamentoRegional other = (EnderecoDepartamentoRegional) obj;
		if (departamentoRegional == null) {
			if (other.departamentoRegional != null)
				return false;
		} else if (!departamentoRegional.equals(other.departamentoRegional))
			return false;
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
		return true;
	}

}
