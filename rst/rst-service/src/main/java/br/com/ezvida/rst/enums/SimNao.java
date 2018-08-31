package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SimNao {

	SIM("S", true), NAO("N", false);

	private final String codigo;

	private final boolean codigoJson;

	private SimNao(String codigo, boolean valor) {
		this.codigo = codigo;
		this.codigoJson = valor;
	}

	public static SimNao getSimNao(String codigo) {
		for (SimNao enumType : SimNao.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}


	@JsonCreator
	public static SimNao getSimNao(boolean codigoJson) {
		for (SimNao type : SimNao.values()) {
			if (type.codigoJson == codigoJson) {
				return type;
			}
		}
		return null;
	}

	public String getCodigo() {
		return codigo;
	}

	@JsonValue
	public boolean getCodigoJson() {
		return codigoJson;
	}
}
