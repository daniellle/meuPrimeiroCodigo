package br.com.ezvida.rst.auditoria.model;

import org.jboss.logging.MDC;

import br.com.ezvida.rst.enums.Funcionalidade;
import br.com.ezvida.rst.enums.TipoOperacaoAuditoria;

public class ClienteAuditoria {

	private String navegador;
	private String usuario;
	private String descricao;
	private TipoOperacaoAuditoria tipoOperacao;
	private Funcionalidade funcionalidade;
	
	public ClienteAuditoria() {
		MDC.put("appName", "rst");
	}

	public String getNavegador() {
		return navegador;
	}

	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoOperacaoAuditoria getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(TipoOperacaoAuditoria tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public Funcionalidade getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

}
