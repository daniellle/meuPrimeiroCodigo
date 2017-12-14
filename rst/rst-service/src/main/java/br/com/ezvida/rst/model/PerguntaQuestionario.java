package br.com.ezvida.rst.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.ezvida.rst.converter.TipoRespostaConverter;
import br.com.ezvida.rst.enums.TipoResposta;

@Entity
@Table(name = "PERGUNTA_QUEST", uniqueConstraints = @UniqueConstraint(name = "PK_PERGUNTA_QUEST", columnNames = { "ID_PERGUNTA_QUEST" }))
public class PerguntaQuestionario extends AbstractData implements Cloneable {

	private static final long serialVersionUID = 5981869454149172511L;
	
	@Id
	@Column(name = "ID_PERGUNTA_QUEST")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PERGUNTA_QUEST")
	@SequenceGenerator(name = "SEQUENCE_PERGUNTA_QUEST", sequenceName = "SEQ_PERGUNTA_QUES_ID_PERGUNTA_", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_QUESTIONARIO_FK", referencedColumnName = "ID_QUESTIONARIO", nullable = false)
	private Questionario questionario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERGUNTA_FK", referencedColumnName = "ID_PERGUNTA", nullable = false)
	private Pergunta pergunta;
	
	@Convert(converter = TipoRespostaConverter.class)
	@Column(name = "tp_resposta")
	private TipoResposta tipoResposta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO_PERGUNTA_FK", referencedColumnName = "ID_GRUPO_PERGUNTA", nullable = false)
	private GrupoPergunta grupoPergunta;
	
	@Column(name = "NUM_ORDEM_PERG")
	private Integer ordemPergunta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_INDICADOR_QUEST_FK", referencedColumnName = "ID_INDICADOR_QUEST")
	private IndicadorQuestionario indicadorQuestionario;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perguntaQuestionario")
	private Set<RespostaQuestionario> respostaQuestionarios;
	
	@Column(name = "num_ordem_grupo")
	private Integer ordemGrupo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Questionario getQuestionario() {
		return questionario;
	}

	public void setQuestionario(Questionario questionario) {
		this.questionario = questionario;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public GrupoPergunta getGrupoPergunta() {
		return grupoPergunta;
	}

	public void setGrupoPergunta(GrupoPergunta grupoPergunta) {
		this.grupoPergunta = grupoPergunta;
	}

	public TipoResposta getTipoResposta() {
		return tipoResposta;
	}

	public void setTipoResposta(TipoResposta tipoResposta) {
		this.tipoResposta = tipoResposta;
	}

	public Integer getOrdemPergunta() {
		return ordemPergunta;
	}

	public void setOrdemPergunta(Integer ordemPergunta) {
		this.ordemPergunta = ordemPergunta;
	}

	public IndicadorQuestionario getIndicadorQuestionario() {
		return indicadorQuestionario;
	}

	public void setIndicadorQuestionario(IndicadorQuestionario indicadorQuestionario) {
		this.indicadorQuestionario = indicadorQuestionario;
	}

	public Set<RespostaQuestionario> getRespostaQuestionarios() {
		return respostaQuestionarios;
	}

	public void setRespostaQuestionarios(Set<RespostaQuestionario> respostaQuestionarios) {
		this.respostaQuestionarios = respostaQuestionarios;
	}
	
	public Integer getOrdemGrupo() {
		return ordemGrupo;
	}

	public void setOrdemGrupo(Integer ordemGrupo) {
		this.ordemGrupo = ordemGrupo;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((grupoPergunta == null) ? 0 : grupoPergunta.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ordemGrupo == null) ? 0 : ordemGrupo.hashCode());
		result = prime * result + ((ordemPergunta == null) ? 0 : ordemPergunta.hashCode());
		result = prime * result + ((pergunta == null) ? 0 : pergunta.hashCode());
		result = prime * result + ((tipoResposta == null) ? 0 : tipoResposta.hashCode());
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
		PerguntaQuestionario other = (PerguntaQuestionario) obj;
		if (grupoPergunta == null) {
			if (other.grupoPergunta != null)
				return false;
		} else if (!grupoPergunta.equals(other.grupoPergunta))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ordemGrupo == null) {
			if (other.ordemGrupo != null)
				return false;
		} else if (!ordemGrupo.equals(other.ordemGrupo))
			return false;
		if (ordemPergunta == null) {
			if (other.ordemPergunta != null)
				return false;
		} else if (!ordemPergunta.equals(other.ordemPergunta))
			return false;
		if (pergunta == null) {
			if (other.pergunta != null)
				return false;
		} else if (!pergunta.equals(other.pergunta))
			return false;
		if (tipoResposta != other.tipoResposta)
			return false;
		return true;
	}

}
