package br.com.ezvida.rst.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
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

import br.com.ezvida.rst.utils.DateJsonDeserializer;
import br.com.ezvida.rst.utils.DateJsonSerializer;

@Entity
@Table(name = "DEPARTAMENTO_REGIONAL", uniqueConstraints = @UniqueConstraint(name = "PK_DEPARTAMENTO_REGIONAL", columnNames = {
		"ID_DEPARTAMENTO_REGIONAL" }))
public class DepartamentoRegional extends AbstractData {

	private static final long serialVersionUID = 5457163610235658287L;

	@Id
	@Column(name = "ID_DEPARTAMENTO_REGIONAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_DEPARTAMENTO_REGIONAL")
	@SequenceGenerator(name = "SEQUENCE_DEPARTAMENTO_REGIONAL", sequenceName = "SEQ_DEPARTAMENTO__ID_DEPARTAME", allocationSize = 1)
	private Long id;

	@Column(name = "NO_CNPJ")
	private String cnpj;

	@Column(name = "DS_RAZAO_SOCIAL")
	private String razaoSocial;

	@Column(name = "DS_NOME_FANTASIA")
	private String nomeFantasia;

	@Column(name = "DS_NOME_RESPONSAVEL")
	private String nomeResponsavel;

	@Column(name = "SIGLA_DR")
	private String siglaDR;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_desativacao", nullable = true)
	private Date dataDesativacao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "departamentoRegional")
	private Set<TelefoneDepartamentoRegional> listaTelDepRegional;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "departamentoRegional")
	private Set<EmailDepartamentoRegional> listaEmailDepRegional;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "departamentoRegional")
	private Set<EnderecoDepartamentoRegional> listaEndDepRegional;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "departamentoRegional")
	private Set<UnidadeAtendimentoTrabalhador> unidadeAtendimentoTrabalhador;
	
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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getSiglaDR() {
		return siglaDR;
	}

	public void setSiglaDR(String siclaDR) {
		this.siglaDR = siclaDR;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	public Set<TelefoneDepartamentoRegional> getListaTelDepRegional() {
		return listaTelDepRegional;
	}

	public void setListaTelDepRegional(Set<TelefoneDepartamentoRegional> listaTelDepRegional) {
		this.listaTelDepRegional = listaTelDepRegional;
	}

	public Set<EmailDepartamentoRegional> getListaEmailDepRegional() {
		return listaEmailDepRegional;
	}

	public void setListaEmailDepRegional(Set<EmailDepartamentoRegional> listaEmailDepRegional) {
		this.listaEmailDepRegional = listaEmailDepRegional;
	}

	public Set<EnderecoDepartamentoRegional> getListaEndDepRegional() {
		return listaEndDepRegional;
	}

	public void setListaEndDepRegional(Set<EnderecoDepartamentoRegional> listaEndDepRegional) {
		this.listaEndDepRegional = listaEndDepRegional;
	}
	
	public Set<UnidadeAtendimentoTrabalhador> getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	public void setUnidadeAtendimentoTrabalhador(Set<UnidadeAtendimentoTrabalhador> unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((getDataDesativacao() == null) ? 0 : getDataDesativacao().hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((nomeResponsavel == null) ? 0 : nomeResponsavel.hashCode());
		result = prime * result + ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
		result = prime * result + ((siglaDR == null) ? 0 : siglaDR.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		DepartamentoRegional other = (DepartamentoRegional) obj;
		
		if (cnpj == null) {
			if (other.cnpj != null) {
				return false;
			}
		} else if (!cnpj.equals(other.cnpj)) {
			return false;
		}
		if (getDataAlteracao() == null) {
			if (other.getDataAlteracao() != null) {
				return false;
			}
		} else if (!getDataAlteracao().equals(other.getDataAlteracao())) {
			return false;
		}
		if (getDataCriacao() == null) {
			if (other.getDataCriacao() != null) {
				return false;
			}
		} else if (!getDataCriacao().equals(other.getDataCriacao())) {
			return false;
		}
		if (getDataExclusao() == null) {
			if (other.getDataExclusao() != null) {
				return false;
			}
		} else if (!getDataExclusao().equals(other.getDataExclusao())) {
			return false;
		}
		if (getDataDesativacao() == null) {
			if (other.getDataDesativacao() != null) {
				return false;
			}
		} else if (!getDataDesativacao().equals(other.getDataDesativacao())) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (nomeFantasia == null) {
			if (other.nomeFantasia != null) {
				return false;
			}
		} else if (!nomeFantasia.equals(other.nomeFantasia)) {
			return false;
		}
		if (nomeResponsavel == null) {
			if (other.nomeResponsavel != null) {
				return false;
			}
		} else if (!nomeResponsavel.equals(other.nomeResponsavel)) {
			return false;
		}
		if (razaoSocial == null) {
			if (other.razaoSocial != null) {
				return false;
			}
		} else if (!razaoSocial.equals(other.razaoSocial)) {
			return false;
		}
		if (siglaDR == null) {
			if (other.siglaDR != null) {
				return false;
			}
		} else if (!siglaDR.equals(other.siglaDR)) {
			return false;
		}
		
		return true;
	}

}
