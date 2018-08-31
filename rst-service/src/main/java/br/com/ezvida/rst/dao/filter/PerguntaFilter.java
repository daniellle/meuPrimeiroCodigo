package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class PerguntaFilter extends FilterBase {

	@QueryParam("descricao")
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
