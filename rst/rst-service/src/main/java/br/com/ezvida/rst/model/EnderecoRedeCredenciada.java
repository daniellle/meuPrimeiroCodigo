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
@Table(name = "end_rede_credenciada", uniqueConstraints = @UniqueConstraint(name = "pk_end_rede_cre_01", columnNames = { "id_end_rede_credenciada" }))
public class EnderecoRedeCredenciada extends BaseEntity<Long> {

	private static final long serialVersionUID = 110275106697465917L;

	@Id
	@Column(name = "id_end_rede_credenciada")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_END_REDE_CRED")
	@SequenceGenerator(name = "SEQUENCE_END_REDE_CRED", sequenceName = "seq_end_rede_cred_id_end_rede_", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_endereco_fk", referencedColumnName = "ID_ENDERECO", nullable = false)
	private Endereco endereco;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_rede_credenciada_fk", referencedColumnName = "id_rede_credenciada", nullable = false)
	private RedeCredenciada redeCredenciada;

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

	public RedeCredenciada getRedeCredenciada() {
		return redeCredenciada;
	}

	public void setRedeCredenciada(RedeCredenciada redeCredenciada) {
		this.redeCredenciada = redeCredenciada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((redeCredenciada == null) ? 0 : redeCredenciada.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(obj == null)
			return false;
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoRedeCredenciada other = (EnderecoRedeCredenciada) obj;
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
		if (redeCredenciada == null) {
			if (other.redeCredenciada != null)
				return false;
		} else if (!redeCredenciada.equals(other.redeCredenciada))
			return false;
		return true;
	}
}
