package br.com.ezvida.rst.model;

import java.util.Date;

import javax.persistence.Column;
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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "emp_lotacao", uniqueConstraints = @UniqueConstraint(name = "PK_emp_lotacao", columnNames = { "id_empresa_lotacao" }))
public class EmpresaLotacao extends AbstractData {

	private static final long serialVersionUID = -1360633938746178058L;

	@Id
	@Column(name = "id_empresa_lotacao")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_EMPRESA_LOTACAO")
	@SequenceGenerator(name = "SEQUENCE_EMPRESA_LOTACAO", sequenceName = "seq_emp_lotacao_id_empresa_l", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_emp_setor_fk", referencedColumnName = "id_emp_setor")
	private EmpresaSetor empresaSetor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_emp_cbo_fk", referencedColumnName = "id_emp_cbo")
	private EmpresaCbo empresaCbo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_emp_funcao_fk", referencedColumnName = "id_emp_funcao")
	private EmpresaFuncao empresaFuncao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_emp_jornada_fk", referencedColumnName = "id_emp_jornada")
	private EmpresaJornada empresaJornada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_und_obra_fk", referencedColumnName = "ID_UND_OBRA")
	private UnidadeObra unidadeObra;

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
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public EmpresaSetor getEmpresaSetor() {
		return empresaSetor;
	}

	public void setEmpresaSetor(EmpresaSetor empresaSetor) {
		this.empresaSetor = empresaSetor;
	}

	public EmpresaCbo getEmpresaCbo() {
		return empresaCbo;
	}

	public void setEmpresaCbo(EmpresaCbo empresaCbo) {
		this.empresaCbo = empresaCbo;
	}

	public EmpresaFuncao getEmpresaFuncao() {
		return empresaFuncao;
	}

	public void setEmpresaFuncao(EmpresaFuncao empresaFuncao) {
		this.empresaFuncao = empresaFuncao;
	}

	public EmpresaJornada getEmpresaJornada() {
		return empresaJornada;
	}

	public void setEmpresaJornada(EmpresaJornada empresaJornada) {
		this.empresaJornada = empresaJornada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
		result = prime * result + ((empresaCbo == null) ? 0 : empresaCbo.hashCode());
		result = prime * result + ((empresaFuncao == null) ? 0 : empresaFuncao.hashCode());
		result = prime * result + ((empresaJornada == null) ? 0 : empresaJornada.hashCode());
		result = prime * result + ((empresaSetor == null) ? 0 : empresaSetor.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		EmpresaLotacao other = (EmpresaLotacao) obj;
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
		if (empresaCbo == null) {
			if (other.empresaCbo != null)
				return false;
		} else if (!empresaCbo.equals(other.empresaCbo))
			return false;
		if (empresaFuncao == null) {
			if (other.empresaFuncao != null)
				return false;
		} else if (!empresaFuncao.equals(other.empresaFuncao))
			return false;
		if (empresaJornada == null) {
			if (other.empresaJornada != null)
				return false;
		} else if (!empresaJornada.equals(other.empresaJornada))
			return false;
		if (empresaSetor == null) {
			if (other.empresaSetor != null)
				return false;
		} else if (!empresaSetor.equals(other.empresaSetor))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public UnidadeObra getUnidadeObra() {
		return unidadeObra;
	}

	public void setUnidadeObra(UnidadeObra unidadeObra) {
		this.unidadeObra = unidadeObra;
	}
}
