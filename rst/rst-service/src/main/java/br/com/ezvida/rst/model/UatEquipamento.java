package br.com.ezvida.rst.model;

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
@Table(name = "UAT_EQUIPAMENTO", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_EQUIPAMENTO", columnNames = {
"ID_UAT_EQUIPAMENTO" }))
public class UatEquipamento extends AbstractData {

	private static final long serialVersionUID = 5047994581651427365L;

	@Id
	@Column(name = "ID_UAT_EQUIPAMENTO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UAT_EQUIPAMENTO")
	@SequenceGenerator(name = "SEQUENCE_UAT_EQUIPAMENTO", sequenceName = "SEQ_UAT_EQUIPAMENTO", allocationSize = 1)
	private Long id;

	@Column(name = "QUANTIDADE", nullable = false)
	private Integer quantidade;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UAT_EQUIPAMENTO_TIPO_FK", referencedColumnName = "ID_UAT_EQUIPAMENTO_TIPO", nullable = false)
	private UatEquipamentoTipo uatEquipamentoTipo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK", referencedColumnName = "ID_UND_ATD_TRABALHADOR", nullable = false)
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public UatEquipamentoTipo getUatEquipamentoTipo() {
		return uatEquipamentoTipo;
	}

	public void setUatEquipamentoTipo(UatEquipamentoTipo uatEquipamentoTipo) {
		this.uatEquipamentoTipo = uatEquipamentoTipo;
	}

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}
}
