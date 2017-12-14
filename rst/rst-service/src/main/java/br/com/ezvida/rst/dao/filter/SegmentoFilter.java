package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class SegmentoFilter extends FilterBase {
	
	@QueryParam("codigo")
	private String codigo;

	@QueryParam("descricao")
	private String descricao;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
