package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PERIODICIDADE", uniqueConstraints = @UniqueConstraint(name = "PK_PERIODICIDADE", columnNames = { "ID_PERIODICIDADE" }))
public class Periodicidade extends AbstractData{

	private static final long serialVersionUID = -1682382334943572354L;
	
	@Id
	@Column(name = "ID_PERIODICIDADE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PERIODICIDADE")
	@SequenceGenerator(name = "SEQUENCE_PERIODICIDADE", sequenceName = "SEQ_PERIODICIDADE_ID_PERIODICI", allocationSize = 1)
	private Long id;
	
	@Column(name = "DS_PERIODICIDADE")
	private String descricao;
	
	@Column(name = "CD_PERIODICIDADE")
	private String codigoPeriodicidade;
	
	@Column(name = "QTD_DIAS")
	private Integer quantidadeDias;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCodigoPeriodicidade() {
		return codigoPeriodicidade;
	}

	public void setCodigoPeriodicidade(String codigoPeriodicidade) {
		this.codigoPeriodicidade = codigoPeriodicidade;
	}
	
	public Integer getQuantidadeDias() {
		return quantidadeDias;
	}

	public void setQuantidadeDias(Integer quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigoPeriodicidade == null) ? 0 : codigoPeriodicidade.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((quantidadeDias == null) ? 0 : quantidadeDias.hashCode());
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
		Periodicidade other = (Periodicidade) obj;
		if (codigoPeriodicidade == null) {
			if (other.codigoPeriodicidade != null)
				return false;
		} else if (!codigoPeriodicidade.equals(other.codigoPeriodicidade))
			return false;
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
		if (quantidadeDias == null) {
			if (other.quantidadeDias != null)
				return false;
		} else if (!quantidadeDias.equals(other.quantidadeDias))
			return false;
		return true;
	}

}
