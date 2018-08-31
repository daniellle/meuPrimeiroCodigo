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
@Table(name = "RESPOSTA_QUEST", uniqueConstraints = @UniqueConstraint(name = "PK_RESPOSTA_QUEST", columnNames = { "ID_RESPOSTA_QUEST" }))
public class RespostaQuestionario extends AbstractData implements Cloneable {

	
	private static final long serialVersionUID = 7492613051615812175L;

	@Id
	@Column(name = "ID_RESPOSTA_QUEST")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_RESPOSTA_QUEST")
	@SequenceGenerator(name = "SEQUENCE_RESPOSTA_QUEST", sequenceName = "SEQ_RESPOSTA_QUES_ID_RESPOSTA_", allocationSize = 1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERGUNTA_QUEST_FK", referencedColumnName = "ID_PERGUNTA_QUEST", nullable = false)
	private PerguntaQuestionario perguntaQuestionario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RESPOSTA_FK", referencedColumnName = "ID_RESPOSTA", nullable = false)
	private Resposta resposta;
	
	@Column(name = "NUM_ORDEM_RESP")
	private Integer ordemResposta;
	
	@Column(name = "PONTUACAO")
	private Integer pontuacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PerguntaQuestionario getPerguntaQuestionario() {
		return perguntaQuestionario;
	}

	public void setPerguntaQuestionario(PerguntaQuestionario perguntaQuestionario) {
		this.perguntaQuestionario = perguntaQuestionario;
	}

	public Resposta getResposta() {
		return resposta;
	}

	public void setResposta(Resposta resposta) {
		this.resposta = resposta;
	}

	public Integer getOrdemResposta() {
		return ordemResposta;
	}

	public void setOrdemResposta(Integer ordemResposta) {
		this.ordemResposta = ordemResposta;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ordemResposta == null) ? 0 : ordemResposta.hashCode());
		result = prime * result + ((perguntaQuestionario == null) ? 0 : perguntaQuestionario.hashCode());
		result = prime * result + ((pontuacao == null) ? 0 : pontuacao.hashCode());
		result = prime * result + ((resposta == null) ? 0 : resposta.hashCode());
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
		RespostaQuestionario other = (RespostaQuestionario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ordemResposta == null) {
			if (other.ordemResposta != null)
				return false;
		} else if (!ordemResposta.equals(other.ordemResposta))
			return false;
		if (perguntaQuestionario == null) {
			if (other.perguntaQuestionario != null)
				return false;
		} else if (!perguntaQuestionario.equals(other.perguntaQuestionario))
			return false;
		if (pontuacao == null) {
			if (other.pontuacao != null)
				return false;
		} else if (!pontuacao.equals(other.pontuacao))
			return false;
		if (resposta == null) {
			if (other.resposta != null)
				return false;
		} else if (!resposta.equals(other.resposta))
			return false;
		return true;
	}
	
	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}

}
