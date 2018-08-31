package br.com.ezvida.rst.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fw.core.model.BaseEntity;

@Entity
@Table(name = "PAIS", uniqueConstraints = @UniqueConstraint(name = "PK_PAIS", columnNames = { "ID_PAIS" }))
public class Pais extends BaseEntity<Long> {

	private static final long serialVersionUID = 1523081735246803686L;

	@Id
	@Column(name = "ID_PAIS")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PAIS")
	@SequenceGenerator(name = "SEQUENCE_PAIS", sequenceName = "SEQ_PAIS_ID_PAIS", allocationSize = 1)
	private Long id;

	@Column(name = "DS_PAIS", nullable = false, length = 50)
	private String descricao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pais")
	private List<Estado> estados;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pais")
	private List<Regiao> regioes;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Regiao> getRegioes() {
		return regioes;
	}

	public void setRegioes(List<Regiao> regioes) {
		this.regioes = regioes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Pais other = (Pais) obj;
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
		return true;
	}
}
