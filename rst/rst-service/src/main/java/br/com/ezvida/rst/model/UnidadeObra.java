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
@Table(name = "UND_OBRA", uniqueConstraints = @UniqueConstraint(name = "PK_UNIDADE_OBRA", columnNames = {
		"ID_UND_OBRA" }))
public class UnidadeObra extends AbstractData {

	private static final long serialVersionUID = -8497013602887929322L;

	@Id
	@Column(name = "ID_UND_OBRA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UNIDADE_OBRA")
	@SequenceGenerator(name = "SEQUENCE_UNIDADE_OBRA", sequenceName = "SEQ_UND_OBRA_ID_UND_OBRA", allocationSize = 1)
	private Long id;

	@Column(name = "DS_UND_OBRA")
	private String descricao;
	
	@Column(name = "CD_CEI")
	private String cei;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "id_empresa_fk", referencedColumnName = "ID_EMPRESA", nullable = false)
	private Empresa empresa;

	@Column(name = "dt_contrato_ini")
	private Date dataContratoInicio;

	@Column(name = "dt_contrato_fim")
	private Date dataContratoFim;

	@Column(name = "fl_inativo")
	private Character flagInativo;
	
	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCei() {
		return cei;
	}

	public void setCei(String cei) {
		this.cei = cei;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Date getDataContratoInicio() {
		return dataContratoInicio;
	}

	public void setDataContratoInicio(Date dataContratoInicio) {
		this.dataContratoInicio = dataContratoInicio;
	}

	public Date getDataContratoFim() {
		return dataContratoFim;
	}

	public void setDataContratoFim(Date dataContratoFim) {
		this.dataContratoFim = dataContratoFim;
	}

	public Character getFlagInativo() {
		return flagInativo;
	}

	public void setFlagInativo(Character flagInativo) {
		this.flagInativo = flagInativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cei == null) ? 0 : cei.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((dataContratoInicio == null) ? 0 : dataContratoInicio.hashCode());
		result = prime * result + ((dataContratoFim == null) ? 0 : dataContratoFim.hashCode());
		result = prime * result + ((flagInativo == null) ? 0 : flagInativo.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
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
		UnidadeObra other = (UnidadeObra) obj;
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
		if (cei == null) {
			if (other.cei != null)
				return false;
		} else if (!cei.equals(other.cei))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
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
