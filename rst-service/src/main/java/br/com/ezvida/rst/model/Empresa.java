package br.com.ezvida.rst.model;

import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ezvida.rst.converter.SimNaoConverter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "EMPRESA", uniqueConstraints = @UniqueConstraint(name = "PK_EMPRESA", columnNames = { "ID_EMPRESA" }))
public class Empresa extends AbstractData {

	private static final long serialVersionUID = -1495129704648116450L;

	@Id
	@Column(name = "ID_EMPRESA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMPRESA")
	@SequenceGenerator(name = "SEQUENCE_EMPRESA", sequenceName = "SEQ_EMPRESA_ID_EMPRESA", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RAMO_EMPRESA_FK", referencedColumnName = "ID_RAMO_EMPRESA")
	private RamoEmpresa ramoEmpresa;

	@Column(name = "NM_FANTASIA")
	private String nomeFantasia;

	@Column(name = "DS_RAZAO_SOCIAL")
	private String razaoSocial;

	@Column(name = "NO_CNPJ")
	private String cnpj;

	@Column(name = "NM_RESPONSAVEL")
	private String nomeResponsavel;

	@Column(name = "DS_CARGO_RESPONSAVEL")
	private String cargo;

	@Column(name = "NO_TEL_RESPONSAVEL")
	private String numeroTelefone;

	@Column(name = "NO_NIT_RESPONSAVEL")
	private String numeroNitResponsavel;

	@Column(name = "DS_EMAIL_RESPONSAVEL")
	private String emailResponsavel;

	@Column(name = "NM_CONTATO")
	private String nomeContato;

	@Column(name = "DS_CARGO_CONTATO")
	private String descricaCargoContato;

	@Column(name = "NO_TEL_CONTATO")
	private String numeroTelefoneContato;

	@Column(name = "NO_NIT_CONTATO")
	private String numeroNitContato;

	@Column(name = "DS_EMAIL_CONTATO")
	private String emailContato;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_EMPRESA_FK")
	private TipoEmpresa tipoEmpresa;

	@Column(name = "NO_INSC_MUNICIPAL")
	private String numeroInscricaoMunicipal;

	@Column(name = "DS_URL_EMPRESA")
	private String url;

	@Column(name = "NO_INSC_ESTADUAL")
	private String numeroInscricaoEstadual;

	@Column(name = "FL_SESMT")
	@Convert(converter = SimNaoConverter.class)
	private SimNao sesmt;

	@Column(name = "FL_CIPA")
	@Convert(converter = SimNaoConverter.class)
	private SimNao cipa;
	
	@Column(name = "FL_MATRIZ")
	@Convert(converter = SimNaoConverter.class)
	private SimNao matriz;

	@Column(name = "QTD_MEMBROS_CIPA")
	private Long qtMembrosCIPA;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PORTE_EMPRESA_FK")
	private PorteEmpresa porteEmpresa;

	@Column(name = "FL_DESIGN_CIPA")
	@Convert(converter = SimNaoConverter.class)
	private SimNao designCipa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_segmento_fk")
	private Segmento segmento;
	
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_desativacao", nullable = true)
	private Date dataDesativacao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<EmpresaJornada> empresaJornada;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<EnderecoEmpresa> enderecosEmpresa;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<EmailEmpresa> emailsEmpresa;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<EmpresaTrabalhador> empresaTrabalhadores;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<TelefoneEmpresa> telefoneEmpresa;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<EmpresaUnidadeAtendimentoTrabalhador> empresaUats;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<EmpresaSindicato> empresaSindicato;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<EmpresaCnae> empresaCnaes;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private Set<UnidadeObra> unidadeObra;

	@Transient
	private List<UnidadeAtendimentoTrabalhador> uats;

	@Transient
	private List<Sindicato> sindicatos;

	public Empresa(Long id) {
		this.id = id;
	}
	
	public Empresa() {	}

	public Empresa(Long id, String cnpjEmpresa, String razaoSocialEmpresa) {
		this.id = id;
		this.cnpj = cnpjEmpresa;
		this.razaoSocial = razaoSocialEmpresa;
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
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nome) {
		this.nomeFantasia = nome;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public String getNumeroNitResponsavel() {
		return numeroNitResponsavel;
	}

	public void setNumeroNitResponsavel(String numeroNitReponsavel) {
		this.numeroNitResponsavel = numeroNitReponsavel;
	}

	public String getEmailResponsavel() {
		return emailResponsavel;
	}

	public void setEmailResponsavel(String emailResponsavel) {
		this.emailResponsavel = emailResponsavel;
	}

	public String getDescricaCargoContato() {
		return descricaCargoContato;
	}

	public void setDescricaCargoContato(String descricaCargoContato) {
		this.descricaCargoContato = descricaCargoContato;
	}

	public String getNumeroTelefoneContato() {
		return numeroTelefoneContato;
	}

	public void setNumeroTelefoneContato(String numeroTelefoneContato) {
		this.numeroTelefoneContato = numeroTelefoneContato;
	}

	public String getNumeroNitContato() {
		return numeroNitContato;
	}

	public void setNumeroNitContato(String numeroNitContato) {
		this.numeroNitContato = numeroNitContato;
	}

	public String getEmailContato() {
		return emailContato;
	}

	public void setEmailContato(String emailContato) {
		this.emailContato = emailContato;
	}

	public TipoEmpresa getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public String getNumeroInscricaoMunicipal() {
		return numeroInscricaoMunicipal;
	}

	public void setNumeroInscricaoMunicipal(String numeroInscricaoMunicipal) {
		this.numeroInscricaoMunicipal = numeroInscricaoMunicipal;
	}

	public String getNumeroInscricaoEstadual() {
		return numeroInscricaoEstadual;
	}

	public void setNumeroInscricaoEstadual(String numeroInscricaoEstadual) {
		this.numeroInscricaoEstadual = numeroInscricaoEstadual;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SimNao getSesmt() {
		return sesmt;
	}

	public void setSesmt(SimNao sesmt) {
		this.sesmt = sesmt;
	}

	public SimNao getCipa() {
		return cipa;
	}

	public void setCipa(SimNao cipa) {
		this.cipa = cipa;
	}

	public Long getQtMembrosCIPA() {
		return qtMembrosCIPA;
	}

	public void setQtMembrosCIPA(Long qtMembrosCIPA) {
		this.qtMembrosCIPA = qtMembrosCIPA;
	}

	public PorteEmpresa getPorteEmpresa() {
		return porteEmpresa;
	}

	public void setPorteEmpresa(PorteEmpresa porteEmpresa) {
		this.porteEmpresa = porteEmpresa;
	}

	public SimNao getDesignCipa() {
		return designCipa;
	}

	public void setDesignCipa(SimNao desifnCipa) {
		this.designCipa = desifnCipa;
	}

	public Set<EnderecoEmpresa> getEnderecosEmpresa() {
		return enderecosEmpresa;
	}

	public void setEnderecosEmpresa(Set<EnderecoEmpresa> enderecosEmpresa) {
		this.enderecosEmpresa = enderecosEmpresa;
	}

	public Set<EmailEmpresa> getEmailsEmpresa() {
		return emailsEmpresa;
	}

	public void setEmailsEmpresa(Set<EmailEmpresa> emailsEmpresa) {
		this.emailsEmpresa = emailsEmpresa;
	}

	public Set<EmpresaTrabalhador> getEmpresaTrabalhadores() {
		return empresaTrabalhadores;
	}

	public void setEmpresaTrabalhadores(Set<EmpresaTrabalhador> empresaTrabalhadores) {
		this.empresaTrabalhadores = empresaTrabalhadores;
	}

	public Set<TelefoneEmpresa> getTelefoneEmpresa() {
		return telefoneEmpresa;
	}

	public void setTelefoneEmpresa(Set<TelefoneEmpresa> telefoneEmpresa) {
		this.telefoneEmpresa = telefoneEmpresa;
	}
	
	public Set<EmpresaUnidadeAtendimentoTrabalhador> getEmpresaUats() {
		return empresaUats;
	}

	public void setEmpresaUats(Set<EmpresaUnidadeAtendimentoTrabalhador> empresaUats) {
		this.empresaUats = empresaUats;
	}

	public Set<EmpresaSindicato> getEmpresaSindicato() {
		return empresaSindicato;
	}

	public void setEmpresaSindicato(Set<EmpresaSindicato> empresaSindicato) {
		this.empresaSindicato = empresaSindicato;
	}

	public Set<EmpresaCnae> getEmpresaCnaes() {
		return empresaCnaes;
	}

	public void setEmpresaCnaes(Set<EmpresaCnae> empresaCnes) {
		this.empresaCnaes = empresaCnes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cargo == null) ? 0 : cargo.hashCode());
		result = prime * result + ((cipa == null) ? 0 : cipa.hashCode());
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((descricaCargoContato == null) ? 0 : descricaCargoContato.hashCode());
		result = prime * result + ((designCipa == null) ? 0 : designCipa.hashCode());
		result = prime * result + ((emailContato == null) ? 0 : emailContato.hashCode());
		result = prime * result + ((emailResponsavel == null) ? 0 : emailResponsavel.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((nomeResponsavel == null) ? 0 : nomeResponsavel.hashCode());
		result = prime * result + ((nomeContato == null) ? 0 : nomeContato.hashCode());
		result = prime * result + ((numeroInscricaoEstadual == null) ? 0 : numeroInscricaoEstadual.hashCode());
		result = prime * result + ((numeroInscricaoMunicipal == null) ? 0 : numeroInscricaoMunicipal.hashCode());
		result = prime * result + ((numeroNitContato == null) ? 0 : numeroNitContato.hashCode());
		result = prime * result + ((numeroNitResponsavel == null) ? 0 : numeroNitResponsavel.hashCode());
		result = prime * result + ((numeroTelefone == null) ? 0 : numeroTelefone.hashCode());
		result = prime * result + ((numeroTelefoneContato == null) ? 0 : numeroTelefoneContato.hashCode());
		result = prime * result + ((qtMembrosCIPA == null) ? 0 : qtMembrosCIPA.hashCode());
		result = prime * result + ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
		result = prime * result + ((sesmt == null) ? 0 : sesmt.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Empresa other = (Empresa) obj;
		if (cargo == null) {
			if (other.cargo != null)
				return false;
		} else if (!cargo.equals(other.cargo))
			return false;
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
		if (getDataDesativacao() == null) {
			if (other.getDataDesativacao() != null)
				return false;
		} else if (!getDataDesativacao().equals(other.getDataDesativacao()))
			return false;
		if (descricaCargoContato == null) {
			if (other.descricaCargoContato != null)
				return false;
		} else if (!descricaCargoContato.equals(other.descricaCargoContato))
			return false;
		if (designCipa != other.designCipa)
			return false;
		if (emailContato == null) {
			if (other.emailContato != null)
				return false;
		} else if (!emailContato.equals(other.emailContato))
			return false;
		if (emailResponsavel == null) {
			if (other.emailResponsavel != null)
				return false;
		} else if (!emailResponsavel.equals(other.emailResponsavel))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomeFantasia == null) {
			if (other.nomeFantasia != null)
				return false;
		} else if (!nomeFantasia.equals(other.nomeFantasia))
			return false;
		if (nomeResponsavel == null) {
			if (other.nomeResponsavel != null)
				return false;
		} else if (!nomeResponsavel.equals(other.nomeResponsavel))
			return false;
		if (nomeContato == null) {
			if (other.nomeContato != null)
				return false;
		} else if (!nomeContato.equals(other.nomeContato))
			return false;
		if (numeroInscricaoEstadual == null) {
			if (other.numeroInscricaoEstadual != null)
				return false;
		} else if (!numeroInscricaoEstadual.equals(other.numeroInscricaoEstadual))
			return false;
		if (numeroInscricaoMunicipal == null) {
			if (other.numeroInscricaoMunicipal != null)
				return false;
		} else if (!numeroInscricaoMunicipal.equals(other.numeroInscricaoMunicipal))
			return false;
		if (numeroNitContato == null) {
			if (other.numeroNitContato != null)
				return false;
		} else if (!numeroNitContato.equals(other.numeroNitContato))
			return false;
		if (numeroNitResponsavel == null) {
			if (other.numeroNitResponsavel != null)
				return false;
		} else if (!numeroNitResponsavel.equals(other.numeroNitResponsavel))
			return false;
		if (numeroTelefone == null) {
			if (other.numeroTelefone != null)
				return false;
		} else if (!numeroTelefone.equals(other.numeroTelefone))
			return false;
		if (numeroTelefoneContato == null) {
			if (other.numeroTelefoneContato != null)
				return false;
		} else if (!numeroTelefoneContato.equals(other.numeroTelefoneContato))
			return false;
		if (porteEmpresa == null) {
			if (other.porteEmpresa != null)
				return false;
		} else if (!porteEmpresa.equals(other.porteEmpresa))
			return false;
		if (qtMembrosCIPA == null) {
			if (other.qtMembrosCIPA != null)
				return false;
		} else if (!qtMembrosCIPA.equals(other.qtMembrosCIPA))
			return false;
		if (razaoSocial == null) {
			if (other.razaoSocial != null)
				return false;
		} else if (!razaoSocial.equals(other.razaoSocial))
			return false;
		if (sesmt != other.sesmt)
			return false;
		if (tipoEmpresa == null) {
			if (other.tipoEmpresa != null)
				return false;
		} else if (!tipoEmpresa.equals(other.tipoEmpresa))
			return false;
		return true;
	}

	public Set<EmpresaJornada> getEmpresaJornada() {
		return empresaJornada;
	}

	public void setEmpresaJornada(Set<EmpresaJornada> empresaJornada) {
		this.empresaJornada = empresaJornada;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public SimNao getMatriz() {
		return matriz;
	}

	public void setMatriz(SimNao matriz) {
		this.matriz = matriz;
	}

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}

	public RamoEmpresa getRamoEmpresa() {
		return ramoEmpresa;
	}

	public void setRamoEmpresa(RamoEmpresa ramoEmpresa) {
		this.ramoEmpresa = ramoEmpresa;
	}

	public Set<UnidadeObra> getUnidadeObra() {
		return unidadeObra;
	}

	public void setUnidadeObra(Set<UnidadeObra> unidadeObra) {
		this.unidadeObra = unidadeObra;
	}

	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	public List<UnidadeAtendimentoTrabalhador> getUats() {
		return uats;
	}

	public void setUats(List<UnidadeAtendimentoTrabalhador> uats) {
		this.uats = uats;
	}

	public List<Sindicato> getSindicatos() {
		return sindicatos;
	}

	public void setSindicatos(List<Sindicato> sindicatos) {
		this.sindicatos = sindicatos;
	}

}
