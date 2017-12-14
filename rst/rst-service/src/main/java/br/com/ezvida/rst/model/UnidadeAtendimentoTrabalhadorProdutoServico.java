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
@Table(name = "uat_produto", uniqueConstraints = @UniqueConstraint(name = "pk_und_atend_produto", columnNames = {
"id_uat_produto" }))
public class UnidadeAtendimentoTrabalhadorProdutoServico extends AbstractData {

	private static final long serialVersionUID = -8828628857912907655L;

	@Id
	@Column(name = "id_uat_produto")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UAT_PRODUTO_SERVICO")
	@SequenceGenerator(name = "SEQUENCE_UAT_PRODUTO_SERVICO", sequenceName = "seq_uat_produto_id_uat_produ", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK", referencedColumnName = "ID_UND_ATD_TRABALHADOR")
	private UnidadeAtendimentoTrabalhador uat;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_produto_fk", referencedColumnName = "id_produto")
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((uat == null) ? 0 : uat.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		UnidadeAtendimentoTrabalhadorProdutoServico other = (UnidadeAtendimentoTrabalhadorProdutoServico) obj;
		if (getDataAlteracao() == null) {
			if (other.getDataAlteracao() != null)
				return false;
		} else if (!getDataAlteracao().equals(other.getDataAlteracao()))
			return false;
		if (getDataCriacao() == null) {
			if (other.getDataCriacao() != null)
				return false;
		} else if (!getDataCriacao().equals(other.getDataCriacao()))
			return false;
		if (getDataExclusao() == null) {
			if (other.getDataExclusao() != null)
				return false;
		} else if (!getDataExclusao().equals(other.getDataExclusao()))
			return false;
		if (uat == null) {
			if (other.uat != null)
				return false;
		} else if (!uat.equals(other.uat))
			return false;
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
		return true;
	}

	@Override
	public Long getId() {
	
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	public UnidadeAtendimentoTrabalhador getUat() {
		return uat;
	}

	public void setUat(UnidadeAtendimentoTrabalhador uat) {
		this.uat = uat;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
}
