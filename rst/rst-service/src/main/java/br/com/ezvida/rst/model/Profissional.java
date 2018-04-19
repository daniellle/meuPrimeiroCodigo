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
import br.com.ezvida.rst.converter.TipoProfissionalConverter;
import br.com.ezvida.rst.enums.Genero;
import br.com.ezvida.rst.enums.TipoProfissional;
import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "PROFISSIONAL", uniqueConstraints = @UniqueConstraint(name = "PROFISSIONAL_PK", columnNames = { "ID_PROFISSIONAL" }))
public class Profissional extends AbstractData {

	private static final long serialVersionUID = 9133959931296292316L;

	@Id
	@Column(name = "ID_PROFISSIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_PROFISSIONAL")
	@SequenceGenerator(name = "SEQUENCE_PROFISSIONAL", sequenceName = "SEQ_PROFISSIONAL_ID_PROFISSIO", allocationSize = 1)
	private Long id;

	@Column(name = "NM_PROFISSIONAL")
	private String nome;

	@Column(name = "CD_REG_PROFISSIONAL")
	private String registro;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_NASCIMENTO")
	private Date dataNascimento;

	@Column(name = "FL_GENERO")
	@Convert(converter = GeneroConverter.class)
	private Genero genero;

	@Column(name = "NO_RG")
	private String rg;

	@Column(name = "NO_CPF")
	private String cpf;

	@Column(name = "NO_NIT")
	private String nit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ESTADO_FK", referencedColumnName = "ID_ESTADO")
	private Estado estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CONSELHO_REGIONAL_FK", referencedColumnName = "ID_CONSELHO_REGIONAL")
	private ConselhoRegional conselhoRegional;

	@Column(name = "FL_TIPO_PROFISSIONAL", nullable = true)
	@Convert(converter = TipoProfissionalConverter.class)
	private TipoProfissional tipoProfissional;

	@OneToMany(mappedBy = "profissional", fetch = FetchType.LAZY)
	private Set<EnderecoProfissional> listaEnderecoProfissional;

	@OneToMany(mappedBy = "profissional", fetch = FetchType.LAZY)
	private Set<EmailProfissional> listaEmailProfissional;

	@OneToMany(mappedBy = "profissional", fetch = FetchType.LAZY)
	private Set<TelefoneProfissional> listaTelefoneProfissional;

	@OneToMany(mappedBy = "profissional", fetch = FetchType.LAZY)
	private Set<ProfissionalEspecialidade> listaProfissionalEspecialidade;

	@OneToMany(mappedBy = "profissional", fetch = FetchType.LAZY)
	private Set<UnidadeAtendimentoTrabalhadorProfissional> listaUnidadeAtendimentoTrabalhadorProfissional;

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

	public String getNome() {
		return nome;
	}

	public String getRegistro() {
		return registro;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public Genero getGenero() {
		return genero;
	}

	public String getRg() {
		return rg;
	}

	public String getCpf() {
		return cpf;
	}

	public String getNit() {
		return nit;
	}

	public TipoProfissional getTipoProfissional() {
		return tipoProfissional;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public void setTipoProfissional(TipoProfissional tipoProfissional) {
		this.tipoProfissional = tipoProfissional;
	}

	public Set<EmailProfissional> getListaEmailProfissional() {
		return listaEmailProfissional;
	}

	public void setListaEmailProfissional(Set<EmailProfissional> listaEmailProfissional) {
		this.listaEmailProfissional = listaEmailProfissional;
	}

	public Set<TelefoneProfissional> getListaTelefoneProfissional() {
		return listaTelefoneProfissional;
	}

	public void setListaTelefoneProfissional(Set<TelefoneProfissional> listaTelefoneProfissional) {
		this.listaTelefoneProfissional = listaTelefoneProfissional;
	}

	public Set<EnderecoProfissional> getListaEnderecoProfissional() {
		return listaEnderecoProfissional;
	}

	public void setListaEnderecoProfissional(Set<EnderecoProfissional> listaEnderecoProfissional) {
		this.listaEnderecoProfissional = listaEnderecoProfissional;
	}

	public Set<UnidadeAtendimentoTrabalhadorProfissional> getListaUnidadeAtendimentoTrabalhadorProfissional() {
		return listaUnidadeAtendimentoTrabalhadorProfissional;
	}

	public void setListaUnidadeAtendimentoTrabalhadorProfissional(
			Set<UnidadeAtendimentoTrabalhadorProfissional> listaUnidadeAtendimentoTrabalhadorProfissional) {
		this.listaUnidadeAtendimentoTrabalhadorProfissional = listaUnidadeAtendimentoTrabalhadorProfissional;
	}

	public Set<ProfissionalEspecialidade> getListaProfissionalEspecialidade() {
		return listaProfissionalEspecialidade;
	}

	public void setListaProfissionalEspecialidade(Set<ProfissionalEspecialidade> listaProfissionalEspecialidade) {
		this.listaProfissionalEspecialidade = listaProfissionalEspecialidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public ConselhoRegional getConselhoRegional() {
		return conselhoRegional;
	}

	public void setConselhoRegional(ConselhoRegional conselhoRegional) {
		this.conselhoRegional = conselhoRegional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((registro == null) ? 0 : registro.hashCode());
		result = prime * result + ((rg == null) ? 0 : rg.hashCode());
		result = prime * result + ((tipoProfissional == null) ? 0 : tipoProfissional.hashCode());
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
		Profissional other = (Profissional) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
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
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
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
		if (registro == null) {
			if (other.registro != null)
				return false;
		} else if (!registro.equals(other.registro))
			return false;
		if (rg == null) {
			if (other.rg != null)
				return false;
		} else if (!rg.equals(other.rg))
			return false;
		if (tipoProfissional != other.tipoProfissional)
			return false;
		return true;
	}

}
