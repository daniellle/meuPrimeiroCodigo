package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.TipoTelefone;

public class TipoTelefoneConverter implements AttributeConverter<TipoTelefone, String> {

	@Override
	public String convertToDatabaseColumn(TipoTelefone value) {
		if (value == null) {
			return null;
		}
		return value.getCodigo();
	}

	@Override
	public TipoTelefone convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return TipoTelefone.getTipoTelefone(value);
	}

}
