package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoOperacaoAuditoria {

	//@formatter:off 
	INCLUSAO("1", "Inclusão", "I"),
	ALTERACAO("2", "Alteração", "A"),
	DESATIVACAO("3", "Exclusão", "E"),
	CONSULTA("4", "Consulta", "C"),
	TODOS("5", "Todos", "TDS");
	//@formatter:on

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private TipoOperacaoAuditoria(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static TipoOperacaoAuditoria getTipoOperacaoAuditoria(String codigo) {
		for (TipoOperacaoAuditoria enumType : TipoOperacaoAuditoria.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonCreator
	public static TipoOperacaoAuditoria getTipoOperacaoAuditoriaJson(String codigoJson) {
		for (TipoOperacaoAuditoria enumType : TipoOperacaoAuditoria.values()) {
			if (enumType.codigoJson.equals(codigoJson)) {
				return enumType;
			}
		}
		return null;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public String getNome() {
		return nome;
	}

	@JsonValue
	public String getCodigoJson() {
		return codigoJson;
	}
}
