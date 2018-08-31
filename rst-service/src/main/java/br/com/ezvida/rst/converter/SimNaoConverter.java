package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.SimNao;

public class SimNaoConverter implements AttributeConverter<SimNao, String> {

	@Override
	public String convertToDatabaseColumn(SimNao value) {
		if (value == null) {
			return null;
		}
		return value.getCodigo();
	}

	@Override
	public SimNao convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return SimNao.getSimNao(value);
	}

}
