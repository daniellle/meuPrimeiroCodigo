package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class EnderecoFilter extends FilterBase {

	@QueryParam("idEstado")
	private Long idEstado;

	@QueryParam("idMunicipio")
	private Long idMunicipio;

	@QueryParam("bairro")
	private String bairro;

	public String getBairro() {
		return bairro;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

}
