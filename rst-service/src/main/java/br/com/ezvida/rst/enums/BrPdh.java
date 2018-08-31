package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BrPdh {

	//@formatter:off 
	NAO_APLICAVEL("NA", "Não Aplicável"),
	BENEFICIARIO_REABILITADO("BR", "Beneficiário Reabilitado"),
	PORTADOR_DEF_HABILITADO("PDH", "Portador de Deficiência Habilitado");
	//@formatter:on

	private final String codigo;
	private final String nome;

	private BrPdh(String codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	@JsonCreator
	public static BrPdh getBrPdh(String codigo) {

		for (BrPdh enumType : BrPdh.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonValue
	public String getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

}
