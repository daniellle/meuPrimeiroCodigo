package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "TIPO_QUESTIONARIO", uniqueConstraints = @UniqueConstraint(name = "TP_QUESTIONARIO", columnNames = { "TP_QUESTIONARIO" }))
public class TipoQuestionario extends AbstractData {
	
	private static final long serialVersionUID = 4527768283688592416L;

	@Id
	@Column(name = "TP_QUESTIONARIO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_TP_QUESTIONARIO")
	@SequenceGenerator(name = "SEQUENCE_TP_QUESTIONARIO", sequenceName = "SEQ_TIPO_QUESTION_TP_QUESTIONA", allocationSize = 1)
	private Long id;	
	
	@Column(name = "DS_TP_QUESTIONARIO")
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
		TipoQuestionario other = (TipoQuestionario) obj;
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
