package br.com.ezvida.rst.model;

import br.com.ezvida.rst.converter.*;
import br.com.ezvida.rst.enums.*;
import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "TRABALHADOR", uniqueConstraints = @UniqueConstraint(name = "PK_TRABALHADOR", columnNames = {
		"ID_TRABALHADOR" }))
public class Trabalhador extends AbstractData {

	private static final long serialVersionUID = -5714911305112739249L;

	@Id
	@Column(name = "ID_TRABALHADOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_TRABALHADOR")
	@SequenceGenerator(name = "SEQUENCE_TRABALHADOR", sequenceName = "seq_trabalhador_id_trabalhad", allocationSize = 1)
	private Long id;

	@Column(name = "NM_TRABALHADOR")
	private String nome;

	@Column(name = "NM_MAE")
	private String nomeMae;
	
	@Column(name = "NM_PAI")
	private String nomePai;
	
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_NASCIMENTO")
	private Date dataNascimento;

	@Column(name = "NO_CPF")
	private String cpf;

	@Column(name = "NO_RG")
	private String rg;

	@Column(name = "DS_ORGAO_RG")
	private String orgaoRg;

	@Convert(converter = GeneroConverter.class)
	@Column(name = "FL_GENERO")
	private Genero genero;

	@Convert(converter = EstadoCivilConverter.class)
	@Column(name = "FL_ESTADO_CIVIL")
	private EstadoCivil estadoCivil;

	@Convert(converter = BrPdhConverter.class)
	@Column(name = "FL_BR_PDH")
	private BrPdh brPdh;

	@Column(name = "NO_NIT")
	private String nit;

	@Column(name = "NO_CTPS")
	private String ctps;

	@Column(name = "NO_SERIE_CTPS")
	private String serieCtps;

	@Column(name = "DS_UF_CTPS")
	private String ufCtps;

	@Column(name = "FL_FAIXA_SALARIAL")
	@Convert(converter = FaixaSalarialConverter.class)
	private FaixaSalarial faixaSalarial;

	@Column(name = "FL_ESCOLARIDADE")
	@Convert(converter = EscolaridadeConverter.class)
	private Escolaridade escolaridade;

	@Column(name = "FL_RACA")
	@Convert(converter = RacaConverter.class)
	private Raca raca;

	@Convert(converter = TipoSanguineoConverter.class)
	@Column(name = "FL_TP_SANGUINEO")
	private TipoSanguineo tipoSanguineo;
	
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_FALECIMENTO")
	private Date dataFalecimento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO_FK", referencedColumnName = "ID_MUNICIPIO")
	private Municipio municipio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAIS_FK", referencedColumnName = "ID_PAIS")
	private Pais pais;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROFISSAO_FK")
	private Profissao profissao;
	
	@Column(name = "FL_PLANO_DE_SAUDE")
	@Convert(converter = SimNaoConverter.class)
	private SimNao planoSaude;
	
	@Column(name = "FL_AUTOMOVEL")
	@Convert(converter = SimNaoConverter.class)
	private SimNao automovel;
	
	@Column(name = "FL_ATIV_FISICA")
	@Convert(converter = SimNaoConverter.class)
	private SimNao atividadeFisica;
	
	@Column(name = "FL_EXAME_REGULAR")
	@Convert(converter = SimNaoConverter.class)
	private SimNao exameRegular;
	
	@Column(name = "FL_NOTIFICACAO")
	@Convert(converter = SimNaoConverter.class)
	private SimNao notificacao;
	
	@Column(name = "FL_SITUACAO_TRAB")
	@Convert(converter = SituacaoTrabalhadorConverter.class)
	private SituacaoTrabalhador situacaoTrabalhador;
	
	@Column(name = "FL_NACIONALIDADE")
	@Convert(converter = NacionalidadeConverter.class)
	private Nacionalidade nacionalidade;
	
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_NATURALIZACAO")
	private Date dataNaturalizacao;
	
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ENTRADA_PAIS")
	private Date dataEntradaPais;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trabalhador")
	private Set<TelefoneTrabalhador> listaTelefoneTrabalhador;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trabalhador")
	private Set<EmailTrabalhador> listaEmailTrabalhador;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trabalhador")
	private Set<EnderecoTrabalhador> listaEnderecoTrabalhador;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trabalhador")
	private Set<EmpresaTrabalhador> listaEmpresaTrabalhador;
	
	@Column(name = "fl_termo")
	@Convert(converter = SimNaoConverter.class)
	private SimNao termo;
	
	@Column(name = "imagem")
	@Type(type="org.hibernate.type.BinaryType")
	private byte[] imagem;
	
	@Column(name="ds_tp_imagem")
	private String tipoImagem;

	@Column(name="ds_medicamentos", length = 300)
	private String descricaoMedicamentos;

	@Column(name="ds_alergias", length = 300)
	private String descricaoAlergias;

	@Column(name="ds_vacinas", length = 300)
	private String descricaoVacinas;

	@Column(name="nome_social", length = 160)
	private String nomeSocial;


	public Trabalhador() {
		
	}
	
	public Trabalhador(Long id) {
		this.id = id;
	}

	public Trabalhador(Long id, String descricaoMedicamentos, String descricaoAlergias, String descricaoVacinas) {
        this.id = id;
        this.descricaoMedicamentos = descricaoMedicamentos;
        this.descricaoAlergias = descricaoAlergias;
        this.descricaoVacinas = descricaoVacinas;
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

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public String getNomeSocial(){return nomeSocial;}

	public void setNomeSocial(String nomeSocial) { this.nomeSocial = nomeSocial;}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getOrgaoRg() {
		return orgaoRg;
	}

	public void setOrgaoRg(String orgaoRg) {
		this.orgaoRg = orgaoRg;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public BrPdh getBrPdh() {
		return brPdh;
	}

	public void setBrPdh(BrPdh brPdh) {
		this.brPdh = brPdh;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
	}

	public String getSerieCtps() {
		return serieCtps;
	}

	public void setSerieCtps(String serieCtps) {
		this.serieCtps = serieCtps;
	}

	public String getUfCtps() {
		return ufCtps;
	}

	public void setUfCtps(String ufCtps) {
		this.ufCtps = ufCtps;
	}

	public FaixaSalarial getFaixaSalarial() {
		return faixaSalarial;
	}

	public void setFaixaSalarial(FaixaSalarial faixaSalarial) {
		this.faixaSalarial = faixaSalarial;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}

	public Date getDataFalecimento() {
		return dataFalecimento;
	}

	public void setDataFalecimento(Date dataFalecimento) {
		this.dataFalecimento = dataFalecimento;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public TipoSanguineo getTipoSanguineo() {
		return tipoSanguineo;
	}

	public void setTipoSanguineo(TipoSanguineo tipoSanguineo) {
		this.tipoSanguineo = tipoSanguineo;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public SimNao getPlanoSaude() {
		return planoSaude;
	}

	public void setPlanoSaude(SimNao planoSaude) {
		this.planoSaude = planoSaude;
	}

	public SimNao getAutomovel() {
		return automovel;
	}

	public void setAutomovel(SimNao automovel) {
		this.automovel = automovel;
	}

	public SimNao getAtividadeFisica() {
		return atividadeFisica;
	}

	public void setAtividadeFisica(SimNao atividadeFisica) {
		this.atividadeFisica = atividadeFisica;
	}

	public SimNao getExameRegular() {
		return exameRegular;
	}

	public void setExameRegular(SimNao exameRegular) {
		this.exameRegular = exameRegular;
	}

	public SimNao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(SimNao notificacao) {
		this.notificacao = notificacao;
	}

	public SituacaoTrabalhador getSituacaoTrabalhador() {
		return situacaoTrabalhador;
	}

	public void setSituacaoTrabalhador(SituacaoTrabalhador situacaoTrabalhador) {
		this.situacaoTrabalhador = situacaoTrabalhador;
	}

	public Nacionalidade getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(Nacionalidade nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public Date getDataNaturalizacao() {
		return dataNaturalizacao;
	}

	public void setDataNaturalizacao(Date dataNaturalizacao) {
		this.dataNaturalizacao = dataNaturalizacao;
	}

	public Date getDataEntradaPais() {
		return dataEntradaPais;
	}

	public void setDataEntradaPais(Date dataEntradaPais) {
		this.dataEntradaPais = dataEntradaPais;
	}

	public Set<TelefoneTrabalhador> getListaTelefoneTrabalhador() {
		return listaTelefoneTrabalhador;
	}

	public void setListaTelefoneTrabalhador(Set<TelefoneTrabalhador> listaTelefoneTrabalhador) {
		this.listaTelefoneTrabalhador = listaTelefoneTrabalhador;
	}

	public Set<EmailTrabalhador> getListaEmailTrabalhador() {
		return listaEmailTrabalhador;
	}

	public void setListaEmailTrabalhador(Set<EmailTrabalhador> listaEmailTrabalhador) {
		this.listaEmailTrabalhador = listaEmailTrabalhador;
	}

	public Set<EnderecoTrabalhador> getListaEnderecoTrabalhador() {
		return listaEnderecoTrabalhador;
	}

	public void setListaEnderecoTrabalhador(Set<EnderecoTrabalhador> listaEnderecoTrabalhador) {
		this.listaEnderecoTrabalhador = listaEnderecoTrabalhador;
	}
	
	
	public Set<EmpresaTrabalhador> getListaEmpresaTrabalhador() {
		return listaEmpresaTrabalhador;
	}

	public void setListaEmpresaTrabalhador(Set<EmpresaTrabalhador> listaEmpresaTrabalhador) {
		this.listaEmpresaTrabalhador = listaEmpresaTrabalhador;
	}
	
	public String getTipoImagem() {
		return tipoImagem;
	}

	public void setTipoImagem(String tipoImagem) {
		this.tipoImagem = tipoImagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((brPdh == null) ? 0 : brPdh.hashCode());
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((ctps == null) ? 0 : ctps.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((dataFalecimento == null) ? 0 : dataFalecimento.hashCode());
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((escolaridade == null) ? 0 : escolaridade.hashCode());
		result = prime * result + ((estadoCivil == null) ? 0 : estadoCivil.hashCode());
		result = prime * result + ((faixaSalarial == null) ? 0 : faixaSalarial.hashCode());
		result = prime * result + ((genero == null) ? 0 : genero.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((orgaoRg == null) ? 0 : orgaoRg.hashCode());
		result = prime * result + ((raca == null) ? 0 : raca.hashCode());
		result = prime * result + ((rg == null) ? 0 : rg.hashCode());
		result = prime * result + ((serieCtps == null) ? 0 : serieCtps.hashCode());
		result = prime * result + ((ufCtps == null) ? 0 : ufCtps.hashCode());
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
		Trabalhador other = (Trabalhador) obj;
		if (brPdh != other.brPdh)
			return false;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (ctps == null) {
			if (other.ctps != null)
				return false;
		} else if (!ctps.equals(other.ctps))
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
		if (dataFalecimento == null) {
			if (other.dataFalecimento != null)
				return false;
		} else if (!dataFalecimento.equals(other.dataFalecimento))
			return false;
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (escolaridade != other.escolaridade)
			return false;
		if (estadoCivil != other.estadoCivil)
			return false;
		if (faixaSalarial != other.faixaSalarial)
			return false;
		if (genero != other.genero)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (orgaoRg == null) {
			if (other.orgaoRg != null)
				return false;
		} else if (!orgaoRg.equals(other.orgaoRg))
			return false;
		if (raca != other.raca)
			return false;
		if (rg == null) {
			if (other.rg != null)
				return false;
		} else if (!rg.equals(other.rg))
			return false;
		if (serieCtps == null) {
			if (other.serieCtps != null)
				return false;
		} else if (!serieCtps.equals(other.serieCtps))
			return false;
		if (ufCtps == null) {
			if (other.ufCtps != null)
				return false;
		} else if (!ufCtps.equals(other.ufCtps))
			return false;
		return true;
	}

	public SimNao getTermo() {
		return termo;
	}

	public void setTermo(SimNao termo) {
		this.termo = termo;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public String getDescricaoMedicamentos() {
        return descricaoMedicamentos;
    }

    public void setDescricaoMedicamentos(String descricaoMedicamentos) {
        this.descricaoMedicamentos = descricaoMedicamentos;
    }

    public String getDescricaoAlergias() {
        return descricaoAlergias;
    }

    public void setDescricaoAlergias(String descricaoAlergias) {
        this.descricaoAlergias = descricaoAlergias;
    }

    public String getDescricaoVacinas() {
        return descricaoVacinas;
    }

    public void setDescricaoVacinas(String descricaoVacinas) {
        this.descricaoVacinas = descricaoVacinas;
    }
}