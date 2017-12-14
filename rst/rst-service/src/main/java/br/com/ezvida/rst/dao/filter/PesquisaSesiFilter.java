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

	public void setIdUnidadeSesi(Long idUnidadeSesi) {
		this.idUnidadeSesi = idUnidadeSesi;
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

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public boolean isAplicarDadosFilter() {
		return aplicarDadosFilter;
	}

	public void setAplicarDadosFilter(boolean aplicarDadosFilter) {
		this.aplicarDadosFilter = aplicarDadosFilter;
	}

	public String getIdsLinha() {
		return idsLinha;
	}

	public void setIdsLinha(String idsLinha) {
		this.idsLinha = idsLinha;
	}

	public String getIdsProduto() {
		return idsProduto;
	}

	public void setIdsProduto(String idsProduto) {
		this.idsProduto = idsProduto;
	}

}
