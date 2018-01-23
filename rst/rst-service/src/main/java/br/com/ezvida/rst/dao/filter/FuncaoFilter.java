package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class FuncaoFilter extends FilterBase {

	@QueryParam("codigo")
	private String codigo;

	@QueryParam("descricao")
	private String descricao;

	@QueryParam("idFuncao")
	private String idFuncoes;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

}
