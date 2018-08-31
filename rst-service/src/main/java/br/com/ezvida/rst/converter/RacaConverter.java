package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.Raca;

public class RacaConverter implements AttributeConverter<Raca, String> {

		public String convertToDatabaseColumn(Raca value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public Raca convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return Raca.getRaca(value);
		}

}
