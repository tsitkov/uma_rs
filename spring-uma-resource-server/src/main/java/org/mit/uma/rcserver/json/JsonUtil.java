package org.mit.uma.rcserver.json;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {
	
	public static <T> String toString(T container) {
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(container);
		} catch (JsonProcessingException exception) {
			result = "Jackson json failed to convert to string object of type " + container.getClass().toString();
		}
		return result;
	}
	
	public static <T> Set<T> fromJsonArray(String jsonString, Class<T> type) 
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Set<T> result = mapper.readValue(jsonString,TypeFactory.defaultInstance().
				constructCollectionType(Set.class, type));

		return result;
	}

}
