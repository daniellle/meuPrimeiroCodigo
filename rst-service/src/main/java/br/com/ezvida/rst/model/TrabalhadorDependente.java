package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import br.com.ezvida.rst.converter.SimNaoConverter;
import br.com.ezvida.rst.converter.TipoDependenteConverter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.enums.TipoDependente;

@Entity
@Table(name = "TRAB_DEPENDENTE", uniqueConstraints = @UniqueConstraint(name = "PK_TRAB_DEPENDENTE", columnNames = { "ID_TRAB_DEPENDENTE" }))
public class TrabalhadorDependente extends AbstractData {

	
	private static final long serialVersionUID = -6364029425132338041L;

	@Id
	@Column(name = "ID_TRAB_DEPENDENTE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_TRAB_DEPENDENTE")
	@SequenceGenerator(name = "SEQUENCE_TRAB_DEPENDENTE", sequenceName = "SEQ_TRAB_DEPENDEN_ID_TRAB_DEPE", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DEPENDENTE_FK", referencedColumnName = "ID_DEPENDENTE", nullable = false)
	private Dependente dependente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "ID_TRABALHADOR_FK", referencedColumnName = "ID_TRABALHADOR", nullable = false)
	private Trabalhador trabalhador;

	@Convert(converter = SimNaoConverter.class)
	@Column(name = "FL_INATIVO")
	private SimNao inativo;
	
	@Convert(converter = TipoDependenteConverter.class)
	@Column(name = "FL_TIPO")
	private TipoDependente tipoDependente;

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dependente getDependente() {
		return dependente;
	}

	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}

	public Trabalhador getTrabalhador() {
		return trabalhador;
	}

	public void setTrabalhador(Trabalhador trabalhador) {
		this.trabalhador = trabalhador;
	}

	public SimNao getInativo() {
		return inativo;
	}

	public void setInativo(SimNao inativo) {
		this.inativo = inativo != null ? inativo: SimNao.NAO;
	}

	public TipoDependente getTipoDependente() {
		return tipoDependente;
	}

	public void setTipoDependente(TipoDependente tipoDependente) {
		this.tipoDependente = tipoDependente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((dependente == null) ? 0 : dependente.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inativo == null) ? 0 : inativo.hashCode());
		result = prime * result + ((tipoDependente == null) ? 0 : tipoDependente.hashCode());
		result = prime * result + ((trabalhador == null) ? 0 : trabalhador.hashCode());
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
		TrabalhadorDependente other = (TrabalhadorDependente) obj;
		if (getDataAlteracao() == null) {
			if (other.getDataAlteracao() != null)
				return false;
		} else if (!getDataAlteracao().equals(other.getDataAlteracao()))
			return false;
		if (getDataCriacao() == null) {
			if (other.getDataCriacao() != null)
				return false;
		} else if (!getDataCriacao().equals(other.getDataCriacao()))
			return false;
		if (getDataExclusao() == null) {
			if (other.getDataExclusao() != null)
				return false;
		} else if (!getDataExclusao().equals(other.getDataExclusao()))
			return false;
		if (dependente == null) {
			if (other.dependente != null)
				return false;
		} else if (!dependente.equals(other.dependente))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inativo != other.inativo)
			return false;
		if (tipoDependente != other.tipoDependente)
			return false;
		if (trabalhador == null) {
			if (other.trabalhador != null)
				return false;
		} else if (!trabalhador.equals(other.trabalhador))
			return false;
		return true;
	}
}
