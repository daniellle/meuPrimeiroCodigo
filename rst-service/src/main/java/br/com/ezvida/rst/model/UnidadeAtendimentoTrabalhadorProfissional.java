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
@Table(name = "UAT_PROFISSIONAL", uniqueConstraints = @UniqueConstraint(name = "UAT_PROFISSIONAL_PK", columnNames = { "ID_UAT_PROFISSIONAL" }))
public class UnidadeAtendimentoTrabalhadorProfissional extends AbstractData {

	private static final long serialVersionUID = 3680514367579279102L;

	@Id
	@Column(name = "ID_UAT_PROFISSIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UAT_PROFISSIONAL")
	@SequenceGenerator(name = "SEQUENCE_UAT_PROFISSIONAL", sequenceName = "SEQ_UAT_PROFISSIO_ID_UAT_PROFI", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROFISSIONAL_FK")
	@JsonIgnore
	private Profissional profissional;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRAB_FK")
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((profissional == null) ? 0 : profissional.hashCode());
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
		UnidadeAtendimentoTrabalhadorProfissional other = (UnidadeAtendimentoTrabalhadorProfissional) obj;
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
		if (unidadeAtendimentoTrabalhador == null) {
			if (other.unidadeAtendimentoTrabalhador != null)
				return false;
		} else if (!unidadeAtendimentoTrabalhador.equals(other.unidadeAtendimentoTrabalhador))
			return false;
		return true;
	}

}
