package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoTelefone implements CodeableEnum {

	//@formatter:off
	RESIDENCIAL("1", "Residencial", "R"), 
	COMERCIAL("2", "Comercial", "CO"), 
	FAX("3", "Fax", "F"), 
	CELULAR("4", "Celular", "CE"),
	WHATSAPP("5", "Whatsapp", "W");
	//@formatter:on
	
	private static final Map<String, TipoTelefone> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, TipoTelefone> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private TipoTelefone(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}
	
	public static TipoTelefone getTipoTelefone(String codigo) {
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
