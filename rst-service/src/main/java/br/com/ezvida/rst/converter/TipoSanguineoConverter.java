package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.TipoSanguineo;

public class TipoSanguineoConverter implements AttributeConverter<TipoSanguineo, String> {

	public String convertToDatabaseColumn(TipoSanguineo value) {
		if (value == null) {
			return null;
		}

		return value.getCodigo();
	}

	public TipoSanguineo convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return TipoSanguineo.getTipoSanguineo(value);
	}

}
