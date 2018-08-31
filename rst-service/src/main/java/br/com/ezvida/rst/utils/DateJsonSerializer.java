package br.com.ezvida.rst.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateJsonSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		arg1.writeString(format.format(arg0));

	}
}
