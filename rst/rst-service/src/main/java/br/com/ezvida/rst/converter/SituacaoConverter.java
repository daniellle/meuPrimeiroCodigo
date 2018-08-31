package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.Situacao;

public class SituacaoConverter implements AttributeConverter<Situacao, String> {

	@Override
	public String convertToDatabaseColumn(Situacao value) {
		if (value == null) {
			return null;
		}
		return value.getCodigo();
	}

	@Override
	public Situacao convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return Situacao.getSituacao(value);
	}

}
