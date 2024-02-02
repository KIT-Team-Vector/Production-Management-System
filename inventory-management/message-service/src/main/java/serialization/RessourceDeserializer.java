package serialization;

import java.util.Map;


import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Resource;


public class RessourceDeserializer implements Deserializer<Resource> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public Resource deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		Resource object = null;
		try {
			object = mapper.readValue(data, Resource.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
