package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoEndereco {

	//@formatter:off 
	PRINCIPAL("1", "Principal", "P"),
	COMERCIAL("2", "Comercial", "C"),
	FINANCEIRO("3", "Financeiro", "F");
	//@formatter:on

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private TipoEndereco(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static TipoEndereco getTipoEndereco(String codigo) {
		for (TipoEndereco enumType : TipoEndereco.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonCreator
	public static TipoEndereco getTipoEnderecoJson(String codigoJson) {
		for (TipoEndereco enumType : TipoEndereco.values()) {
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
