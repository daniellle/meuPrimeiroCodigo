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

@Entity
@Table(name = "dep_reg_produto", uniqueConstraints = @UniqueConstraint(name = "pk_dep_reg_produto", columnNames = {
"id_dep_reg_produto" }))
public class DepartamentoRegionalProdutoServico extends AbstractData {
	
	
	private static final long serialVersionUID = 4187055692717480761L;

	@Id
	@Column(name = "id_dep_reg_produto")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_DEPARTAMENTO_REGIONAL_PRODUTO_SERVICO")
	@SequenceGenerator(name = "SEQUENCE_DEPARTAMENTO_REGIONAL_PRODUTO_SERVICO", sequenceName = "seq_dep_reg_produ_id_dep_reg_p", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DEPARTAMENTO_REGIONAL_FK", referencedColumnName = "ID_DEPARTAMENTO_REGIONAL")
	private DepartamentoRegional departamentoRegional;
	
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
		result = prime * result + ((departamentoRegional == null) ? 0 : departamentoRegional.hashCode());
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
		DepartamentoRegionalProdutoServico other = (DepartamentoRegionalProdutoServico) obj;
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
		if (departamentoRegional == null) {
			if (other.departamentoRegional != null)
				return false;
		} else if (!departamentoRegional.equals(other.departamentoRegional))
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

	public DepartamentoRegional getDepartamentoRegional() {
		return departamentoRegional;
	}

	public void setDepartamentoRegional(DepartamentoRegional departamentoRegional) {
		this.departamentoRegional = departamentoRegional;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
}
