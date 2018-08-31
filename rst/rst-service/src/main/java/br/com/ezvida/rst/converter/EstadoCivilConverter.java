package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.EstadoCivil;

public class EstadoCivilConverter implements AttributeConverter<EstadoCivil, String> {

		public String convertToDatabaseColumn(EstadoCivil value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public EstadoCivil convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return EstadoCivil.getEstadoCivil(value);
		}

}
