package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.TipoDependente;

public class TipoDependenteConverter implements AttributeConverter<TipoDependente, String> {

	@Override
	public String convertToDatabaseColumn(TipoDependente value) {
		if (value == null) {
			return null;
		}

		return value.getCodigo();
	}

	@Override
	public TipoDependente convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return TipoDependente.getTipoDependente(value);
	}

}
