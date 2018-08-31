package br.com.ezvida.rst.dao.filter;

import javax.ws.rs.QueryParam;

public class AuditoriaFilter extends FilterBase {

	@QueryParam("dataInicial")
	private String dataInicial;

	@QueryParam("dataFinal")
	private String dataFinal;

	@QueryParam("usuario")
	private String usuario;
	
	@QueryParam("funcionalidade")
	private String funcionalidade;
	
	@QueryParam("tipoOperacaoAuditoria")
	private String tipoOperacaoAuditoria;

	public String getUsuario() {
		return usuario;
	}

	public String getFuncionalidade() {
		return funcionalidade;
	}

	public String getTipoOperacaoAuditoria() {
		return tipoOperacaoAuditoria;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setFuncionalidade(String funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public void setTipoOperacaoAuditoria(String tipoOperacaoAuditoria) {
		this.tipoOperacaoAuditoria = tipoOperacaoAuditoria;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	
	
	
	
	
	
	
}
