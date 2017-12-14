package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class RespostaFilter extends FilterBase {

	@QueryParam("descricao")
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
