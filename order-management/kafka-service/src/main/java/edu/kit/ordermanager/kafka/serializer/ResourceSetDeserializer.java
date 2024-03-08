package edu.kit.ordermanager.kafka.serializer;

import java.util.Map;


import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.ordermanager.entities.ResourceSet;



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
