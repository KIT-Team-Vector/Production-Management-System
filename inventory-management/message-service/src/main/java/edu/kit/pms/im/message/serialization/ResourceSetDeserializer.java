package edu.kit.pms.im.message.serialization;

import java.util.Map;


import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.pms.im.common.concepts.ResourceSetImpl;


public class ResourceSetDeserializer implements Deserializer<ResourceSetImpl> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public ResourceSetImpl deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		ResourceSetImpl object = null;
		try {
			object = mapper.readValue(data, ResourceSetImpl.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
