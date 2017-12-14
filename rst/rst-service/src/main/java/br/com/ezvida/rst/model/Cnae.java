package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "cnae", uniqueConstraints = @UniqueConstraint(name = "pk_cnae", columnNames = {"id_cnae"}))
public class Cnae extends AbstractData {

	private static final long serialVersionUID = -5425902179770724732L;
	@Id
	@Column(name ="id_cnae")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_cnae")
	@SequenceGenerator(name = "sequence_cnae", sequenceName = "seq_cnae_id_cnae", allocationSize = 1)
	private Long id;
	
	@Column(name = "versao_cnae" )
	private String versao;
	
	@Column(name = "cd_cnae")
	private String codigo;
	
	@Column(name = "ds_cnae")
	private String descricao;
	
	@Column(name = "no_grau_de_risco")
	private Long numeroGrauDeRisco;
	
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

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versaoCnae) {
		this.versao = versaoCnae;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigoCnae) {
		this.codigo = codigoCnae;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricaoCnae) {
		this.descricao = descricaoCnae;
	}

	public Long getNumeroGrauDeRisco() {
		return numeroGrauDeRisco;
	}

	public void setNumeroGrauDeRisco(Long numeroGrauDeRisco) {
		this.numeroGrauDeRisco = numeroGrauDeRisco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((numeroGrauDeRisco == null) ? 0 : numeroGrauDeRisco.hashCode());
		result = prime * result + ((versao == null) ? 0 : versao.hashCode());
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
		
		Cnae other = (Cnae) obj;
		
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
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
		if (descricao == null) {
			if (other.descricao != null) {
				return false;
			}
		} else if (!descricao.equals(other.descricao)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (numeroGrauDeRisco == null) {
			if (other.numeroGrauDeRisco != null) {
				return false;
			}
		} else if (!numeroGrauDeRisco.equals(other.numeroGrauDeRisco)) {
			return false;
		}
		if (versao == null) {
			if (other.versao != null) {
				return false;
			}
		} else if (!versao.equals(other.versao)) {
			return false;
		}
		
		return true;
	}
}
