package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoResposta {
	
	//@formatter:off 
	SIMPLES("S", "Seleção Simples"),
	MULTIPLA("M", "Seleção Múltipla");
	//@formatter:on
	
	private final String codigo;
	private final String nome;
	
	private TipoResposta(String codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	@JsonCreator
	public static TipoResposta getTipoResposta(String codigo) {
		for (TipoResposta enumType : TipoResposta.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonValue
	public String getCodigo() {
		return this.codigo;
	}

	public String getNome() {
		return nome;
	}

}
