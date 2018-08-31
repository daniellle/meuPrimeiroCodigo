package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoProfissional {
	
	//@formatter:off 
	MEDICO("1", "Médico(a)", "ME"),
	FONOAUDIOLOGO("2", "Fonoaudiólogo(a)", "FO"),
	ENFERMEIRO("3", "Enfermeiro(a)", "ENF"),
	ENGENHEIRO_SEGURANCA_TRABALHO("4", "Engenheiro(a) de Segurança do Trabalho", "ENG"),
	TECNICO_SEGURANCA_TRABALHO("5", "Técnico(a) de Segurança do Trabalho", "TECSEGTRAB"),
	TECNICO_ENFERMAGEM("6", "Técnico de Enfermagem", "TECENF"),
	AUXILIAR_ENFERMAGEM("7", "Auxiliar de Enfermagem", "AUXENF"),
	TECNICO_SEGURANCA("8", "Técnico(a) de Segurança", "TECSEG"),
	ENGENHEIRO_SEGURANCA("9", "Engenheiro(a) de Segurança", "ENGSEG"),
	TECNOLOGO_SEGURANCA_TRABALHO("10", "Tecnólogo de Segurança do Trabalho", "TECNOSEGTRAB"),
	ARQUITETO_URBANISTA("11", "Arquiteto e Urbanista - Especialista em Segurança do Trabalho", "ARQURB");
	//@formatter:on

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private TipoProfissional(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static TipoProfissional getTipoProfissional(String codigo) {
		for (TipoProfissional enumType : TipoProfissional.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonCreator
	public static TipoProfissional getTipoProfissionalJson(String codigoJson) {
		for (TipoProfissional enumType : TipoProfissional.values()) {
			if (enumType.codigoJson.equals(codigoJson)) {
				return enumType;
			}
		}
		return null;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public String getNome() {
		return nome;
	}

	@JsonValue
	public String getCodigoJson() {
		return codigoJson;
	}
}