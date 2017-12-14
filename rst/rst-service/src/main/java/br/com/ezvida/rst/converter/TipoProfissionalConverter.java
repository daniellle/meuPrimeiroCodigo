package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.TipoProfissional;

public class TipoProfissionalConverter implements AttributeConverter<TipoProfissional, String> {

	public String convertToDatabaseColumn(TipoProfissional value) {
		if (value == null) {
			return null;
		}

		return value.getCodigo();
	}

	public TipoProfissional convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return TipoProfissional.getTipoProfissional(value);
	}
	
}
