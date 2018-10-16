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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "emp_trab_lotacao", uniqueConstraints = @UniqueConstraint(name = "pk_emp_trab_lotacao", columnNames = { "id_emp_trab_lotacao" }))
public class EmpresaTrabalhadorLotacao extends AbstractData {

	private static final long serialVersionUID = 6161594033386029694L;

	@Id
	@Column(name = "id_emp_trab_lotacao")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMPRESA_TRAB_LOTACAO")
	@SequenceGenerator(name = "SEQUENCE_EMPRESA_TRAB_LOTACAO", sequenceName = "seq_emp_trab_lota_id_emp_trab_", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empr_trabalhador_fk", referencedColumnName = "id_emp_trabalhador")
	private EmpresaTrabalhador empresaTrabalhador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_emp_lotacao_fk", referencedColumnName = "id_empresa_lotacao")
	private EmpresaLotacao empresaLotacao;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_associacao", updatable = false)
	private Date dataAssociacao;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_desligamento")
	private Date dataDesligamento;

	@Column(name = "fl_inativo")
	private Character flagInativo;

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public EmpresaTrabalhador getEmpresaTrabalhador() {
		return empresaTrabalhador;
	}

	public void setEmpresaTrabalhador(EmpresaTrabalhador empresaTrabalhador) {
		this.empresaTrabalhador = empresaTrabalhador;
	}

	public EmpresaLotacao getEmpresaLotacao() {
		return empresaLotacao;
	}

	public void setEmpresaLotacao(EmpresaLotacao empresaLotacao) {
		this.empresaLotacao = empresaLotacao;
	}

	public Date getDataAssociacao() {
		return dataAssociacao;
	}

	public void setDataAssociacao(Date dataAssociacao) {
		this.dataAssociacao = dataAssociacao;
	}

	public Date getDataDesligamento() {
		return dataDesligamento;
	}

	public void setDataDesligamento(Date dataDesligamento) {
		this.dataDesligamento = dataDesligamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((dataAssociacao == null) ? 0 : dataAssociacao.hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((dataDesligamento == null) ? 0 : dataDesligamento.hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((empresaLotacao == null) ? 0 : empresaLotacao.hashCode());
		result = prime * result + ((empresaTrabalhador == null) ? 0 : empresaTrabalhador.hashCode());
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
		EmpresaTrabalhadorLotacao other = (EmpresaTrabalhadorLotacao) obj;
		if (getDataAlteracao() == null) {
			if (other.getDataAlteracao() != null)
				return false;
		} else if (!getDataAlteracao().equals(other.getDataAlteracao()))
			return false;
		if (dataAssociacao == null) {
			if (other.dataAssociacao != null)
				return false;
		} else if (!dataAssociacao.equals(other.dataAssociacao))
			return false;
		if (getDataCriacao() == null) {
			if (other.getDataCriacao() != null)
				return false;
		} else if (!getDataCriacao().equals(other.getDataCriacao()))
			return false;
		if (dataDesligamento == null) {
			if (other.dataDesligamento != null)
				return false;
		} else if (!dataDesligamento.equals(other.dataDesligamento))
			return false;
		if (getDataExclusao() == null) {
			if (other.getDataExclusao() != null)
				return false;
		} else if (!getDataExclusao().equals(other.getDataExclusao()))
			return false;
		if (empresaLotacao == null) {
			if (other.empresaLotacao != null)
				return false;
		} else if (!empresaLotacao.equals(other.empresaLotacao))
			return false;
		if (empresaTrabalhador == null) {
			if (other.empresaTrabalhador != null)
				return false;
		} else if (!empresaTrabalhador.equals(other.empresaTrabalhador))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
