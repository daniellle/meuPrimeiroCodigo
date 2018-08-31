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
@Table(name = "PERGUNTA", uniqueConstraints = @UniqueConstraint(name = "PK_PERGUNTA", columnNames = { "ID_PERGUNTA" }))
public class Pergunta extends AbstractData {

	private static final long serialVersionUID = -3353583394667659407L;
	
	@Id
	@Column(name = "ID_PERGUNTA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PERGUNTA")
	@SequenceGenerator(name = "SEQUENCE_PERGUNTA", sequenceName = "SEQ_PERGUNTA_ID_PERGUNTA", allocationSize = 1)
	private Long id;
	
	@Column(name = "DS_PERGUNTA")
	private String descricao;

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
		Pergunta other = (Pergunta) obj;
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
