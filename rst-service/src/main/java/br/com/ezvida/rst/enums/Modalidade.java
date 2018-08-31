package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Modalidade implements CodeableEnum {

	PRESENCIAL("1", "PRESENCIAL", "PR"), EAD("2", "EAD", "EA"), HIBRIDA("3", "HIBRIDA", "HI");

	private static final Map<String, Modalidade> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, Modalidade> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private Modalidade(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}
	public String getNome() {
		return nome;
	}
	
	public static Modalidade getModalidade(String codigo) {
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

	@Override
	@JsonValue
	public String getCodigoJson() {
		return codigoJson;
	}

}
