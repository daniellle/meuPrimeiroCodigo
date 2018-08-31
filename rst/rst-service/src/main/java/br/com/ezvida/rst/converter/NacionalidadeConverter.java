package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.Nacionalidade;

public class NacionalidadeConverter implements AttributeConverter<Nacionalidade, String> {

	public String convertToDatabaseColumn(Nacionalidade value) {
		if (value == null) {
			return null;
		}
		return value.getCodigo();
	}

	public Nacionalidade convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return Nacionalidade.getNacionalidade(value);
	}

}