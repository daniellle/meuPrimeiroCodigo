package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rede_cred_produto", uniqueConstraints = @UniqueConstraint(name = "pk_rede_cred_prod", columnNames = {
"ID_REDE_CRED_PRODUTO" }))
public class RedeCredenciadaProdutoServico extends AbstractData {

	private static final long serialVersionUID = -1970572234150009861L;

	@Id
	@Column(name= "ID_REDE_CRED_PRODUTO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_REDE_CRED_PRODUTO")
	@SequenceGenerator(name = "SEQUENCE_REDE_CRED_PRODUTO", sequenceName = "seq_rede_cred_pro_id_rede_cred", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = " id_rede_credenciada_fk", referencedColumnName = "id_rede_credenciada")
	private RedeCredenciada redeCredenciada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_produto_fk", referencedColumnName = "id_produto", nullable = false)
	private ProdutoServico produtoServico;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((produtoServico == null) ? 0 : produtoServico.hashCode());
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
		RedeCredenciadaProdutoServico other = (RedeCredenciadaProdutoServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (produtoServico == null) {
			if (other.produtoServico != null)
				return false;
		} else if (!produtoServico.equals(other.produtoServico))
			return false;
		if (redeCredenciada == null) {
			if (other.redeCredenciada != null)
				return false;
		} else if (!redeCredenciada.equals(other.redeCredenciada))
			return false;
		return true;
	}

	public RedeCredenciada getRedeCredenciada() {
		return redeCredenciada;
	}

	public void setRedeCredenciada(RedeCredenciada redeCredenciada) {
		this.redeCredenciada = redeCredenciada;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}
	
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}
	
	
	
}
