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
@Table(name = "email_rede_credenciada", uniqueConstraints = @UniqueConstraint(name = "pk_email_rede_c_01", columnNames = { "id_email_rede_credenciada" }))
public class EmailRedeCredenciada extends BaseEntity<Long>{

	private static final long serialVersionUID = -1776496042097001603L;

	@Id
	@Column(name = "id_email_rede_credenciada")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMAIL_REDE_CREDENCIADA")
	@SequenceGenerator(name = "SEQUENCE_EMAIL_REDE_CREDENCIADA", sequenceName = "seq_email_rede_cr_id_email_red", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_EMAIL_FK")
	private Email email;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "id_rede_credenciada_fk")
	private RedeCredenciada redeCredenciada;

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
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((redeCredenciada == null) ? 0 : redeCredenciada.hashCode());
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
		EmailRedeCredenciada other = (EmailRedeCredenciada) obj;
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
		if (redeCredenciada == null) {
			if (other.redeCredenciada != null)
				return false;
		} else if (!redeCredenciada.equals(other.redeCredenciada))
			return false;
		return true;
	}
}

