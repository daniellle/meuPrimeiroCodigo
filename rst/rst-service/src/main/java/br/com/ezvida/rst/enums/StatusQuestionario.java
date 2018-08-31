package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusQuestionario {

	//@formatter:off 
	EDICAO("E", "Em Edição"),
	PUBLICADO("P", "Publicado"),
	DESATIVADO("D", "Desativado");
	//@formatter:on

	private final String codigo;
	private final String nome;

	private StatusQuestionario(String codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	@JsonCreator
	public static StatusQuestionario getStatusQuestionario(String codigo) {

		for (StatusQuestionario enumType : StatusQuestionario.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonValue
	public String getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}
}
