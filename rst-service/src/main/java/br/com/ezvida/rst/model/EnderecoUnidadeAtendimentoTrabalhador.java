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
@Table(name = "END_UAT", uniqueConstraints = @UniqueConstraint(name = "ID_END_UAT_PK", columnNames = { "ID_END_UAT" }))
public class EnderecoUnidadeAtendimentoTrabalhador extends BaseEntity<Long> {

	private static final long serialVersionUID = -1097667135279765998L;

	@Id
	@Column(name = "ID_END_UAT")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENDERECO_UAT")
	@SequenceGenerator(name = "SEQ_ENDERECO_UAT", sequenceName = "SEQ_END_UAT_ID_END_UAT", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ENDERECO_FK")
	private Endereco endereco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK")
	@JsonIgnore
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

	@Override
	public Long getId() {
		return id;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
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
		EnderecoUnidadeAtendimentoTrabalhador other = (EnderecoUnidadeAtendimentoTrabalhador) obj;
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
		if (unidadeAtendimentoTrabalhador == null) {
			if (other.unidadeAtendimentoTrabalhador != null)
				return false;
		} else if (!unidadeAtendimentoTrabalhador.equals(other.unidadeAtendimentoTrabalhador))
			return false;
		return true;
	}
}
	
	
