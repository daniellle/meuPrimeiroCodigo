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
@Table(name = "EMAIL_UAT", uniqueConstraints = @UniqueConstraint(name = "ID_EMAIL_UAT_PK", columnNames = { "ID_EMAIL_UAT" }))
public class EmailUnidadeAtendimentoTrabalhador extends BaseEntity<Long> {

	private static final long serialVersionUID = 1097667135279665998L;

	@Id
	@Column(name = "ID_EMAIL_UAT")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EMAIL_UAT")
	@SequenceGenerator(name = "SEQ_EMAIL_UAT", sequenceName = "SEQ_EMAIL_UAT_ID_EMAIL_UAT", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_EMAIL_FK")
	private Email email;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK")
	@JsonIgnore
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		EmailUnidadeAtendimentoTrabalhador other = (EmailUnidadeAtendimentoTrabalhador) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
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
