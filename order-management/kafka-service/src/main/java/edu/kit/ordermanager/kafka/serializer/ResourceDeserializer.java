package edu.kit.ordermanager.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.ordermanager.entities.Resource;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;


public class ResourceDeserializer implements Deserializer<Resource> {

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
