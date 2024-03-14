package edu.kit.ordermanager.services.rest.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.ordermanager.entities.ResourceSet;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;



public class ResourceSetDeserializer implements Deserializer<ResourceSet> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public ResourceSet deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		ResourceSet object = null;
		try {
			object = mapper.readValue(data, ResourceSet.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
