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
@Table(name = "end_parceiro", uniqueConstraints = @UniqueConstraint(name = "pk_end_parceiro_01", columnNames = { "id_end_parceiro" }))
public class EnderecoParceiro extends BaseEntity<Long> {

	private static final long serialVersionUID = -5818768554648314131L;

	@Id
	@Column(name = "id_end_parceiro")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_END_PARCEIRO")
	@SequenceGenerator(name = "SEQUENCE_END_PARCEIRO", sequenceName = "seq_end_parceiro_id_end_parce", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_endereco_fk", referencedColumnName = "ID_ENDERECO", nullable = false)
	private Endereco endereco;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_parceiro_fk", referencedColumnName = "id_parceiro", nullable = false)
	private Parceiro parceiro;

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

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((parceiro == null) ? 0 : parceiro.hashCode());
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
		EnderecoParceiro other = (EnderecoParceiro) obj;
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
		if (parceiro == null) {
			if (other.parceiro != null)
				return false;
		} else if (!parceiro.equals(other.parceiro))
			return false;
		return true;
	}
}
