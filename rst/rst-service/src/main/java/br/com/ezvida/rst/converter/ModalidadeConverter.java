package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.Modalidade;

public class ModalidadeConverter implements AttributeConverter<Modalidade, String> {

		public String convertToDatabaseColumn(Modalidade value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public Modalidade convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return Modalidade.getModalidade(value);
		}

}
