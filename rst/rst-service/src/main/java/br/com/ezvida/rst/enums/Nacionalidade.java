package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Nacionalidade implements CodeableEnum {
	
	// @formatter:off
	BRASILEIRO("1", "Brasileiro", "BR"),
	NATURALIZADO("2", "Naturalizado", "NA"),
	ESTRANGEIRO("3", "Estrangeiro", "ES");
	// @formatter:on
	
	private static final Map<String, Nacionalidade> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, Nacionalidade> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());


	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private Nacionalidade(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}
	
	public static Nacionalidade getNacionalidade(String codigo) {
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
