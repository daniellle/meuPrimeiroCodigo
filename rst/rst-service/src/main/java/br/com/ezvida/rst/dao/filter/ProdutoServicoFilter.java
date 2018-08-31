package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class ProdutoServicoFilter extends FilterBase {

	@QueryParam("id")
	private Long id;

	@QueryParam("nome")
	private String nome;

	@QueryParam("idLinha")
	private Long idLinha;

	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Long getIdLinha() {
		return idLinha;
	}

	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

}
