package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.Escolaridade;

public class EscolaridadeConverter implements AttributeConverter<Escolaridade, String> {

		public String convertToDatabaseColumn(Escolaridade value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public Escolaridade convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return Escolaridade.getEscolaridade(value);
		}

}
