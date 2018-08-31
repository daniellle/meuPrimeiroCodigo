package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class PesquisaSesiFilter extends FilterBase {

	@QueryParam("idUnidadeSesi")
	private Long idUnidadeSesi;

	@QueryParam("idEstado")
	private Long idEstado;

	@QueryParam("idMunicipio")
	private Long idMunicipio;

	@QueryParam("bairro")
	private String bairro;

	@QueryParam("idsLinha")
	private String idsLinha;

	@QueryParam("idsProduto")
	private String idsProduto;

	@QueryParam("aplicarDadosFilter")
	private boolean aplicarDadosFilter;

	public Long getIdUnidadeSesi() {
		return idUnidadeSesi;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public String getBairro() {
		return bairro;
	}

	public String getIdsLinha() {
		return idsLinha;
	}

	public String getIdsProduto() {
		return idsProduto;
	}

}
