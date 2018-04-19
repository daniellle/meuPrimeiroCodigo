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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ezvida.rst.converter.SituacaoConverter;
import br.com.ezvida.rst.enums.Situacao;
import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "EMP_TRABALHADOR", uniqueConstraints = @UniqueConstraint(name = "PK_EMP_TRABALHADOR", columnNames = {
		"ID_EMP_TRABALHADOR" }))
public class EmpresaTrabalhador extends AbstractData {

	private static final long serialVersionUID = 8603199078616393538L;

	@Id
	@Column(name = "ID_EMP_TRABALHADOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMP_TRABALHADOR")
	@SequenceGenerator(name = "SEQUENCE_EMP_TRABALHADOR", sequenceName = "SEQ_EMP_TRABALHAD_ID_EMP_TRABA", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TRABALHADOR_FK", referencedColumnName = "ID_TRABALHADOR")
	private Trabalhador trabalhador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EMPRESA_FK", referencedColumnName = "ID_EMPRESA", nullable = false)
	private Empresa empresa;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ADMISSAO")
	private Date dataAdmissao;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_DEMISSAO")
	private Date dataDemissao;

	@Column(name = "FL_SITUACAO")
	@Convert(converter = SituacaoConverter.class)
	private Situacao situacao;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CONTRATO_INI")
	private Date dataIniContrato;
	
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CONTRATO_FIM")
	private Date dataFimContrato;	
	
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
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Trabalhador getTrabalhador() {
		return trabalhador;
	}

	public void setTrabalhador(Trabalhador trabalhador) {
		this.trabalhador = trabalhador;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public Date getDataDemissao() {
		return dataDemissao;
	}

	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public Date getDataIniContrato() {
		return dataIniContrato;
	}

	public void setDataIniContrato(Date dataIniContrato) {
		this.dataIniContrato = dataIniContrato;
	}

	public Date getDataFimContrato() {
		return dataFimContrato;
	}

	public void setDataFimContrato(Date dataFimContrato) {
		this.dataFimContrato = dataFimContrato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dataAdmissao == null) ? 0 : dataAdmissao.hashCode());
		result = prime * result + ((dataDemissao == null) ? 0 : dataDemissao.hashCode());
		result = prime * result + ((dataFimContrato == null) ? 0 : dataFimContrato.hashCode());
		result = prime * result + ((dataIniContrato == null) ? 0 : dataIniContrato.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
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
		EmpresaTrabalhador other = (EmpresaTrabalhador) obj;
		if (dataAdmissao == null) {
			if (other.dataAdmissao != null)
				return false;
		} else if (!dataAdmissao.equals(other.dataAdmissao))
			return false;
		if (dataDemissao == null) {
			if (other.dataDemissao != null)
				return false;
		} else if (!dataDemissao.equals(other.dataDemissao))
			return false;
		if (dataFimContrato == null) {
			if (other.dataFimContrato != null)
				return false;
		} else if (!dataFimContrato.equals(other.dataFimContrato))
			return false;
		if (dataIniContrato == null) {
			if (other.dataIniContrato != null)
				return false;
		} else if (!dataIniContrato.equals(other.dataIniContrato))
			return false;
		if (empresa == null) {
			if (other.empresa != null)
				return false;
		} else if (!empresa.equals(other.empresa))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (situacao != other.situacao)
			return false;
		if (trabalhador == null) {
			if (other.trabalhador != null)
				return false;
		} else if (!trabalhador.equals(other.trabalhador))
			return false;
		return true;
	}

	
}