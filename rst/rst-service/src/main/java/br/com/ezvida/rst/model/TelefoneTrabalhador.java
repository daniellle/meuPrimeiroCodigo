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
@Table(name = "TEL_TRABALHADOR", uniqueConstraints = @UniqueConstraint(name = "PK_TEL_TRABALHADOR", columnNames = {
		"ID_TEL_TRABALHADOR" }))
public class TelefoneTrabalhador extends BaseEntity<Long> {

	private static final long serialVersionUID = -4684311864006102620L;

	@Id
	@Column(name = "ID_TEL_TRABALHADOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_ID_TEL_TRABALHADOR")
	@SequenceGenerator(name = "SEQUENCE_ID_TEL_TRABALHADOR", sequenceName = "SEQ_TEL_TRABALHAD_ID_TEL_TRABA", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TELEFONE_FK", referencedColumnName = "ID_TELEFONE")
	private Telefone telefone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "ID_TRABALHADOR_FK", referencedColumnName = "ID_TRABALHADOR")
	private Trabalhador trabalhador;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Trabalhador getTrabalhador() {
		return trabalhador;
	}

	public void setTrabalhador(Trabalhador trabalhador) {
		this.trabalhador = trabalhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
		TelefoneTrabalhador other = (TelefoneTrabalhador) obj;
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
		if (trabalhador == null) {
			if (other.trabalhador != null)
				return false;
		} else if (!trabalhador.equals(other.trabalhador))
			return false;
		return true;
	}
}
