package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.BrPdh;

public class BrPdhConverter implements AttributeConverter<BrPdh, String> {

		public String convertToDatabaseColumn(BrPdh value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public BrPdh convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return BrPdh.getBrPdh(value);
		}

}
