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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.ezvida.rst.converter.GeneroConverter;
import br.com.ezvida.rst.enums.Genero;
import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "parceiro", uniqueConstraints = @UniqueConstraint(name = "pk_parceiro", columnNames = { "id_parceiro" }))
public class Parceiro extends AbstractData {

	private static final long serialVersionUID = 2957235901253349183L;

	@Id
	@Column(name = "id_parceiro")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PARCEIRO")
	@SequenceGenerator(name = "SEQUENCE_PARCEIRO", sequenceName = "seq_parceiro_id_parceiro", allocationSize = 1)
	private Long id;

	@Column(name = "no_cnpj_cpf")
	private String numeroCnpjCpf;

	@Column(name = "ds_nome")
	private String nome;

	@Column(name = "nm_fantasia")
	private String nomeFantasia;

	@Column(name = "no_insc_municipal")
	private String inscricaoMunicipal;

	@Column(name = "no_insc_estadual")
	private String inscricaoEstadual;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_nascimento")
	private Date dataNascimento;

	@Convert(converter = GeneroConverter.class)
	@Column(name = "fl_genero")
	private Genero genero;

	@Column(name = "no_nit", length = 11)
	private String numeroNit;

	@Column(name = "ds_url_parceiro", length = 70)
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

	@Column(name = "nm_contato")
	private String nomeContato;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_desligamento")
	private Date dataDesligamento;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parceiro")
	private Set<ParceiroEspecialidade> parceiroEspecialidades;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parceiro")
	private Set<EmailParceiro> emailsParceiro;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parceiro")
	private Set<EnderecoParceiro> enderecosParceiro;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parceiro")
	private Set<TelefoneParceiro> telefonesParceiro;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parceiro")
	private Set<ParceiroProdutoServico> parceiroProdutoServicos;

	public Parceiro() {
		
	}
	
	public Parceiro(Long id) {
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

	public String getNumeroCnpjCpf() {
		return numeroCnpjCpf;
	}

	public void setNumeroCnpjCpf(String numeroCnpjCpf) {
		this.numeroCnpjCpf = numeroCnpjCpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantansia) {
		this.nomeFantasia = nomeFantansia;
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

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public String getNumeroNit() {
		return numeroNit;
	}

	public void setNumeroNit(String numeroNit) {
		this.numeroNit = numeroNit;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
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

	public Set<ParceiroEspecialidade> getParceiroEspecialidades() {
		return parceiroEspecialidades;
	}

	public void setParceiroEspecialidades(Set<ParceiroEspecialidade> parceiroEspecialidades) {
		this.parceiroEspecialidades = parceiroEspecialidades;
	}

	public Set<EmailParceiro> getEmailsParceiro() {
		return emailsParceiro;
	}

	public void setEmailsParceiro(Set<EmailParceiro> emailsParceiro) {
		this.emailsParceiro = emailsParceiro;
	}

	public Set<EnderecoParceiro> getEnderecosParceiro() {
		return enderecosParceiro;
	}

	public void setEnderecosParceiro(Set<EnderecoParceiro> enderecosParceiro) {
		this.enderecosParceiro = enderecosParceiro;
	}

	public Set<TelefoneParceiro> getTelefonesParceiro() {
		return telefonesParceiro;
	}

	public void setTelefonesParceiro(Set<TelefoneParceiro> telefonesParceiro) {
		this.telefonesParceiro = telefonesParceiro;
	}

	public Set<ParceiroProdutoServico> getParceiroProdutoServicos() {
		return parceiroProdutoServicos;
	}

	public void setParceiroProdutoServicos(Set<ParceiroProdutoServico> parceiroProdutoServicos) {
		this.parceiroProdutoServicos = parceiroProdutoServicos;
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
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((emailResponsavel == null) ? 0 : emailResponsavel.hashCode());
		result = prime * result + ((genero == null) ? 0 : genero.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inscricaoEstadual == null) ? 0 : inscricaoEstadual.hashCode());
		result = prime * result + ((inscricaoMunicipal == null) ? 0 : inscricaoMunicipal.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((nomeContato == null) ? 0 : nomeContato.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((nomeResponsavel == null) ? 0 : nomeResponsavel.hashCode());
		result = prime * result + ((numeroCnpjCpf == null) ? 0 : numeroCnpjCpf.hashCode());
		result = prime * result + ((numeroNit == null) ? 0 : numeroNit.hashCode());
		result = prime * result + ((numeroNitResponsavel == null) ? 0 : numeroNitResponsavel.hashCode());
		result = prime * result + ((numeroTelefoneResponsavel == null) ? 0 : numeroTelefoneResponsavel.hashCode());
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
		Parceiro other = (Parceiro) obj;
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
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (emailResponsavel == null) {
			if (other.emailResponsavel != null)
				return false;
		} else if (!emailResponsavel.equals(other.emailResponsavel))
			return false;
		if (genero != other.genero)
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
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (nomeContato == null) {
			if (other.nomeContato != null)
				return false;
		} else if (!nomeContato.equals(other.nomeContato))
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
		if (numeroCnpjCpf == null) {
			if (other.numeroCnpjCpf != null)
				return false;
		} else if (!numeroCnpjCpf.equals(other.numeroCnpjCpf))
			return false;
		if (numeroNit == null) {
			if (other.numeroNit != null)
				return false;
		} else if (!numeroNit.equals(other.numeroNit))
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
}
