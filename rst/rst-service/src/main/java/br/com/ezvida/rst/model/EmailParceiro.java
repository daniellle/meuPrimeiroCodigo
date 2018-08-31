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
@Table(name = "email_parceiro", uniqueConstraints = @UniqueConstraint(name = "pk_email_parceiro", columnNames = { "id_email_parceiro" }))
public class EmailParceiro extends BaseEntity<Long>{

	private static final long serialVersionUID = 7278259990468404500L;

	@Id
	@Column(name = "id_email_parceiro")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMAIL_PARCEIRO")
	@SequenceGenerator(name = "SEQUENCE_EMAIL_PARCEIRO", sequenceName = "seq_email_parceir_id_email_par", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_EMAIL_FK")
	private Email email;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "id_parceiro_fk")
	private Parceiro parceiro;

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
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		EmailParceiro other = (EmailParceiro) obj;
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
		if (parceiro == null) {
			if (other.parceiro != null)
				return false;
		} else if (!parceiro.equals(other.parceiro))
			return false;
		return true;
	}
}

