package br.com.ezvida.rst.model;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PRODUTO", uniqueConstraints = @UniqueConstraint(name = "PK_PRODUTO", columnNames = { "ID_PRODUTO" }))
public class ProdutoServico extends AbstractData {

	
	private static final long serialVersionUID = -4376312568930259906L;

	@Id
	@Column(name = "ID_PRODUTO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PRODUTO")
	@SequenceGenerator(name = "SEQUENCE_PRODUTO", sequenceName = "SEQ_PRODUTO_ID_PRODUTO", allocationSize = 1)
	private Long id;
	
	@Column(name = "NM_PRODUTO")
	private String nome;
	
	@Column(name = "DS_PRODUTO")
	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LINHA_FK", referencedColumnName = "ID_LINHA")
	private Linha linha;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "produtoServico")
	private Set<DepartamentoRegionalProdutoServico> departamentoRegionalProdutoServicos;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "produtoServico")
	private Set<UnidadeAtendimentoTrabalhadorProdutoServico> unidadeAtendimentoTrabalhadorProdutoServico;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	public Set<DepartamentoRegionalProdutoServico> getDepartamentoRegionalProdutoServicos() {
		return departamentoRegionalProdutoServicos;
	}

	public void setDepartamentoRegionalProdutoServicos(
			Set<DepartamentoRegionalProdutoServico> departamentoRegionalProdutoServicos) {
		this.departamentoRegionalProdutoServicos = departamentoRegionalProdutoServicos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((linha == null) ? 0 : linha.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		ProdutoServico other = (ProdutoServico) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (linha == null) {
			if (other.linha != null)
				return false;
		} else if (!linha.equals(other.linha))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public Set<UnidadeAtendimentoTrabalhadorProdutoServico> getUnidadeAtendimentoTrabalhadorProdutoServico() {
		return unidadeAtendimentoTrabalhadorProdutoServico;
	}

	public void setUnidadeAtendimentoTrabalhadorProdutoServico(
			Set<UnidadeAtendimentoTrabalhadorProdutoServico> unidadeAtendimentoTrabalhadorProdutoServico) {
		this.unidadeAtendimentoTrabalhadorProdutoServico = unidadeAtendimentoTrabalhadorProdutoServico;
	}

}