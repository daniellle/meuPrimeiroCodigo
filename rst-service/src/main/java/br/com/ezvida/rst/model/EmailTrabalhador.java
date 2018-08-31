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
@Table(name = "EMAIL_TRABALHADOR", uniqueConstraints = @UniqueConstraint(name = "PK_EMAIL_TRABAL_01", columnNames = {
		"ID_EMAIL_TRABALHADOR" }))
public class EmailTrabalhador extends BaseEntity<Long> {

	private static final long serialVersionUID = 2414606568529569154L;

	@Id
	@Column(name = "ID_EMAIL_TRABALHADOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMAIL_TRABALHADOR")
	@SequenceGenerator(name = "SEQUENCE_EMAIL_TRABALHADOR", sequenceName = "SEQ_EMAIL_TRABALH_ID_EMAIL_TRA", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "ID_TRABALHADOR_FK", referencedColumnName = "ID_TRABALHADOR")
	private Trabalhador trabalhador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMAIL_FK", referencedColumnName = "ID_EMAIL")
	private Email email;

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

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		EmailTrabalhador other = (EmailTrabalhador) obj;
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
		if (trabalhador == null) {
			if (other.trabalhador != null)
				return false;
		} else if (!trabalhador.equals(other.trabalhador))
			return false;
		return true;
	}
}
