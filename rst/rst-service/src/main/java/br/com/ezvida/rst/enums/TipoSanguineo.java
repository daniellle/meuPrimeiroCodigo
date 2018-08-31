package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoSanguineo implements CodeableEnum {

	// @formatter:off
	A_POSITIVO("A+", "A Positivo", "AP"),
	A_NEGATIVO("A-", "A Negativo", "AN"),
	AB_POSITIVO("AB+", "AB Positivo", "ABP"),
	AB("AB", "AB", "AB"),
	B_POSITIVO("B+", "B Positivo", "BP"),
	B_NEGATIVO("B-", "B Negativo", "BN"),
	O_POSITIVO("O+", "O Positivo", "0P"),
	O_NEGATIVO("O-", "O Negativo", "0N");
	// @formatter:on
	
	private static final Map<String, TipoSanguineo> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, TipoSanguineo> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());


	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private TipoSanguineo(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}
	
	public static TipoSanguineo getTipoSanguineo(String codigo) {
		return byCodigo.get(codigo.toLowerCase(Locale.ROOT));
	}
	
	@Override
	public CodeableEnum getByCodigo(String codigo) {
		return byCodigo.get(codigo.toLowerCase(Locale.ROOT));
	}

	@Override
	@JsonCreator
	public CodeableEnum getByCodigoJson(String codigoJson) {
		return byCodigoJson.get(codigoJson.toLowerCase(Locale.ROOT));
	}

	@Override
	public String getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	@Override
	@JsonValue
	public String getCodigoJson() {
		return codigoJson;
	}

}
