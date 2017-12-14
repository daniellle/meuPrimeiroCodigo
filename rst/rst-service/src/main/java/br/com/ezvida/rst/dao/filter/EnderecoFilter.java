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

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
}
