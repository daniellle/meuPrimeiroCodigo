package br.com.ezvida.rst.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.HashCodeBuilder;

import fw.core.model.BaseEntity;

@Entity
@Table(name = "REGIAO", uniqueConstraints = @UniqueConstraint(name = "PK_REGIAO", columnNames = { "ID_REGIAO" }))
public class Regiao extends BaseEntity<Long> {

	private static final long serialVersionUID = -6665602890439415733L;

	@Id
	@Column(name = "ID_REGIAO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_REGIAO")
	@SequenceGenerator(name = "SEQUENCE_REGIAO", sequenceName = "SEQ_REGIAO_ID", allocationSize = 1)
	private Long id;

	@Column(name = "DS_REGIAO")
	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAIS_FK", referencedColumnName = "ID_PAIS")
	private Pais pais;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "regiao")
	private List<Municipio> municipios;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "regiao")
	private List<Estado> estados;

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

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(descricao).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Regiao other = (Regiao) obj;
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
