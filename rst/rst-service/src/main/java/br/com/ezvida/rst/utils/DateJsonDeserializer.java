package br.com.ezvida.rst.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateJsonDeserializer extends JsonDeserializer<Date> {
	public static final String FORMATO_DE_DATA_SEM_HORA_BR = "dd/MM/yyyy";

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
		String data = jsonparser.getText();
		try {
			String[] strings = { FORMATO_DE_DATA_SEM_HORA_BR };
			return DateUtils.parseDate(data, strings);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
    }
}
