package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.TipoResposta;

public class TipoRespostaConverter implements AttributeConverter<TipoResposta, String> {

	public String convertToDatabaseColumn(TipoResposta value) {
		if (value == null) {
			return null;
		}
		return value.getCodigo();
	}

	public TipoResposta convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		return TipoResposta.getTipoResposta(value);
	}

}
