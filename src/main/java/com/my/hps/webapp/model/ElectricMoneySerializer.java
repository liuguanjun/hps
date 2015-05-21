package com.my.hps.webapp.model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ElectricMoneySerializer extends JsonSerializer<Number> {

	@Override
	public void serialize(Number value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		NumberFormat format = null;
		if (value instanceof Long) {
			format = new DecimalFormat("#,##0");
		} else {
			format = new DecimalFormat("#,##0.000");
		}
		jgen.writeString(format.format(value.doubleValue()));
	}
}