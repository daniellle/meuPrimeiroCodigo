package br.com.ezvida.rst.enums;

import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoDependente implements CodeableEnum {
	
	//@formatter:off
	CONJUGE("01","Cônjuge", "CON"),
	COMPANHEIRO("O2","Companheiro", "COM"),
	FILHO_OU_ENTEADO("03","Filho(a) ou enteado(a)", "FILENT"),
	IRMAO_NETO_OU_BISNETO("04","Irmão(ã), neto(a) ou bisneto(a) sem arrimo dos pais, do(a) qual detenha a guarda judicial", "IRNEBIS"),
	PAIS_AVOS_BISAVOS("05","Pais, avós e bisavós", "PAAVBIS"),
	MENOR_POBRE("06","Menor pobre do qual detenha a guarda judicial", "MEPO"),
	PESSOA_ABSOLUTAMENTE_INCAPAZ("07","A pessoa absolutamente incapaz, da qual seja tutor ou curador", "PEABIIN"),
	FILHO_OU_ENTEADO_UNIVERSITARIO("08","Filho(a) ou enteado(a) universitário(a) ou cursando escola técnica de 2º grau, até 24 (vinte equatro) anos", "FIENUN"),
	EX_CONJUGE("15","Ex-cônjuge", "EX"),
	AGREGADO_OUTROS("99","Agregado/Outros", "AG");
	//@formatter:on
	
	private static final Map<String, TipoDependente> byCodigo = CodeableEnumHelper.mapByCodigo(values());
	private static final Map<String, TipoDependente> byCodigoJson = CodeableEnumHelper.mapByCodigoJson(values());
	
	private final String codigo;
	private final String nome;
	private final String codigoJson;

	private TipoDependente(String codigo, String nome, String codigoJson) {
		this.codigo = codigo;
		this.nome = nome;
		this.codigoJson = codigoJson;
	}

	public static TipoDependente getTipoDependente(String codigo) {
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
