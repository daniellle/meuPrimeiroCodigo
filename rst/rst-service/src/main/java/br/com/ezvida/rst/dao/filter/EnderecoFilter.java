package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class EnderecoFilter extends FilterBase {

	@QueryParam("idEstado")
	private Long idEstado;

	@QueryParam("idMunicipio")
	private Long idMunicipio;

	@QueryParam("bairro")
	private String bairro;

	@QueryParam("filtrarDepRegEmp")
	private String filtrarDepRegEmp;

	public String getBairro() {
		return bairro;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public String getFiltrarDepRegEmp() {
		return filtrarDepRegEmp;
	}

	public void setFiltrarDepRegEmp(String filtrarDepRegEmp) {
		this.filtrarDepRegEmp = filtrarDepRegEmp;
	}
}
