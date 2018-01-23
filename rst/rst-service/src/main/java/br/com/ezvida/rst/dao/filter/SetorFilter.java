package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class SetorFilter extends FilterBase {

	@QueryParam("sigla")
	private String sigla;

	@QueryParam("descricao")
	private String descricao;

	@QueryParam("idEmpresa")
	private Long idEmpresa;

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}
}
