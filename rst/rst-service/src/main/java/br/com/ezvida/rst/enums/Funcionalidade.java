package br.com.ezvida.rst.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Funcionalidade {

	//@formatter:off 
	CAT("1", "CAT", "C"),
	EMPRESA("2", "EMPRESA", "E"),
	TRABALHADOR("3", "TRABALHADOR", "T"),
	PROFISSIONAL("4", "PROFISSIONAL", "P"),
	SINDICATO("5", "SINDICATO", "S"),
	DEPARTAMENTO_REGIONAL("6", "DEPARTAMENTO_REGIONAL", "D"),
	PARCEIRO_CREDENCIADO("7", "PARCEIRO_CREDENCIADO", "PC"),
	REDE_CREDENCIADA	("8", "REDE_CREDENCIADA", "RC"),
	PRODUTOS_SERVICOS ("9", "PRODUTOS_SERVICOS", "PS"),	
	USUARIOS ("10", "USUARIOS", "U"),
	RES ("12", "REGISTRO DE SAÚDE", "RES"),	
	TODOS ("11", "TODOS", "TDS"),
	QUESTIONARIOS ("12", "QUESTIONÁRIO", "Q"),
	PESQUISA_SESI ("13", "PESQUISA_SESI", "PQS"),
	QUESTIONARIO_TRABALHADOR ("14", "QUESTIONÁRIO_TRABALHADOR", "QT"),
	SISTEMAS_CREDENCIADOS("15", "SISTEMAS_CREDENCIADOS", "SC");
	//@formatter:on

	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private Funcionalidade(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static Funcionalidade getFuncionalidade(String codigo) {
		for (Funcionalidade enumType : Funcionalidade.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	@JsonCreator
	public static Funcionalidade getTFuncionalidadeJson(String codigoJson) {
		for (Funcionalidade enumType : Funcionalidade.values()) {
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
