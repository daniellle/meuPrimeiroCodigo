package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoEmail implements CodeableEnum  {

	//@formatter:off
	PESSOAL("1", "Pessoal", "P"), 
	TRABALHO("2", "Trabalho", "T");
	//@formatter:on
	
	private static final Map<String, TipoEmail> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, TipoEmail> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private TipoEmail(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static TipoEmail getTipoEmail(String codigo) {
		return byCodigo.get(codigo.toLowerCase(Locale.ROOT));
	}

	@JsonCreator
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
