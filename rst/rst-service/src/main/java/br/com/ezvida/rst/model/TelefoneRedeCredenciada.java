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
@Table(name = "tel_rede_credenciada", uniqueConstraints = @UniqueConstraint(name = "pk_tel_rede_cre_01", columnNames = { "id_tel_rede_credenciada" }))
public class TelefoneRedeCredenciada extends BaseEntity<Long>{

	private static final long serialVersionUID = 6173692501341905774L;

	@Id
	@Column(name = "id_tel_rede_credenciada")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TEL_REDE_CRED")
	@SequenceGenerator(name = "SEQ_TEL_REDE_CRED", sequenceName = "seq_tel_rede_cred_id_tel_rede_", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TELEFONE_FK")
	private Telefone telefone;
	
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
	
	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((redeCredenciada == null) ? 0 : redeCredenciada.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
		TelefoneRedeCredenciada other = (TelefoneRedeCredenciada) obj;
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
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}
}
