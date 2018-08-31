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
@Table(name = "PARC_PRODUTO", uniqueConstraints = @UniqueConstraint(name = "PK_PARCEIRO_PRO_01", columnNames = {
"ID_PARC_PRODUTO" }))
public class ParceiroProdutoServico extends AbstractData {

	
	private static final long serialVersionUID = -2360651162232175960L;

	@Id
	@Column(name= "ID_PARC_PRODUTO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PARCEIRO_PRODUTO")
	@SequenceGenerator(name = "SEQUENCE_PARCEIRO_PRODUTO", sequenceName = "SEQ_REDE_CRED_PRO_ID_REDE_CRED", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = " ID_PARCEIRO_FK", referencedColumnName = "ID_PARCEIRO")
	private Parceiro parceiro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iD_PRODUTO_FK", referencedColumnName = "ID_PRODUTO", nullable = false)
	private ProdutoServico produtoServico;

	
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

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((parceiro == null) ? 0 : parceiro.hashCode());
		result = prime * result + ((produtoServico == null) ? 0 : produtoServico.hashCode());
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
		ParceiroProdutoServico other = (ParceiroProdutoServico) obj;
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
		if (produtoServico == null) {
			if (other.produtoServico != null)
				return false;
		} else if (!produtoServico.equals(other.produtoServico))
			return false;
		return true;
	}

	
	
	
}
