package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Raca implements CodeableEnum {

	// @formatter:off
	BRANCA("1", "Branca", "B"), PRETA("2", "Preta", "PR"), AMARELA("3", "Amarela", "A"), PARDA("4", "Parda",
			"PA"), INDIGENA("5", "Indígena", "I"), SEM_DECLARACAO("6", "Sem declaração", "SD");
	// @formatter:on

	private static final Map<String, Raca> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, Raca> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private Raca(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static Raca getRaca(String codigo) {
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

	@Override
	public CodeableEnum getByCodigo(String codigo) {
		return byCodigo.get(codigo.toLowerCase(Locale.ROOT));
	}

}
