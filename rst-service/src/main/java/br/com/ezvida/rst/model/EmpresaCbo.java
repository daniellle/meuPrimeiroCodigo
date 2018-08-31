package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "emp_cbo", uniqueConstraints = @UniqueConstraint(name = "PK_emp_cbo", columnNames = { "id_emp_cbo" }))
public class EmpresaCbo extends AbstractData {

	private static final long serialVersionUID = -5930928929809828979L;

	@Id
	@Column(name = "id_emp_cbo")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMP_CBO")
	@SequenceGenerator(name = "SEQUENCE_EMP_CBO", sequenceName = "seq_emp_cbo_id_emp_cbo", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cbo_fk", referencedColumnName = "id_cbo", nullable = false)
	private Cbo cbo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "id_empresa_fk", referencedColumnName = "ID_EMPRESA", nullable = false)
	private Empresa empresa;
	
	public EmpresaCbo() {}
	
	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist()
	public void prePersist() {
		setDataCriacao(new Date());
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cbo getCbo() {
		return cbo;
	}

	public void setCbo(Cbo cbo) {
		this.cbo = cbo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cbo == null) ? 0 : cbo.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
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
		EmpresaCbo other = (EmpresaCbo) obj;
		if (cbo == null) {
			if (other.cbo != null)
				return false;
		} else if (!cbo.equals(other.cbo))
			return false;
		if (getDataAlteracao() == null) {
			if (other.getDataAlteracao() != null)
				return false;
		} else if (!getDataAlteracao().equals(other.getDataAlteracao()))
			return false;
		if (getDataCriacao() == null) {
			if (other.getDataCriacao() != null)
				return false;
		} else if (!getDataCriacao().equals(other.getDataCriacao()))
			return false;
		if (getDataExclusao() == null) {
			if (other.getDataExclusao() != null)
				return false;
		} else if (!getDataExclusao().equals(other.getDataExclusao()))
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
