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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ezvida.rst.converter.SimNaoConverter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "SINDICATO", uniqueConstraints = @UniqueConstraint(name = "PK_SINDICATO", columnNames = {
		"ID_SINDICATO" }))
public class Sindicato extends AbstractData {

	private static final long serialVersionUID = 3277701293800151197L;

	@Id
	@Column(name = "ID_SINDICATO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_SINDICATO")
	@SequenceGenerator(name = "SEQUENCE_SINDICATO", sequenceName = "SEQ_SINDICATO_ID_SINDICATO", allocationSize = 1)
	private Long id;

	@Column(name = "NO_CNPJ")
	private String cnpj;

	@Column(name = "DS_RAZAO_SOCIAL")
	private String razaoSocial;

	@Column(name = "NM_FANTASIA")
	private String nomeFantasia;

	@Column(name = "NO_INSC_MUNICIPAL")
	private String inscricaoMunicipal;

	@Column(name = "NO_INSC_ESTADUAL")
	private String inscricaoEstadual;

	@Column(name = "FL_SESMT")
	@Convert(converter = SimNaoConverter.class)
	private SimNao sesmt;

	@Column(name = "FL_CIPA")
	@Convert(converter = SimNaoConverter.class)
	private SimNao cipa;

	@Column(name = "QTD_MEMBROS_CIPA")
	private Long qtdMembrosCipa;

	@Column(name = "FL_DESIGN_CIPA")
	@Convert(converter = SimNaoConverter.class)
	private SimNao designCipa;

	@OneToMany(mappedBy = "sindicato", fetch = FetchType.LAZY)
	private Set<EnderecoSindicato> endereco;

	@OneToMany(mappedBy = "sindicato", fetch = FetchType.LAZY)
	private Set<EmailSindicato> email;

	@OneToMany(mappedBy = "sindicato", fetch = FetchType.LAZY)
	private Set<TelefoneSindicato> telefone;
	
	@OneToMany(mappedBy = "sindicato", fetch = FetchType.LAZY)
	private Set<EmpresaSindicato> empresaSindicato;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_desativacao", nullable = true)
	private Date dataDesativacao;
	
	public Sindicato() {
		// construtor padrao
	}

	public Sindicato(String cnpj, String razaoSocial, String nomeFantasia) {
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
	}

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

	public String getCnpj() {
		return cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public SimNao getSesmt() {
		return sesmt;
	}

	public SimNao getCipa() {
		return cipa;
	}

	public Long getQtdMembrosCipa() {
		return qtdMembrosCipa;
	}

	public SimNao getDesignCipa() {
		return designCipa;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public void setSesmt(SimNao sesmt) {
		this.sesmt = sesmt;
	}

	public void setCipa(SimNao cipa) {
		this.cipa = cipa;
	}

	public void setQtdMembrosCipa(Long qtdMembrosCipa) {
		this.qtdMembrosCipa = qtdMembrosCipa;
	}

	public void setDesignCipa(SimNao designCipa) {
		this.designCipa = designCipa;
	}

	public Set<EnderecoSindicato> getEndereco() {
		return endereco;
	}

	public Set<EmailSindicato> getEmail() {
		return email;
	}

	public Set<TelefoneSindicato> getTelefone() {
		return telefone;
	}

	public void setEndereco(Set<EnderecoSindicato> endereco) {
		this.endereco = endereco;
	}

	public void setEmail(Set<EmailSindicato> email) {
		this.email = email;
	}

	public void setTelefone(Set<TelefoneSindicato> telefone) {
		this.telefone = telefone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cipa == null) ? 0 : cipa.hashCode());
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((designCipa == null) ? 0 : designCipa.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inscricaoEstadual == null) ? 0 : inscricaoEstadual.hashCode());
		result = prime * result + ((inscricaoMunicipal == null) ? 0 : inscricaoMunicipal.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((qtdMembrosCipa == null) ? 0 : qtdMembrosCipa.hashCode());
		result = prime * result + ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
		result = prime * result + ((sesmt == null) ? 0 : sesmt.hashCode());
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
		Sindicato other = (Sindicato) obj;
		if (cipa != other.cipa)
			return false;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
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
		if (designCipa != other.designCipa)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inscricaoEstadual == null) {
			if (other.inscricaoEstadual != null)
				return false;
		} else if (!inscricaoEstadual.equals(other.inscricaoEstadual))
			return false;
		if (inscricaoMunicipal == null) {
			if (other.inscricaoMunicipal != null)
				return false;
		} else if (!inscricaoMunicipal.equals(other.inscricaoMunicipal))
			return false;
		if (nomeFantasia == null) {
			if (other.nomeFantasia != null)
				return false;
		} else if (!nomeFantasia.equals(other.nomeFantasia))
			return false;
		if (qtdMembrosCipa == null) {
			if (other.qtdMembrosCipa != null)
				return false;
		} else if (!qtdMembrosCipa.equals(other.qtdMembrosCipa))
			return false;
		if (razaoSocial == null) {
			if (other.razaoSocial != null)
				return false;
		} else if (!razaoSocial.equals(other.razaoSocial))
			return false;
		if (sesmt != other.sesmt)
			return false;
		return true;
	}

	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	public Set<EmpresaSindicato> getEmpresaSindicato() {
		return empresaSindicato;
	}

	public void setEmpresaSindicato(Set<EmpresaSindicato> empresaSindicato) {
		this.empresaSindicato = empresaSindicato;
	}

}
