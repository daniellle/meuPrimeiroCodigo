package br.com.ezvida.rst.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "UAT_INSTALACAO_FISICA", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_INSTALACAO_FISICA", columnNames = {
"ID_UAT_INSTALACAO_FISICA" }))
public class UatInstalacaoFisica extends AbstractData {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_UAT_INSTALACAO_FISICA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UAT_INSTALACAO_FISICA")
	@SequenceGenerator(name = "SEQUENCE_UAT_INSTALACAO_FISICA", sequenceName = "SEQ_UAT_INSTALACAO_FISICA", allocationSize = 1)
	private Long id;
	
	private BigDecimal area;

	private Integer quantidade;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UAT_INSTALACAO_FISICA_AMBIENTE_FK", referencedColumnName = "ID_UAT_INSTALACAO_FISICA_AMBIENTE")
	private UatInstalacaoFisicaAmbiente uatInstalacaoFisicaAmbiente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK", referencedColumnName = "ID_UND_ATD_TRABALHADOR")
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public UatInstalacaoFisicaAmbiente getUatInstalacaoFisicaAmbiente() {
		return uatInstalacaoFisicaAmbiente;
	}

	public void setUatInstalacaoFisicaAmbiente(UatInstalacaoFisicaAmbiente uatInstalacaoFisicaAmbiente) {
		this.uatInstalacaoFisicaAmbiente = uatInstalacaoFisicaAmbiente;
	}

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}
}
