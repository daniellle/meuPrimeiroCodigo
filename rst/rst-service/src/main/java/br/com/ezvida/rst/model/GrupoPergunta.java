package br.com.ezvida.rst.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "GRUPO_PERGUNTA", uniqueConstraints = @UniqueConstraint(name = "PK_GRUPO_PERGUNTA", columnNames = { "ID_GRUPO_PERGUNTA" }))
public class GrupoPergunta extends AbstractData{

	private static final long serialVersionUID = 7785814006381813819L;

	@Id
	@Column(name = "ID_GRUPO_PERGUNTA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_GRUPO_PERGUNTA")
	@SequenceGenerator(name = "SEQUENCE_GRUPO_PERGUNTA", sequenceName = "SEQ_GRUPO_PERGUNT_ID_GRUPO_PER", allocationSize = 1)
	private Long id;
	
	@Column(name = "DS_GRUPO_PERGUNTA")
	private String descricao;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grupoPergunta")
	private Set<PerguntaQuestionario> listaPerguntaQuestionario;

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
	
	public Set<PerguntaQuestionario> getListaPerguntaQuestionario() {
		return listaPerguntaQuestionario;
	}

	public void setListaPerguntaQuestionario(Set<PerguntaQuestionario> listaPerguntaQuestionario) {
		this.listaPerguntaQuestionario = listaPerguntaQuestionario;
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
		GrupoPergunta other = (GrupoPergunta) obj;
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
