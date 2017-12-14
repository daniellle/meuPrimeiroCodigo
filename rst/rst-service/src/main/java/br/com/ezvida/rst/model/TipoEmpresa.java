package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fw.core.model.BaseEntity;

@Entity
@Table(name = "TIPO_EMPRESA", uniqueConstraints = @UniqueConstraint(name = "TIPO_EMPRESA_PK", columnNames = { "ID_TIPO_EMPRESA" }))
public class TipoEmpresa  extends BaseEntity<Long>{


	private static final long serialVersionUID = -3450996286597133448L;
	
	@Id
	@Column(name = "ID_TIPO_EMPRESA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_TIPO_EMPRESA")
	@SequenceGenerator(name = "SEQUENCE_TIPO_EMPRESA", sequenceName = "SEQ_TIPO_EMPRESA_ID_TIPO_EMPR", allocationSize = 1)	
	private Long id;
	
	@Column(name = "DS_TIPO_EMPRESA", nullable = false)
	private String descricao;
	
	@Override
	public Long getId() {
		return this.id;
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
		TipoEmpresa other = (TipoEmpresa) obj;
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
