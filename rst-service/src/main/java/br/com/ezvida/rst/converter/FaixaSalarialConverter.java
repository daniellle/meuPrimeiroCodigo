package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.FaixaSalarial;

public class FaixaSalarialConverter implements AttributeConverter<FaixaSalarial, String> {

		public String convertToDatabaseColumn(FaixaSalarial value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public FaixaSalarial convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return FaixaSalarial.getFaixaSalarial(value);
		}

}
