package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import br.com.ezvida.rst.converter.SimNaoConverter;
import br.com.ezvida.rst.enums.SimNao;

@Entity
@Table(name = "emp_cnae", uniqueConstraints = @UniqueConstraint(name = "pk_emp_cnae", columnNames = { "id_emp_cnae" }))
public class EmpresaCnae extends AbstractData {
	
	private static final long serialVersionUID = -8993513357223748137L;
	
	@Id
	@Column(name = "id_emp_cnae")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_emp_cnae")
	@SequenceGenerator(name = "sequence_emp_cnae", sequenceName = "seq_emp_cnae_id_emp_cnae", allocationSize = 1 )
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn (name = "id_cnae_fk", referencedColumnName = "id_cnae")
	private Cnae cnae;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMPRESA_FK", referencedColumnName = "ID_EMPRESA")
	private Empresa empresa;
	
	@Convert(converter = SimNaoConverter.class)
	@Column(name = "fl_principal")
	private SimNao principal;
	
	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Cnae getCnae() {
		return cnae;
	}

	public void setCnae(Cnae cnae) {
		this.cnae = cnae;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public SimNao getPrincipal() {
		return principal;
	}

	public void setPrincipal(SimNao principal) {
		this.principal = principal != null ? principal : SimNao.NAO;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cnae == null) ? 0 : cnae.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
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
		EmpresaCnae other = (EmpresaCnae) obj;
		if (cnae == null) {
			if (other.cnae != null)
				return false;
		} else if (!cnae.equals(other.cnae))
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
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
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
