package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class CnaeFilter extends FilterBase {

	@QueryParam("versao")
	private String versao;

	@QueryParam("descricao")
	private String descricao;

	@QueryParam("codigo")
	private String codigo;

	@QueryParam("idsCnae")
	private String idsCnae;

	public String getVersao() {
		return versao;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getIdsCnae() {
		return idsCnae;
	}

}
