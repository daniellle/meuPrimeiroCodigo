package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class TipoCursoFilter  extends FilterBase  {
	
	@QueryParam("descricao")
	private String descricao;
	
	@QueryParam("idTipoCurso")
	private String idFuncoes;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIdFuncoes() {
		return idFuncoes;
	}

	public void setIdFuncoes(String idFuncoes) {
		this.idFuncoes = idFuncoes;
	}
	
}
