package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Escolaridade {

	//@formatter:off 
	ANALFABETO("1", "Analfabeto", "A"),
	FUNDAMENTAL_COMPLETO("2", "Fundamental completo", "FUNCOM"),
	FUNDAMENTAL_INCOMPLETO("3", "Fundamental incompleto", "FUNINC"),
	MEDIO_COMPLETO("4", "Médio completo", "MC"),
	MEDIO_INCOMPLETO("5", "Médio incompleto", "MI"),
	SUPERIOR_COMPLETO("6", "Superior completo", "SM"),
	SUPERIOR_INCOMPLETO("7", "Superior incompleto", "SI"),
	POS_GRADUACAO_COMPLETO("8", "Pós-Graduação completo", "PGC"),
	POS_GRADUACAO_INCOMPLETO("9", "Pós-Graduação incompleto", "PGI");
	//@formatter:on

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private Escolaridade(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static Escolaridade getEscolaridade(String codigo) {
		for (Escolaridade enumType : Escolaridade.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonCreator
	public static Escolaridade getEscolaridadeJson(String codigoJson) {
		for (Escolaridade enumType : Escolaridade.values()) {

			if (enumType.codigoJson.equals(codigoJson)) {
				return enumType;
			}
		}
		return null;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	@JsonValue
	public String getCodigoJson() {
		return codigoJson;
	}

}
