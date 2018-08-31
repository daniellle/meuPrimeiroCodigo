package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class FilterBase {
	
	@QueryParam("pagina")
	@DefaultValue("1")
	private Integer pagina;

	@QueryParam("qtdRegistro")
	@DefaultValue("10")
	private Integer quantidadeRegistro;

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	public Integer getQuantidadeRegistro() {
		return quantidadeRegistro;
	}

	public void setQuantidadeRegistro(Integer quantidadeRegistro) {
		this.quantidadeRegistro = quantidadeRegistro;
	}

}
