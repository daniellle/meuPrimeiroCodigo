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
@Table(name = "UAT_QUADRO_PESSOAL", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_INSTALACAO_FISICA_CATEGORIA", columnNames = {
		"ID_UAT_QUADRO_PESSOAL" }))
public class UatQuadroPessoal extends AbstractData {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_UAT_QUADRO_PESSOAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_UAT_QUADRO_PESSOAL")
	@SequenceGenerator(name = "SEQUENCE_UAT_QUADRO_PESSOAL", sequenceName = "SEQ_UAT_QUADRO_PESSOAL", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UAT_QUADRO_PESSOAL_TIPO_PROFISSIONAL_FK", referencedColumnName = "ID_UAT_QUADRO_PESSOAL_TIPO_PROFISSIONAL")
	private UatQuadroPessoalTipoProfissional uatQuadroPessoalTipoProfissional;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UND_ATD_TRABALHADOR_FK", referencedColumnName = "ID_UND_ATD_TRABALHADOR")
	private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

	private Integer quantidade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UatQuadroPessoalTipoProfissional getUatQuadroPessoalTipoProfissional() {
		return uatQuadroPessoalTipoProfissional;
	}

	public void setUatQuadroPessoalTipoProfissional(UatQuadroPessoalTipoProfissional uatQuadroPessoalTipoProfissional) {
		this.uatQuadroPessoalTipoProfissional = uatQuadroPessoalTipoProfissional;
	}

	public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
		return unidadeAtendimentoTrabalhador;
	}

	public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
		this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}
