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
@Table(name = "EMPRESA_UAT", uniqueConstraints = @UniqueConstraint(name = "PK_EMPRESA_UAT", columnNames = { "ID_EMPRESA_UAT" }))
public class EmpresaUnidadeAtendimentoTrabalhador extends AbstractData {

	private static final long serialVersionUID = 7840190208762317762L;
	
	@Id
	@Column(name = "ID_EMPRESA_UAT")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMPRESA_UAT")
	@SequenceGenerator(name = "SEQUENCE_EMPRESA_UAT", sequenceName = "SEQ_EMPRESA_UAT_ID_EMPRESA_U", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMPRESA_FK", referencedColumnName = "ID_EMPRESA", nullable = false)
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK", referencedColumnName = "ID_UND_ATD_TRABALHADOR", nullable = false)
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}
	@PreUpdate
	public void preUpdate() {
		setDataCriacao(new Date());
	}


	@Override
	public Long getId() {
		return id;
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

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeTrabalhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((unidadeAtendimentoTrabalhador == null) ? 0 : unidadeAtendimentoTrabalhador.hashCode());
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
		EmpresaUnidadeAtendimentoTrabalhador other = (EmpresaUnidadeAtendimentoTrabalhador) obj;
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
		if (getDataAlteracao() == null) {
			if (other.getDataAlteracao() != null)
				return false;
		} else if (!getDataAlteracao().equals(other.getDataAlteracao()))
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
		if (unidadeAtendimentoTrabalhador == null) {
			if (other.unidadeAtendimentoTrabalhador != null)
				return false;
		} else if (!unidadeAtendimentoTrabalhador.equals(other.unidadeAtendimentoTrabalhador))
			return false;
		return true;
	}
}
