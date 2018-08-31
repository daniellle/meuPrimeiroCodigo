package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Situacao {

	TODOS("undefined", ""), ATIVO("A", " is null "), INATIVO("I", " is not null ");

	private String codigo;

	private String query;

	private Situacao(String codigo, String query) {
		this.codigo = codigo;
		this.query = query;
	}

	@JsonCreator
	public static Situacao getSituacao(String codigo) {
		for (Situacao type : Situacao.values()) {
			if (type.codigo.equals(codigo)) {
				return type;
			}
		}
		return null;
	}

	@JsonValue
	public String getCodigo() {
		return codigo;
	}

	public String getQuery() {
		return query;
	}
}
