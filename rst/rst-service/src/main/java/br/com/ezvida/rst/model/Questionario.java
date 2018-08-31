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

import br.com.ezvida.rst.converter.StatusQuestionarioConverter;
import br.com.ezvida.rst.enums.StatusQuestionario;

@Entity
@Table(name = "QUESTIONARIO", uniqueConstraints = @UniqueConstraint(name = "PK_QUESTIONARIO", columnNames = {
		"ID_QUESTIONARIO" }))
public class Questionario extends AbstractData implements Cloneable {

	private static final long serialVersionUID = -1017829898718635175L;

	@Id
	@Column(name = "ID_QUESTIONARIO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_QUESTIONARIO")
	@SequenceGenerator(name = "SEQUENCE_QUESTIONARIO", sequenceName = "seq_questionario_id_questiona", allocationSize = 1)
	private Long id;

	@Column(name = "NM_QUESTIONARIO")
	private String nome;

	@Column(name = "DS_QUESTIONARIO")
	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERIODICIDADE_FK")
	private Periodicidade periodicidade;

	@Column(name = "NO_VERSAO")
	private Integer versao;

	@Convert(converter = StatusQuestionarioConverter.class)
	@Column(name = "FL_STATUS")
	private StatusQuestionario status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TP_QUESTIONARIO_FK", referencedColumnName = "TP_QUESTIONARIO", nullable = false)
	private TipoQuestionario tipoQuestionario;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "questionario")
	private Set<PerguntaQuestionario> listaPerguntaQuestionario;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Periodicidade getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(Periodicidade periodicidade) {
		this.periodicidade = periodicidade;
	}

	public TipoQuestionario getTipoQuestionario() {
		return tipoQuestionario;
	}

	public void setTipoQuestionario(TipoQuestionario tipoQuestionario) {
		this.tipoQuestionario = tipoQuestionario;
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

	public StatusQuestionario getStatus() {
		return status;
	}

	public void setStatus(StatusQuestionario status) {
		this.status = status;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((periodicidade == null) ? 0 : periodicidade.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tipoQuestionario == null) ? 0 : tipoQuestionario.hashCode());
		result = prime * result + ((versao == null) ? 0 : versao.hashCode());
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
		Questionario other = (Questionario) obj;
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
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (periodicidade == null) {
			if (other.periodicidade != null)
				return false;
		} else if (!periodicidade.equals(other.periodicidade))
			return false;
		if (status != other.status)
			return false;
		if (tipoQuestionario == null) {
			if (other.tipoQuestionario != null)
				return false;
		} else if (!tipoQuestionario.equals(other.tipoQuestionario))
			return false;
		if (versao == null) {
			if (other.versao != null)
				return false;
		} else if (!versao.equals(other.versao))
			return false;
		return true;
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}

}
