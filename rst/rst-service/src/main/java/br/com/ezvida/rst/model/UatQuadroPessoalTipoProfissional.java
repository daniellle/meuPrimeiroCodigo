package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fw.core.model.BaseEntity;

@Entity
@Table(name = "UAT_QUADRO_PESSOAL_TIPO_PROFISSIONAL", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_QUADRO_PESSOAL_TIPO_PROFISSIONAL", columnNames = {
"ID_UAT_QUADRO_PESSOAL_TIPO_PROFISSIONAL" }))
public class UatQuadroPessoalTipoProfissional extends BaseEntity<Long>  {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_UAT_QUADRO_PESSOAL_TIPO_PROFISSIONAL")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UAT_QUADRO_PESSOAL_TIPO_SERVICO_FK", referencedColumnName = "ID_UAT_QUADRO_PESSOAL_TIPO_SERVICO", nullable = false)
	private UatQuadroPessoalTipoServico uatQuadroPessoalTipoServico;
	
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UatQuadroPessoalTipoServico getUatQuadroPessoalTipoServico() {
		return uatQuadroPessoalTipoServico;
	}

	public void setUatQuadroPessoalTipoServico(UatQuadroPessoalTipoServico uatQuadroPessoalTipoServico) {
		this.uatQuadroPessoalTipoServico = uatQuadroPessoalTipoServico;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
