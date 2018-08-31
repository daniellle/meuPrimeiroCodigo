package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "RESP_QUEST_TRAB", uniqueConstraints = @UniqueConstraint(name = "PK_RESP_QUEST_TRAB", columnNames = {
		"ID_RESP_QUEST_TRAB" }))
public class RespostaQuestionarioTrabalhador extends AbstractData {

	private static final long serialVersionUID = -7158752456327358768L;

	@Id
	@Column(name = "ID_RESP_QUEST_TRAB")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_ID_RESP_QUEST_TRAB")
	@SequenceGenerator(name = "SEQUENCE_ID_RESP_QUEST_TRAB", sequenceName = "SEQ_RESP_QUEST_TR_ID_RESP_QUES", allocationSize = 1)
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_QUEST_TRAB_FK", referencedColumnName = "ID_QUEST_TRAB", nullable = false)
	private QuestionarioTrabalhador questionarioTrabalhador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RESPOSTA_QUEST_FK", referencedColumnName = "ID_RESPOSTA_QUEST", nullable = false)
	private RespostaQuestionario respostaQuestionario;

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public QuestionarioTrabalhador getQuestionarioTrabalhador() {
		return questionarioTrabalhador;
	}

	public void setQuestionarioTrabalhador(QuestionarioTrabalhador questionarioTrabalhador) {
		this.questionarioTrabalhador = questionarioTrabalhador;
	}

	public RespostaQuestionario getRespostaQuestionario() {
		return respostaQuestionario;
	}

	public void setRespostaQuestionario(RespostaQuestionario respostaQuestionario) {
		this.respostaQuestionario = respostaQuestionario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((questionarioTrabalhador == null) ? 0 : questionarioTrabalhador.hashCode());
		result = prime * result + ((respostaQuestionario == null) ? 0 : respostaQuestionario.hashCode());
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
		RespostaQuestionarioTrabalhador other = (RespostaQuestionarioTrabalhador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (questionarioTrabalhador == null) {
			if (other.questionarioTrabalhador != null)
				return false;
		} else if (!questionarioTrabalhador.equals(other.questionarioTrabalhador))
			return false;
		if (respostaQuestionario == null) {
			if (other.respostaQuestionario != null)
				return false;
		} else if (!respostaQuestionario.equals(other.respostaQuestionario))
			return false;
		return true;
	}
}
