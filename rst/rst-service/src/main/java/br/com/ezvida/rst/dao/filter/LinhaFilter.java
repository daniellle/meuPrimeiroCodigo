package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class LinhaFilter extends FilterBase {
	
	@QueryParam("id")
	private Long id;
	
	@QueryParam("descricao")
	private String desricao;

	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDesricao() {
		return desricao;
	}

	public void setDesricao(String desricao) {
		this.desricao = desricao;
	}
	
	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

	public void setAplicarDadosFilter(boolean aplicarDadosFilter) {
		this.aplicarDadosFilter = aplicarDadosFilter;
	}

}
