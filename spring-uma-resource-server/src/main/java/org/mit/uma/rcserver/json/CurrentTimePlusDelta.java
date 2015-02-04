package org.mit.uma.rcserver.json;

//import java.lang.Integer;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CurrentTimePlusDelta extends JsonDeserializer<Date> {
    
	@Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String timestamp = jp.getText().trim();        
        try {
        	Calendar calendar = Calendar.getInstance();
        	calendar.add(Calendar.SECOND, Integer.parseInt(timestamp));
        	return calendar.getTime();
        } catch (NumberFormatException e) {
        	throw new IOException("Cannot deserialize timestamp " + timestamp);
        }
    }
	
}
