package br.com.ezvida.rst.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="UAT_INSTALACAO_FISICA")
public class UatInstalacaoFisica extends AbstractData {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_UAT_INSTALACAO_FISICA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UAT_INSTALACAO_FISICA")
	@SequenceGenerator(name = "SEQUENCE_UAT_INSTALACAO_FISICA", sequenceName = "seq_uat_instalacao_fisica", allocationSize = 1)
	private Long id;
	
	private BigDecimal area;

	@Column(name="DT_ALTERACAO")
	private Timestamp dtAlteracao;

	@Column(name="DT_CRIACAO")
	private Timestamp dtCriacao;

	@Column(name="DT_EXCLUSAO")
	private Timestamp dtExclusao;

	@Column(name="ID_UND_ATD_TRABALHADOR")
	private Integer idUndAtdTrabalhador;

	private Integer quantidade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public Timestamp getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Timestamp dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	public Timestamp getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Timestamp dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Timestamp getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(Timestamp dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public Integer getIdUndAtdTrabalhador() {
		return idUndAtdTrabalhador;
	}

	public void setIdUndAtdTrabalhador(Integer idUndAtdTrabalhador) {
		this.idUndAtdTrabalhador = idUndAtdTrabalhador;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}
