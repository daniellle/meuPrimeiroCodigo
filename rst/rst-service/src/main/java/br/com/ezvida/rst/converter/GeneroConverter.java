package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.Genero;

public class GeneroConverter implements AttributeConverter<Genero, String> {

		public String convertToDatabaseColumn(Genero value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public Genero convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return Genero.getGenero(value);
		}

}
