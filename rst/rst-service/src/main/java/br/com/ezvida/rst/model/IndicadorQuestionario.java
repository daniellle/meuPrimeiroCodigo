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
@Table(name = "INDICADOR_QUEST", uniqueConstraints = @UniqueConstraint(name = "PK_INDICADOR_QUEST", columnNames = {
		"ID_INDICADOR_QUEST" }))
public class IndicadorQuestionario extends AbstractData {

	private static final long serialVersionUID = -8173937274405231109L;
	@Id
	@Column(name = "ID_INDICADOR_QUEST")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_INDICADOR_QUEST")
	@SequenceGenerator(name = "SEQUENCE_INDICADOR_QUEST", sequenceName = "SEQ_INDICADOR_QUE_ID_INDICADOR", allocationSize = 1)
	private Long id;

	@Column(name = "DS_INDICADOR_QUEST")
	private String descricao;

	@Column(name = "DS_ORIENTACAO")
	private String orientacao;
	
	@Column(name = "ds_incentivo")
	private String incentivo;
	
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

	public String getOrientacao() {
		return orientacao;
	}

	public void setOrientacao(String orientacao) {
		this.orientacao = orientacao;
	}
	
	
	public String getIncentivo() {
		return incentivo;
	}

	public void setIncentivo(String incentivo) {
		this.incentivo = incentivo;
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
		result = prime * result + ((orientacao == null) ? 0 : orientacao.hashCode());
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
		IndicadorQuestionario other = (IndicadorQuestionario) obj;
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
		if (orientacao == null) {
			if (other.orientacao != null)
				return false;
		} else if (!orientacao.equals(other.orientacao))
			return false;
		return true;
	}
}
