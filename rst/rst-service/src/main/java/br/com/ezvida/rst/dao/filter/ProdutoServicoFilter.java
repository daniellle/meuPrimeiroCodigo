package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class ProdutoServicoFilter extends FilterBase {

	@QueryParam("id")
	private Long id;

	@QueryParam("nome")
	private String nome;

	@QueryParam("descricao")
	private String descricao;

	@QueryParam("idLinha")
	private Long idLinha;

	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getIdLinha() {
		return idLinha;
	}

	public void setIdLinha(Long idLinha) {
		this.idLinha = idLinha;
	}

	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

	public void setAplicarDadosFilter(boolean aplicarDadosFilter) {
		this.aplicarDadosFilter = aplicarDadosFilter;
	}
	
}
