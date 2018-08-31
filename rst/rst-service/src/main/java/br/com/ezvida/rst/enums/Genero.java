package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genero {

	//@formatter:off 
	MASCULINO("M", "MASCULINO"),
	FEMININO("F", "FEMININO");
	//@formatter:on

	private final String codigo;
	private final String nome;

	private Genero(String codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	@JsonCreator
	public static Genero getGenero(String codigo) {
		for (Genero enumType : Genero.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}

		}
		return null;
	}

	@JsonValue
	public String getCodigo() {
		return this.codigo;
	}

	public String getNome() {
		return nome;
	}

}
