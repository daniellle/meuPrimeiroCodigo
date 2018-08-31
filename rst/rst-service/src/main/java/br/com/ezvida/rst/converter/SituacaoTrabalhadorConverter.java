package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.SituacaoTrabalhador;

public class SituacaoTrabalhadorConverter implements AttributeConverter<SituacaoTrabalhador, String> {

	public String convertToDatabaseColumn(SituacaoTrabalhador value) {
		if (value == null) {
			return null;
		}
		return value.getCodigo();
	}

	public SituacaoTrabalhador convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return SituacaoTrabalhador.getSituacaoTrabalhador(value);
	}

}
