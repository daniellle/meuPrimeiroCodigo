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
@Table(name = "ESTADO", uniqueConstraints = @UniqueConstraint(name = "PK_ESTADO", columnNames = { "ID_ESTADO" }))
public class Estado extends BaseEntity<Long> {

	private static final long serialVersionUID = -8436826483523278557L;

	@Id
	@Column(name = "ID_ESTADO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_ESTADO")
	@SequenceGenerator(name = "SEQUENCE_ESTADO", sequenceName = "SEQ_ESTADO_ID", allocationSize = 1)
	private Long id;

	@Column(name = "DS_ESTADO", nullable = false, length = 100)
	private String descricao;

	@Column(name = "DS_SIGLA_UF", nullable = false, length = 2)
	private String siglaUF;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAIS_FK", referencedColumnName = "ID_PAIS")
	private Pais pais;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_REGIAO_FK", referencedColumnName = "ID_REGIAO")
	private Regiao regiao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "estado")
	private List<Municipio> municipios;

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

	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public String getSiglaUF() {
		return siglaUF;
	}

	public void setSiglaUF(String siglaUF) {
		this.siglaUF = siglaUF;
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(descricao).append(siglaUF).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
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
		if (pais == null) {
			if (other.pais != null)
				return false;
		} else if (!pais.equals(other.pais))
			return false;
		if (regiao == null) {
			if (other.regiao != null)
				return false;
		} else if (!regiao.equals(other.regiao))
			return false;
		if (siglaUF == null) {
			if (other.siglaUF != null)
				return false;
		} else if (!siglaUF.equals(other.siglaUF))
			return false;
		return true;
	}
}