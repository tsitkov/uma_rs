package org.mit.uma.rcserver.json;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TimestampDeserializer extends JsonDeserializer<Date> {
    
	@Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String timestamp = jp.getText().trim();

        try {
            return new Date(Long.valueOf(timestamp + "000"));
        } catch (NumberFormatException e) {
        	throw new IOException("Cannot deserialize timestamp " + timestamp);
        }
    }
	
}
