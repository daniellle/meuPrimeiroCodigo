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
@Table(name = "TEL_UAT", uniqueConstraints = @UniqueConstraint(name = "ID_TEL_UAT_PK", columnNames = { "ID_TEL_UAT" }))
public class TelefoneUnidadeAtendimentoTrabalhador extends BaseEntity<Long> {

	private static final long serialVersionUID = -1988273305195110480L;

	@Id
	@Column(name = "ID_TEL_UAT")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TEL_UAT")
	@SequenceGenerator(name = "SEQ_TEL_UAT", sequenceName = "SEQ_TEL_UAT_ID_TEL_UAT", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TELEFONE_FK")
	private Telefone telefone;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK")
	@JsonIgnore
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

	@Override
	public Long getId() {
		return id;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
		TelefoneUnidadeAtendimentoTrabalhador other = (TelefoneUnidadeAtendimentoTrabalhador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (unidadeAtendimentoTrabalhador == null) {
			if (other.unidadeAtendimentoTrabalhador != null)
				return false;
		} else if (!unidadeAtendimentoTrabalhador.equals(other.unidadeAtendimentoTrabalhador))
			return false;
		return true;
	}

}