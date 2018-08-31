package br.com.ezvida.rst.model.dto;

import java.util.List;

public class LinhaDTO {

	private String descricao;

	private List<ProdutoServicoDTO> produtosServicos;

	public LinhaDTO() {

	}

	public LinhaDTO(List<ProdutoServicoDTO> produtosServicos) {
		this.produtosServicos = produtosServicos;
	}

	public List<ProdutoServicoDTO> getProdutosServicos() {
		return produtosServicos;
	}

	public void setProdutosServicos(List<ProdutoServicoDTO> produtosServicos) {
		this.produtosServicos = produtosServicos;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
