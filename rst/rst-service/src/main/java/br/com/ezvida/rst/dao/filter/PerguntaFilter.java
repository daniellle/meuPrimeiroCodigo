package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class PerguntaFilter  extends FilterBase {
	
	@QueryParam("id")
	private Long id;
	
	@QueryParam("descricao")
	private String descricao;

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
	
	

}
