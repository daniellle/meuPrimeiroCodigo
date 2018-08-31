package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FaixaSalarial implements CodeableEnum {

	//@formatter:off 
	ENTRE_1_E_3_SALARIOS("1", "Entre 1 e 3 salários mínimos", "E13"),
	ENTRE_3_E_5_SALARIOS("2", "Entre 3 e 5 salários mínimos", "E35"),
	ENTRE_5_E_10_SALARIOS("3", "Entre 5 e 10 salários mínimos", "E510"),
	MAIS__DE_10_SALARIOS("4", "Mais do que 10 salários mínimos", "M10");
	//@formatter:on
	
	private static final Map<String, FaixaSalarial> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, FaixaSalarial> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private FaixaSalarial(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static FaixaSalarial getFaixaSalarial(String codigo) {
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
