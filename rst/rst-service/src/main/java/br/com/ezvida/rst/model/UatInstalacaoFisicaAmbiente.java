package br.com.ezvida.rst.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "UAT_INSTALACAO_FISICA_AMBIENTE", uniqueConstraints = @UniqueConstraint(name = "PK_UAT_INSTALACAO_FISICA_AMBIENTE", columnNames = {
"ID_UAT_INSTALACAO_FISICA_AMBIENTE" }))
public class UatInstalacaoFisicaAmbiente extends AbstractData  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_UAT_INSTALACAO_FISICA_AMBIENTE")
	private Long id;

	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UAT_INSTALACAO_FISICA_CATEGORIA_FK", referencedColumnName = "ID_UAT_INSTALACAO_FISICA_CATEGORIA")
	private UatInstalacaoFisicaCategoria instalacaoFisicaCategoria;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public UatInstalacaoFisicaCategoria getInstalacaoFisicaCategoria() {
		return instalacaoFisicaCategoria;
	}

	public void setInstalacaoFisicaCategoria(UatInstalacaoFisicaCategoria instalacaoFisicaCategoria) {
		this.instalacaoFisicaCategoria = instalacaoFisicaCategoria;
	}
}
