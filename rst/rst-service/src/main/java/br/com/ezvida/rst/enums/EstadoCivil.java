package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoCivil {

	//@formatter:off 
	CASADO("CA", "Casado"),
	DESQUITADO("DQ", "Desquitado"),
	DIVORCIADO("DV", "Divorciado"),
	OUTRO("OU", "Outro"),
	SOLTEIRO("SO", "Solteiro"),
	SEPARADO("SP", "Separado"),
	UNIAO_ESTAVEL("UE", "União Estável"),
	VIUVO("VI", "Viúvo");
	//@formatter:on

	private final String codigo;
	private final String nome;

	private EstadoCivil(String codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	@JsonCreator
	public static EstadoCivil getEstadoCivil(String codigo) {
		for (EstadoCivil enumType : EstadoCivil.values()) {
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
