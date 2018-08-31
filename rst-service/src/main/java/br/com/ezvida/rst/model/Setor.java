package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "setor", uniqueConstraints = @UniqueConstraint(name = "pk_setor", columnNames = { "id_setor" }))
public class Setor extends AbstractData {

	private static final long serialVersionUID = -7291300730535960721L;

	@Id
	@Column(name = "id_setor")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_SETOR")
	@SequenceGenerator(name = "SEQUENCE_SETOR", sequenceName = "seq_setor_id_setor", allocationSize = 1)
	private Long id;
	
	@Column(name = "sigla_setor", nullable = false, length = 10)
	private String sigla;
	
	@Column(name = "ds_setor", nullable = false, length = 80)
	private String descricao;

	@Override
	public Long getId() {
		return this.id;
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
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
		Setor other = (Setor) obj;
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
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		return true;
	}

}
