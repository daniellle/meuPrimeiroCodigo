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
@Table(name = "UND_ATD_TRABALHADOR", uniqueConstraints = @UniqueConstraint(name = "PK_CENTRO_ATEND_TRAB", columnNames = {
		"ID_UND_ATD_TRABALHADOR" }))
public class UnidadeAtendimentoTrabalhador extends AbstractData {

	private static final long serialVersionUID = 7768787938478831998L;

	@Id
	@Column(name = "ID_UND_ATD_TRABALHADOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UND_ATEND_TRAB")
	@SequenceGenerator(name = "SEQUENCE_UND_ATEND_TRAB", sequenceName = "SEQ_UND_ATD_TRABA_ID_UND_ATD_T", allocationSize = 1)
	private Long id;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_DESATIVACAO", nullable = true)
	private Date dataDesativacao;
	
	@Column(name = "DS_RAZAO_SOCIAL", nullable = false)
	private String razaoSocial;

	@Column(name = "NM_FANTASIA", nullable = false)
	private String nomeFantasia;

	@Column(name = "NO_CNPJ")
	private String cnpj;

	@Column(name = "NM_RESPONSAVEL")
	private String nomeResponsavel;

	@OneToMany(mappedBy = "unidadeAtendimentoTrabalhador", fetch = FetchType.LAZY)
	private Set<EmailUnidadeAtendimentoTrabalhador> email;

	@OneToMany(mappedBy = "unidadeAtendimentoTrabalhador", fetch = FetchType.LAZY)
	private Set<EnderecoUnidadeAtendimentoTrabalhador> endereco;

	@OneToMany(mappedBy = "unidadeAtendimentoTrabalhador", fetch = FetchType.LAZY)
	private Set<TelefoneUnidadeAtendimentoTrabalhador> telefone;

	@OneToMany(mappedBy = "unidadeAtendimentoTrabalhador", fetch = FetchType.LAZY)
	private Set<UnidadeAtendimentoTrabalhadorProfissional> profissional;
	
	@OneToMany(mappedBy = "unidadeAtendimentoTrabalhador", fetch = FetchType.LAZY)
	private Set<EmpresaUnidadeAtendimentoTrabalhador> empresaUats;
	
	@OneToMany(mappedBy = "uat", fetch = FetchType.LAZY)
	private Set<UnidadeAtendimentoTrabalhadorProdutoServico> uatProdutoServico;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DEPARTAMENTO_REGIONAL_FK")
	private DepartamentoRegional departamentoRegional;

	public UnidadeAtendimentoTrabalhador() {
		// construtor padrao
	}

	public UnidadeAtendimentoTrabalhador(String cnpj, String razaoSocial, String cnpjDR, String razaoSocialDR, String siglaDR, String estadoDR) {
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.departamentoRegional = new DepartamentoRegional(cnpjDR, razaoSocialDR, siglaDR, estadoDR);
	}

	@PreUpdate
	public void preUpdate() {
		setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		setDataCriacao(new Date());
	}
	
	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DepartamentoRegional getDepartamentoRegional() {
		return departamentoRegional;
	}

	public void setDepartamentoRegional(DepartamentoRegional departamentoRegional) {
		this.departamentoRegional = departamentoRegional;
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

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
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

	public Set<EmailUnidadeAtendimentoTrabalhador> getEmail() {
		return email;
	}

	public void setEmail(Set<EmailUnidadeAtendimentoTrabalhador> email) {
		this.email = email;
	}

	public Set<EnderecoUnidadeAtendimentoTrabalhador> getEndereco() {
		return endereco;
	}

	public void setEndereco(Set<EnderecoUnidadeAtendimentoTrabalhador> endereco) {
		this.endereco = endereco;
	}

	public Set<TelefoneUnidadeAtendimentoTrabalhador> getTelefone() {
		return telefone;
	}

	public void setTelefone(Set<TelefoneUnidadeAtendimentoTrabalhador> telefone) {
		this.telefone = telefone;
	}

	public Set<UnidadeAtendimentoTrabalhadorProfissional> getProfissional() {
		return profissional;
	}

	public void setProfissional(Set<UnidadeAtendimentoTrabalhadorProfissional> profissional) {
		this.profissional = profissional;
	}

	public Set<EmpresaUnidadeAtendimentoTrabalhador> getEmpresaUats() {
		return empresaUats;
	}

	public void setEmpresaUats(Set<EmpresaUnidadeAtendimentoTrabalhador> empresaUats) {
		this.empresaUats = empresaUats;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((dataDesativacao == null) ? 0 : dataDesativacao.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((nomeResponsavel == null) ? 0 : nomeResponsavel.hashCode());
		result = prime * result + ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
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
		UnidadeAtendimentoTrabalhador other = (UnidadeAtendimentoTrabalhador) obj;
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
		if (razaoSocial == null) {
			if (other.razaoSocial != null)
				return false;
		} else if (!razaoSocial.equals(other.razaoSocial))
			return false;
		return true;
	}

	public Set<UnidadeAtendimentoTrabalhadorProdutoServico> getUatProdutoServico() {
		return uatProdutoServico;
	}

	public void setUatProdutoServico(Set<UnidadeAtendimentoTrabalhadorProdutoServico> uatProdutoServico) {
		this.uatProdutoServico = uatProdutoServico;
	}

}
