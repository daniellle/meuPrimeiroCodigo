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
@Table(name = "TEL_DEP_REGIONAL", uniqueConstraints = @UniqueConstraint(name = "PK_TEL_DEP_REGIONAL", columnNames = {
		"ID_TEL_DEP_REGIONAL" }))
public class TelefoneDepartamentoRegional extends BaseEntity<Long> {

	private static final long serialVersionUID = 7325312478605918115L;

	@Id
	@Column(name = "ID_TEL_DEP_REGIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_TEL_DEP_REGIONAL")
	@SequenceGenerator(name = "SEQUENCE_TEL_DEP_REGIONAL", sequenceName = "SEQ_TEL_DEP_REGIO_ID_TEL_DEP_R", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "ID_DEPARTAMENTO_REGIONAL_FK", referencedColumnName = "ID_DEPARTAMENTO_REGIONAL", nullable = false)
	private DepartamentoRegional departamentoRegional;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TELEFONE_FK", referencedColumnName = "ID_TELEFONE", nullable = false)
	private Telefone telefone;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public DepartamentoRegional getDepartamentoRegional() {
		return departamentoRegional;
	}

	public void setDepartamentoRegional(DepartamentoRegional departamentoRegional) {
		this.departamentoRegional = departamentoRegional;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((departamentoRegional == null) ? 0 : departamentoRegional.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TelefoneDepartamentoRegional other = (TelefoneDepartamentoRegional) obj;
		if (departamentoRegional == null) {
			if (other.departamentoRegional != null)
				return false;
		} else if (!departamentoRegional.equals(other.departamentoRegional))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}

}
