package br.com.ezvida.rst.converter;

import javax.persistence.AttributeConverter;

import br.com.ezvida.rst.enums.TipoEndereco;

public class TipoEnderecoConverter implements AttributeConverter<TipoEndereco, String>{

	@Override
	public String convertToDatabaseColumn(TipoEndereco value) {
		if (value == null) {
			return null;
		}

		return value.getCodigo();
	}

	@Override
	public TipoEndereco convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}

		return TipoEndereco.getTipoEndereco(value);
	}

}
