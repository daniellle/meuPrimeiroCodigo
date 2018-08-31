package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.StatusQuestionario;

public class StatusQuestionarioConverter implements AttributeConverter<StatusQuestionario, String> {

		public String convertToDatabaseColumn(StatusQuestionario value) {
			if (value == null) {
				return null;
			}

			return value.getCodigo();
		}

		public StatusQuestionario convertToEntityAttribute(String value) {
			if (value == null) {
				return null;
			}

			return StatusQuestionario.getStatusQuestionario(value);
		}

}
