package br.com.ezvida.rst.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidadeObra")
	private Set<UnidadeObraContratoUat> unidadeObraContratoUats;
	
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

	public Set<UnidadeObraContratoUat> getUnidadeObraContratoUats() {
		return unidadeObraContratoUats;
	}

	public void setUnidadeObraContratoUats(Set<UnidadeObraContratoUat> unidadeObraContratoUats) {
		this.unidadeObraContratoUats = unidadeObraContratoUats;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cei == null) ? 0 : cei.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
