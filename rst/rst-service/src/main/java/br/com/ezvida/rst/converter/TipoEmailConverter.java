package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.TipoEmail;

public class TipoEmailConverter implements AttributeConverter<TipoEmail, String> {

	@Override
	public String convertToDatabaseColumn(TipoEmail value) {
		if (value == null) {
			return null;
		}
		return value.getCodigo();
	}

	@Override
	public TipoEmail convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return TipoEmail.getTipoEmail(value);
	}

}
