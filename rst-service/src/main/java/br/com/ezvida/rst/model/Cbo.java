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
@Table(name = "cbo", uniqueConstraints = @UniqueConstraint(name = "PK_cbo", columnNames = { "id_cbo" }))
public class Cbo extends AbstractData {

	private static final long serialVersionUID = 8605171119576005599L;

	@Id
	@Column(name = "id_cbo")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_CBO")
	@SequenceGenerator(name = "SEQUENCE_CBO", sequenceName = "seq_cbo_id_cbo", allocationSize = 1)
	private Long id;
	
	@Column(name = "cd_cbo", nullable = false, length = 10)
	private String codigo;
	
	@Column(name = "ds_cbo", nullable = false, length = 80)
	private String descricao;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
