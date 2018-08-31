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
@Table(name = "END_TRABALHADOR", uniqueConstraints = @UniqueConstraint(name = "PK_END_TRABALHA_01", columnNames = {
		"ID_END_TRABALHADOR" }))
public class EnderecoTrabalhador extends BaseEntity<Long> {

	private static final long serialVersionUID = -5714911305112739249L;

	@Id
	@Column(name = "ID_END_TRABALHADOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_END_TRABALHADOR")
	@SequenceGenerator(name = "SEQUENCE_END_TRABALHADOR", sequenceName = "SEQ_END_TRABALHAD_ID_END_TRABA", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "ID_TRABALHADOR_FK", referencedColumnName = "ID_TRABALHADOR", nullable = false)
	private Trabalhador trabalhador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ENDERECO_FK", referencedColumnName = "ID_ENDERECO", nullable = true)
	private Endereco endereco;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Trabalhador getTrabalhador() {
		return trabalhador;
	}

	public void setTrabalhador(Trabalhador trabalhador) {
		this.trabalhador = trabalhador;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((trabalhador == null) ? 0 : trabalhador.hashCode());
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
		EnderecoTrabalhador other = (EnderecoTrabalhador) obj;
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
		if (trabalhador == null) {
			if (other.trabalhador != null)
				return false;
		} else if (!trabalhador.equals(other.trabalhador))
			return false;
		return true;
	}

}