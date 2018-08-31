package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SituacaoTrabalhador implements CodeableEnum {
	// @formatter:off
	ATIVO("1", "Ativo", "AT"),
	AFASTADO("2", "Afastado", "AF"),
	APOSENTADO("3", "Aposentado", "AP"),
	OUTRO("4", "Outro", "OU");
	// @formatter:on
	
	private static final Map<String, SituacaoTrabalhador> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, SituacaoTrabalhador> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private SituacaoTrabalhador(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static SituacaoTrabalhador getSituacaoTrabalhador(String codigo) {
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
