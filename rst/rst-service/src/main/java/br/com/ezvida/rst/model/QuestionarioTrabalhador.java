package br.com.ezvida.rst.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "QUEST_TRAB", uniqueConstraints = @UniqueConstraint(name = "PK_QUEST_TRAB", columnNames = { "ID_QUEST_TRAB" }))
public class QuestionarioTrabalhador extends AbstractData{
	
	private static final long serialVersionUID = -4591101214076376009L;
	
	@Id
	@Column(name = "ID_QUEST_TRAB")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_QUEST_TRAB")
	@SequenceGenerator(name = "SEQUENCE_QUEST_TRAB", sequenceName = "SEQ_QUEST_TRAB_ID_QUEST_TRA", allocationSize = 1)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_QUEST_TRAB")
	private Date dataQuestionarioTrabalhador;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TRABALHADOR_FK", referencedColumnName = "ID_TRABALHADOR")
	private Trabalhador trabalhador;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_QUESTIONARIO_FK", referencedColumnName = "ID_QUESTIONARIO")
	private Questionario questionario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLASSIF_PONT_FK", referencedColumnName = "ID_CLASSIF_PONT")
	private ClassificacaoPontuacao classificacaoPontuacao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "questionarioTrabalhador")
	private Set<RespostaQuestionarioTrabalhador> listaRespostaQuestionarioTrabalhador;
	
	@Column(name = "QTD_PONT")
	private Integer quantidadePonto;
	
	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
		setDataQuestionarioTrabalhador(new Date());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataQuestionarioTrabalhador() {
		return dataQuestionarioTrabalhador;
	}

	public void setDataQuestionarioTrabalhador(Date dataQuestionarioTrabalhador) {
		this.dataQuestionarioTrabalhador = dataQuestionarioTrabalhador;
	}

	public Trabalhador getTrabalhador() {
		return trabalhador;
	}

	public void setTrabalhador(Trabalhador trabalhador) {
		this.trabalhador = trabalhador;
	}

	public Questionario getQuestionario() {
		return questionario;
	}

	public void setQuestionario(Questionario questionario) {
		this.questionario = questionario;
	}

	public ClassificacaoPontuacao getClassificacaoPontuacao() {
		return classificacaoPontuacao;
	}

	public void setClassificacaoPontuacao(ClassificacaoPontuacao classificacaoPontuacao) {
		this.classificacaoPontuacao = classificacaoPontuacao;
	}

	public Set<RespostaQuestionarioTrabalhador> getListaRespostaQuestionarioTrabalhador() {
		return listaRespostaQuestionarioTrabalhador;
	}

	public void setListaRespostaQuestionarioTrabalhador(
			Set<RespostaQuestionarioTrabalhador> listaRespostaQuestionarioTrabalhador) {
		this.listaRespostaQuestionarioTrabalhador = listaRespostaQuestionarioTrabalhador;
	}

	public Integer getQuantidadePonto() {
		return quantidadePonto;
	}

	public void setQuantidadePonto(Integer quantidadePonto) {
		this.quantidadePonto = quantidadePonto;
	}
}
