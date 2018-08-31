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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "rede_credenciada", uniqueConstraints = @UniqueConstraint(name = "pk_rede_credenciada", columnNames = { "id_rede_credenciada" }))
public class RedeCredenciada extends AbstractData {

	private static final long serialVersionUID = -7967967136128988557L;

	@Id
	@Column(name = "id_rede_credenciada")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_REDE_CREDENCIADA")
	@SequenceGenerator(name = "SEQUENCE_REDE_CREDENCIADA", sequenceName = "seq_rede_credenci_id_rede_cred", allocationSize = 1)
	private Long id;

	@Column(name = "ds_razao_social")
	private String razaoSocial;

	@Column(name = "nm_fantasia")
	private String nomeFantasia;

	@Column(name = "no_cnpj")
	private String numeroCnpj;

	@Column(name = "nm_responsavel")
	private String nomeResponsavel;

	@Column(name = "ds_cargo_responsavel")
	private String cargoResponsavel;

	@Column(name = "no_tel_responsavel")
	private String numeroTelefoneResponsavel;

	@Column(name = "no_nit_responsavel")
	private String numeroNitResponsavel;

	@Column(name = "ds_email_responsavel")
	private String emailResponsavel;

	@Column(name = "no_insc_municipal")
	private String inscricaoMunicipal;

	@Column(name = "no_insc_estadual")
	private String inscricaoEstadual;

	@Column(name = "ds_url_rede_cred")
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_porte_empresa_fk")
	private PorteEmpresa porteEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_empresa_fk")
	private TipoEmpresa tipoEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_segmento_fk")
	private Segmento segmento;

	@JsonSerialize(using = DateJsonSerializer.class)
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_desligamento")
	private Date dataDesligamento;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "redeCredenciada")
	private Set<EmailRedeCredenciada> emailsRedeCredenciada;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "redeCredenciada")
	private Set<EnderecoRedeCredenciada> enderecosRedeCredenciada;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "redeCredenciada")
	private Set<TelefoneRedeCredenciada> telefonesRedeCredenciada;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "redeCredenciada")
	private Set<RedeCredenciadaProdutoServico> redeCredenciadaProdutoServico;

	public RedeCredenciada() {}
	
	public RedeCredenciada(Long id) {
		this.id = id;
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
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantansia) {
		this.nomeFantasia = nomeFantansia;
	}

	public String getNumeroCnpj() {
		return numeroCnpj;
	}

	public void setNumeroCnpj(String numeroCnpj) {
		this.numeroCnpj = numeroCnpj;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getCargoResponsavel() {
		return cargoResponsavel;
	}

	public void setCargoResponsavel(String cargoResponsavel) {
		this.cargoResponsavel = cargoResponsavel;
	}

	public String getNumeroTelefoneResponsavel() {
		return numeroTelefoneResponsavel;
	}

	public void setNumeroTelefoneResponsavel(String numeroTelefoneResponsavel) {
		this.numeroTelefoneResponsavel = numeroTelefoneResponsavel;
	}

	public String getNumeroNitResponsavel() {
		return numeroNitResponsavel;
	}

	public void setNumeroNitResponsavel(String numeroNitResponsavel) {
		this.numeroNitResponsavel = numeroNitResponsavel;
	}

	public String getEmailResponsavel() {
		return emailResponsavel;
	}

	public void setEmailResponsavel(String emailResponsavel) {
		this.emailResponsavel = emailResponsavel;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PorteEmpresa getPorteEmpresa() {
		return porteEmpresa;
	}

	public void setPorteEmpresa(PorteEmpresa porteEmpresa) {
		this.porteEmpresa = porteEmpresa;
	}

	public TipoEmpresa getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public Set<EmailRedeCredenciada> getEmailsRedeCredenciada() {
		return emailsRedeCredenciada;
	}

	public void setEmailsRedeCredenciada(Set<EmailRedeCredenciada> emailsRedeCredenciada) {
		this.emailsRedeCredenciada = emailsRedeCredenciada;
	}

	public Set<EnderecoRedeCredenciada> getEnderecosRedeCredenciada() {
		return enderecosRedeCredenciada;
	}

	public void setEnderecosRedeCredenciada(Set<EnderecoRedeCredenciada> enderecosRedeCredenciada) {
		this.enderecosRedeCredenciada = enderecosRedeCredenciada;
	}

	public Set<TelefoneRedeCredenciada> getTelefonesRedeCredenciada() {
		return telefonesRedeCredenciada;
	}

	public void setTelefonesRedeCredenciada(Set<TelefoneRedeCredenciada> telefonesRedeCredenciada) {
		this.telefonesRedeCredenciada = telefonesRedeCredenciada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cargoResponsavel == null) ? 0 : cargoResponsavel.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((dataDesligamento == null) ? 0 : dataDesligamento.hashCode());
		result = prime * result + ((emailResponsavel == null) ? 0 : emailResponsavel.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inscricaoEstadual == null) ? 0 : inscricaoEstadual.hashCode());
		result = prime * result + ((inscricaoMunicipal == null) ? 0 : inscricaoMunicipal.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((nomeResponsavel == null) ? 0 : nomeResponsavel.hashCode());
		result = prime * result + ((numeroCnpj == null) ? 0 : numeroCnpj.hashCode());
		result = prime * result + ((numeroNitResponsavel == null) ? 0 : numeroNitResponsavel.hashCode());
		result = prime * result + ((numeroTelefoneResponsavel == null) ? 0 : numeroTelefoneResponsavel.hashCode());
		result = prime * result + ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
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
		RedeCredenciada other = (RedeCredenciada) obj;
		if (cargoResponsavel == null) {
			if (other.cargoResponsavel != null)
				return false;
		} else if (!cargoResponsavel.equals(other.cargoResponsavel))
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
		if (dataDesligamento == null) {
			if (other.dataDesligamento != null)
				return false;
		} else if (!dataDesligamento.equals(other.dataDesligamento))
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
		if (nomeResponsavel == null) {
			if (other.nomeResponsavel != null)
				return false;
		} else if (!nomeResponsavel.equals(other.nomeResponsavel))
			return false;
		if (numeroCnpj == null) {
			if (other.numeroCnpj != null)
				return false;
		} else if (!numeroCnpj.equals(other.numeroCnpj))
			return false;
		if (numeroNitResponsavel == null) {
			if (other.numeroNitResponsavel != null)
				return false;
		} else if (!numeroNitResponsavel.equals(other.numeroNitResponsavel))
			return false;
		if (numeroTelefoneResponsavel == null) {
			if (other.numeroTelefoneResponsavel != null)
				return false;
		} else if (!numeroTelefoneResponsavel.equals(other.numeroTelefoneResponsavel))
			return false;
		if (razaoSocial == null) {
			if (other.razaoSocial != null)
				return false;
		} else if (!razaoSocial.equals(other.razaoSocial))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	public Date getDataDesligamento() {
		return dataDesligamento;
	}

	public void setDataDesligamento(Date dataDesligamento) {
		this.dataDesligamento = dataDesligamento;
	}

	public Set<RedeCredenciadaProdutoServico> getRedeCredenciadaProdutoServico() {
		return redeCredenciadaProdutoServico;
	}

	public void setRedeCredenciadaProdutoServico(Set<RedeCredenciadaProdutoServico> redeCredenciadaProdutoServico) {
		this.redeCredenciadaProdutoServico = redeCredenciadaProdutoServico;
	}
}
